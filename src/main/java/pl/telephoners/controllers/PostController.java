package pl.telephoners.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.telephoners.models.Gallery;
import pl.telephoners.models.Post;
import pl.telephoners.services.PostService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("upload-images")
    public void uploadImage(@RequestParam("mainfile") MultipartFile file,@RequestParam("galleryfiles") MultipartFile[] multipartFiles, @RequestParam("Post") String postData ){


        System.out.println(postService.addNewPost(file,multipartFiles,postData));


    }


}
