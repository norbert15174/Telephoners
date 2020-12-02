package pl.telephoners.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.telephoners.models.Gallery;
import pl.telephoners.models.Post;
import pl.telephoners.repositories.GalleryRepository;
import pl.telephoners.repositories.PostRepository;

import java.io.IOException;
import java.util.*;

@Service
public class PostService {

    Storage storage = StorageOptions.getDefaultInstance().getService();

    private GalleryRepository galleryRepository;
    private PostRepository postRepository;
    @Autowired
    public PostService(GalleryRepository galleryRepository, PostRepository postRepository) {
        this.galleryRepository = galleryRepository;
        this.postRepository = postRepository;
    }

    public Post addNewPost(MultipartFile file, MultipartFile[] multipartFiles, String postData){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Post post = objectMapper.readValue(postData, Post.class);
            System.out.println(post.getPostName());
            if(postRepository.findPostByPostName(post.getPostName()).isPresent()) return null;
            if(post.getPostName().isBlank()) return null;
            Set<Gallery> galleries = addNewPhotos(multipartFiles,post.getPostName());
            Gallery mainPhoto = addNewMainPhoto(file,post.getPostName());
            post.setGalleries(galleries);
            if(mainPhoto != null) post.setMainPhoto(mainPhoto);
            postRepository.save(post);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }




        return null;
    }



    public Set<Gallery> addNewPhotos(MultipartFile[] multipartFiles, String postName){

        Set<Gallery> galleryList = new HashSet<>();
        Arrays.asList(multipartFiles).stream().forEach(multipartFile ->
        {
            String path = "post/" + postName + "/" + multipartFile.getOriginalFilename();


            for(int i=0;;i++){
                if(!galleryRepository.findFirstByUrl(path + i + multipartFile.getOriginalFilename()).isPresent()){
                    path += i+multipartFile.getOriginalFilename();
                    break;
                }
            }

            Gallery gallery = new Gallery();
            gallery.setUrl(path);
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
            galleryToSave.setUrl(path);
            return galleryToSave;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
