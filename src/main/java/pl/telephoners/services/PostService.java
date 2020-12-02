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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.telephoners.DTO.PostDTO;
import pl.telephoners.mappers.PersonalDataObjectMapperClass;
import pl.telephoners.mappers.PostObjectMapperClass;
import pl.telephoners.models.Gallery;
import pl.telephoners.models.PersonalData;
import pl.telephoners.models.Post;
import pl.telephoners.repositories.GalleryRepository;
import pl.telephoners.repositories.PostRepository;

import java.io.IOException;
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

    public PostService(PersonalDataObjectMapperClass personalDataObjectMapperClass, GalleryRepository galleryRepository, PostRepository postRepository, PersonalDataService personalDataService, PostObjectMapperClass postObjectMapperClass) {
        this.personalDataObjectMapperClass = personalDataObjectMapperClass;
        this.galleryRepository = galleryRepository;
        this.postRepository = postRepository;
        this.personalDataService = personalDataService;
        this.postObjectMapperClass = postObjectMapperClass;
    }

    public Post addNewPost(MultipartFile file, MultipartFile[] multipartFiles, String postData, long authorId){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Post post = objectMapper.readValue(postData, Post.class);
            if(postRepository.findPostByPostName(post.getPostName()).isPresent()) return null;
            if(post.getPostName().isBlank()) return null;
            PersonalData personalData = personalDataService.getPersonalDataById(authorId);
            if(personalData==null) return null;
            post.setAuthor(personalData);
            Set<Gallery> galleries = addNewPhotos(multipartFiles,post.getPostName());
            Gallery mainPhoto = addNewMainPhoto(file,post.getPostName());
            galleries.forEach(gallery -> gallery.setPost(post));
            mainPhoto.setPost(post);
            post.setGalleries(galleries);


            post.setMainPhoto(mainPhoto);
            postRepository.save(post);
            return postRepository.findPostByPostName(post.getPostName()).get();
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        }





    }



    public Set<Gallery> addNewPhotos(MultipartFile[] multipartFiles, String postName){

        Set<Gallery> galleryList = new HashSet<>();
        Arrays.asList(multipartFiles).stream().forEach(multipartFile ->
        {
            String path = "post/" + postName + "/";


            for(int i=0;;i++){
                if(!galleryRepository.findFirstByUrl(urlGCP + path + i + multipartFile.getOriginalFilename()).isPresent()){
                    path += i+multipartFile.getOriginalFilename();
                    break;
                }
            }

            Gallery gallery = new Gallery();
            gallery.setUrl(urlGCP + path);
            BlobId blobId = BlobId.of("telephoners",path);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(multipartFile.getContentType()).build();
            try {
                storage.create(blobInfo,multipartFile.getBytes());
                galleryList.add(gallery);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return galleryList;

    }

    public Gallery addNewMainPhoto(MultipartFile file,String postName){
        String path = "post/" + postName + "/main/";
        for(int i=0;;i++){
            if(!galleryRepository.findFirstByUrl(path + i + file.getOriginalFilename()).isPresent()){
                path += i+file.getOriginalFilename();
                break;
            }
        }
        BlobId blobId = BlobId.of("telephoners",path);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        try {
            storage.create(blobInfo,file.getBytes());
            Gallery galleryToSave = new Gallery();
            galleryToSave.setUrl(urlGCP + path);
            return galleryToSave;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<Post> findAllPosts(int page){
        page = page < 0 ?  0 : page;
        Optional<List<Post>> posts = postRepository.findAllPosts(PageRequest.of(page,10));
        return posts.orElse(null);
    }

    public List<Post> findPostByName(String postName){
        Optional<List<Post>> posts = postRepository.findPostsByPostName(postName);
        if(posts.isEmpty()) return null;
        return posts.get();
    }

    public Post findPostById(long id){
        Optional<Post> post = postRepository.findPostById(id);
        if(post.isPresent()) return postRepository.findPostById(id).get();
            return null;

    }

    public Post addPhotosToPost(MultipartFile[] multipartFiles, long id){

        Post post = findPostById(id);

        if(post == null) return null;

        Arrays.asList(multipartFiles).stream().forEach(multipartFile ->
        {
            String path = "post/" + post.getPostName() + "/";


            for(int i=0;;i++){
                if(!galleryRepository.findFirstByUrl(urlGCP + path + i + multipartFile.getOriginalFilename()).isPresent()){
                    path += i+multipartFile.getOriginalFilename();
                    break;
                }
            }

            Gallery gallery = new Gallery();
            gallery.setUrl(urlGCP + path);
            BlobId blobId = BlobId.of("telephoners",path);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(multipartFile.getContentType()).build();
            try {
                storage.create(blobInfo,multipartFile.getBytes());
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
        if(post.isEmpty()) return null;
        Post postToUpdate = post.get();
        postToUpdate.setContent(content);
        postToUpdate.setTopic(topic);
        try {
            postRepository.save(postToUpdate);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

        return postToUpdate;
    }

    public List<PostDTO> getPostDTOByAuthorId(long id){
        Optional<List<Post>> posts = postRepository.findPostsByAuthor(id);
        if(posts.isEmpty()) return null;
        return  postObjectMapperClass.mapPostsToPostsDTO(posts.get());

    }

}
