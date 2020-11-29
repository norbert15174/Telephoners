package pl.telephoners.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.telephoners.models.Gallery;
import pl.telephoners.models.Post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "posts")
public class PostController {

    Storage storage = StorageOptions.getDefaultInstance().getService();

    @PostMapping("upload-images")
    public void uploadImage(@RequestParam("mainfile") MultipartFile file,@RequestParam("galleryfiles") MultipartFile[] multipartFiles, @RequestParam("Post") String postData ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Post post = objectMapper.readValue(postData,Post.class);


        List<Gallery> galleryList = new ArrayList();
        Arrays.asList(multipartFiles).stream().forEach(multipartFile ->
        {BlobId blobId = BlobId.of("telephoners","post/" + post.getPostName() + "/" + multipartFile.getOriginalFilename());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(multipartFile.getContentType()).build();
            try {
                storage.create(blobInfo,multipartFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });




    }


}
