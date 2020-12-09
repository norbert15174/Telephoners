package pl.telephoners.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import pl.telephoners.DTO.PostDTO;
import pl.telephoners.models.Post;
import pl.telephoners.services.PostService;

import java.util.List;

@RestController
@RequestMapping(path = "posts")
public class PostController {


    private PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("upload-images/{authorId}")
    public ResponseEntity<Post> uploadImage(@RequestParam("mainfile") MultipartFile file, @RequestParam("galleryfiles") MultipartFile[] multipartFiles, @RequestParam("Post") String postData,@PathVariable long authorId ){

        Post post = postService.addNewPost(file,multipartFiles,postData,authorId);
        if(post==null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(post,HttpStatus.CREATED);
    }

    @GetMapping("/page/{page}")
    public ResponseEntity<List<Post>> findPosts(@PathVariable int page){
        return new ResponseEntity<>(postService.findAllPosts(page),HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Post>> getPostsByName(@PathVariable String name){
        List<Post> posts = postService.findPostByName(name);
        if(posts == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> findPostById(@PathVariable long id){
        Post post = postService.findPostById(id);
        if(post == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(post,HttpStatus.OK);
    }

    @PostMapping("/photos/add/{id}")
    public ResponseEntity<Post> addNewPhotosToPost(@RequestParam("galleryfiles") MultipartFile[] multipartFiles, @PathVariable long id){

        Post post = postService.addPhotosToPost(multipartFiles,id);
        if(post == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(post,HttpStatus.OK);

    }

    @PostMapping("/update/{postId}")
    public ResponseEntity<Post> updatePost(@RequestParam("topic") String topic,@RequestParam("content") String content, @PathVariable long postId){
        Post post = postService.updatePost(topic,content,postId);
        if(post == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(post,HttpStatus.OK);
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<List<PostDTO>> getAuthorPosts(@PathVariable long id){
        List<PostDTO> postDtos = postService.getPostDTOByAuthorId(id);
        if(postDtos == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(postDtos,HttpStatus.OK);
    }

    @GetMapping("/user")
    public void getUser(@AuthenticationPrincipal UsernamePasswordAuthenticationToken user){
        System.out.println("asd");
    }

}
