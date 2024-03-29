package pl.telephoners.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.telephoners.DTO.PostDTO;
import pl.telephoners.DTO.PostPageDTO;
import pl.telephoners.mappers.PersonalDataObjectMapperClass;
import pl.telephoners.mappers.PostObjectMapperClass;
import pl.telephoners.mappers.PostPageObjectMapperClass;
import pl.telephoners.models.Gallery;
import pl.telephoners.models.PersonalData;
import pl.telephoners.models.Post;
import pl.telephoners.repositories.GalleryRepository;
import pl.telephoners.repositories.PostRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class PostService {

    Storage storage = StorageOptions.getDefaultInstance().getService();
    @Value("${url-gcp}")
    private String urlGCP;

    private PersonalDataObjectMapperClass personalDataObjectMapperClass;
    private GalleryRepository galleryRepository;
    private PostRepository postRepository;
    private PersonalDataService personalDataService;
    private PostObjectMapperClass postObjectMapperClass;
    private PostPageObjectMapperClass postPageObjectMapperClass;

    @Autowired
    public PostService(PersonalDataObjectMapperClass personalDataObjectMapperClass , GalleryRepository galleryRepository , PostRepository postRepository , PersonalDataService personalDataService , PostObjectMapperClass postObjectMapperClass , PostPageObjectMapperClass postPageObjectMapperClass) {
        this.personalDataObjectMapperClass = personalDataObjectMapperClass;
        this.galleryRepository = galleryRepository;
        this.postRepository = postRepository;
        this.personalDataService = personalDataService;
        this.postObjectMapperClass = postObjectMapperClass;
        this.postPageObjectMapperClass = postPageObjectMapperClass;
    }

    @Transactional
    public Post addPhotosToPost(MultipartFile file, MultipartFile[] multipartFiles, long postId, long authorId) {
        ObjectMapper objectMapper = new ObjectMapper();
            Optional<Post> postOpt = postRepository.findPostById(postId);
            if(!postOpt.isPresent()) return null;
            Post post = postOpt.get();
            PersonalData personalData = personalDataService.getPersonalDataById(authorId);
            if (personalData == null) return null;
            if(post.getAuthor().getId() != personalData.getId()) return null;
            if(file != null){
                Gallery mainPhoto = addNewMainPhoto(file, post.getPostName());
                post.setMainPhoto(mainPhoto);
            }
            if(multipartFiles != null){
                Set<Gallery> galleries = addNewPhotos(multipartFiles, post.getPostName());
                post.addPhotoToGallery(galleries);
            }
            postRepository.save(post);
            return postRepository.findPostByPostName(post.getPostName()).get();
    }

    @Transactional
    public ResponseEntity<Post> addPost(Post post, long authorId){
        if(postRepository.findPostByPostName(post.getPostName()).isPresent()) return new ResponseEntity("taki post już istnieje", HttpStatus.CONFLICT);
        PersonalData personalData = personalDataService.getPersonalDataById(authorId);
        if (personalData == null) return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        if(post.getPostName() == null || post.getPostName().isBlank()) return new ResponseEntity("brak nazwy posta", HttpStatus.BAD_REQUEST);
        if(post.getTopic() == null || post.getTopic().isBlank()) return new ResponseEntity("brak tematu posta", HttpStatus.BAD_REQUEST);
        if(post.getContent() == null || post.getContent().isBlank()) return new ResponseEntity("brak opisu posta", HttpStatus.BAD_REQUEST);
        post.setAuthor(personalData);
        post.setPostDate(LocalDate.now());
        return new ResponseEntity(postRepository.save(post), HttpStatus.CREATED);
    }

    @Transactional
    public Set<Gallery> addNewPhotos(MultipartFile[] multipartFiles, String postName) {

        Set<Gallery> galleryList = new HashSet<>();
        Arrays.asList(multipartFiles).stream().forEach(multipartFile ->
        {
            String path = "post/" + postName + "/";


            for (int i = 0; ; i++) {
                if (!galleryRepository.findFirstByUrl(urlGCP + path + i + multipartFile.getOriginalFilename()).isPresent()) {
                    path += i + multipartFile.getOriginalFilename();
                    break;
                }
            }

            Gallery gallery = new Gallery();
            gallery.setUrl(urlGCP + path);
            BlobId blobId = BlobId.of("telephoners", path);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(multipartFile.getContentType()).build();
            try {
                storage.create(blobInfo, multipartFile.getBytes());
                galleryList.add(gallery);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return galleryList;

    }
    @Transactional
    public Gallery addNewMainPhoto(MultipartFile file, String postName) {
        String path = "post/" + postName + "/main/";
        for (int i = 0; ; i++) {
            if (!galleryRepository.findFirstByUrl(path + i + file.getOriginalFilename()).isPresent()) {
                path += i + file.getOriginalFilename();
                break;
            }
        }
        BlobId blobId = BlobId.of("telephoners", path);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        try {
            storage.create(blobInfo, file.getBytes());
            Gallery galleryToSave = new Gallery();
            galleryToSave.setUrl(urlGCP + path);
            return galleryToSave;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<PostPageDTO> findAllPosts(int page) {
        page = page < 0 ? 0 : page;
        Optional<List<Post>> posts = postRepository.findAllPosts(PageRequest.of(page, 5));

        if (posts.isPresent()) {
            List<PostPageDTO> postPageDTOS = postPageObjectMapperClass.mapPostsToPostsDTO(posts.get());
            return postPageDTOS;
        }
        return null;
    }

    public List<Post> findActualPosts() {
        Optional<List<Post>> posts = postRepository.findAllPosts(PageRequest.of(0, 3));
        return posts.orElse(null);
    }

    public List<Post> findPostByName(String postName) {
        Optional<List<Post>> posts = postRepository.findPostsByPostName(postName);
        if (posts.isEmpty()) return null;
        return posts.get();
    }

    public PostPageDTO findPostById(long id) {
        Optional<Post> post = postRepository.findPostById(id);
        if (post.isPresent()) {
            PostPageDTO postPageDTOS = postPageObjectMapperClass.mapPostToPostDTO(post.get());
            return postPageDTOS;
        }
        return null;
    }

    //to modify
    public boolean deletePost() {
        BlobId blobId = BlobId.of("telephoners", "post/Norbercik/039e1987ec0e3872933e37a6dc7340bab.jpg");
        storage.delete(blobId);
        return true;
    }

    public ResponseEntity<String> deletePostById(long id) {
        if (!postRepository.findPostById(id).isPresent())
            return new ResponseEntity<>("We couldn't delete the post", HttpStatus.BAD_REQUEST);
        postRepository.deleteById(id);
        return new ResponseEntity<>("Post has been deleted", HttpStatus.OK);
    }

    public ResponseEntity<String> deletePhotoById(long id, long postid) {
        Optional<Gallery> gallery = galleryRepository.findById(id);
        Optional<Post> post = postRepository.findPostById(postid);
        if (!gallery.isPresent() && !post.isPresent())
            return new ResponseEntity<>("We couldn't delete the photo", HttpStatus.BAD_REQUEST);
        galleryRepository.deleteById(id);
        Post postToUpdate = post.get();
        postToUpdate.deletePhotoFromGallery(gallery.get());
        postRepository.save(postToUpdate);
        return new ResponseEntity<>("The photo has been deleted", HttpStatus.OK);
    }

    ;

    @Transactional
    public Post addPhotosToPost(MultipartFile[] multipartFiles, long id) {
        if (!postRepository.findPostById(id).isPresent())
            return null;
        Post post = postRepository.findPostById(id).get();


        Arrays.asList(multipartFiles).stream().forEach(multipartFile ->
        {
            String path = "post/" + post.getPostName() + "/";


            for (int i = 0; ; i++) {
                if (!galleryRepository.findFirstByUrl(urlGCP + path + i + multipartFile.getOriginalFilename()).isPresent()) {
                    path += i + multipartFile.getOriginalFilename();
                    break;
                }
            }

            Gallery gallery = new Gallery();
            gallery.setUrl(urlGCP + path);
            BlobId blobId = BlobId.of("telephoners", path);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(multipartFile.getContentType()).build();
            try {
                storage.create(blobInfo, multipartFile.getBytes());
                post.addPhotoToGallery(gallery);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        postRepository.save(post);
        return post;

    }


    public Post updatePost(String topic, String content, long postId) {
        Optional<Post> post = postRepository.findPostById(postId);
        if (post.isEmpty()) return null;
        Post postToUpdate = post.get();
        postToUpdate.setContent(content);
        postToUpdate.setTopic(topic);
        try {
            postRepository.save(postToUpdate);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

        return postToUpdate;
    }

    public List<PostDTO> getPostDTOByAuthorId(long id) {
        Optional<List<Post>> posts = postRepository.findPostsByAuthor(id);
        if (posts.isEmpty()) return null;
        return postObjectMapperClass.mapPostsToPostsDTO(posts.get());

    }

    public boolean checkIfAuthor(long idAuthor, long idPost) {
        Optional<Post> post = postRepository.findPostById(idPost);
        if (post.isPresent()) {
            return post.get().getAuthor().getId() == idAuthor;
        }
        return false;
    }
    @Transactional
    public ResponseEntity<String> updateMainPhoto(long id, MultipartFile file) {
        Optional<Post> post = postRepository.findPostById(id);
        if (!post.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Post postToSave = post.get();
        Gallery mainPhotoPath = addNewMainPhoto(file, postToSave.getPostName());
        if (mainPhotoPath == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        postToSave.setMainPhoto(mainPhotoPath);
        postRepository.save(postToSave);
        galleryRepository.deleteById(post.get().getMainPhoto().getId());
        return new ResponseEntity<>("Photos has been changed", HttpStatus.OK);
    }

    public ResponseEntity<Long> getPostsAmount() {
        return new ResponseEntity<>(postRepository.count(), HttpStatus.OK);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void init(){
//        deletePost();
//    }

}
