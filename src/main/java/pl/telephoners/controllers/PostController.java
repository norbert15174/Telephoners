package pl.telephoners.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import pl.telephoners.DTO.PostDTO;
import pl.telephoners.DTO.PostPageDTO;
import pl.telephoners.models.PersonalData;
import pl.telephoners.models.Post;
import pl.telephoners.models.UserApp;
import pl.telephoners.services.PostService;
import pl.telephoners.services.UserAppService;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "*",allowCredentials = "true",allowedHeaders = "*")
@RestController
@RequestMapping(path = "posts")
public class PostController {


    private PostService postService;
    private UserAppService userAppService;

    @Autowired
    public PostController(PostService postService, UserAppService userAppService) {
        this.postService = postService;
        this.userAppService = userAppService;
    }

    @PostMapping("/addpost")
    public ResponseEntity<Post> addNewPost(@RequestParam("mainfile") MultipartFile file, @RequestParam("galleryfiles") MultipartFile[] multipartFiles, @RequestParam("Post") String postData, @AuthenticationPrincipal Principal user) {
        PersonalData personalData = getUserInformation(user);
        Post post = postService.addNewPost(file, multipartFiles, postData, personalData.getId());
        if (post == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @GetMapping("/page/{page}")
    public ResponseEntity<List<PostPageDTO>> findPosts(@PathVariable int page) {
        return new ResponseEntity<>(postService.findAllPosts(page), HttpStatus.OK);
    }
    @GetMapping("/actual")
    public ResponseEntity<List<Post>> findActualPosts() {
        return new ResponseEntity<>(postService.findActualPosts(), HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Post>> getPostsByName(@PathVariable String name) {
        List<Post> posts = postService.findPostByName(name);
        if (posts == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> findPostById(@PathVariable long id) {
        Post post = postService.findPostById(id);
        if (post == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping("/photos/add/{id}")
    public ResponseEntity<Post> addNewPhotosToPost(@RequestParam("galleryfiles") MultipartFile[] multipartFiles, @PathVariable long id, @AuthenticationPrincipal Principal user) {
        PersonalData personalData = getUserInformation(user);
        if (!postService.checkIfAuthor(personalData.getId(), id)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Post post = postService.addPhotosToPost(multipartFiles, id);
        if (post == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(post, HttpStatus.OK);

    }

    @PostMapping("/update/{postId}")
    public ResponseEntity<Post> updatePost(@RequestParam("topic") String topic, @RequestParam("content") String content, @PathVariable long postId, @AuthenticationPrincipal Principal user) {
        PersonalData personalData = getUserInformation(user);
        if (!postService.checkIfAuthor(personalData.getId(), postId)) return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Post post = postService.updatePost(topic, content, postId);
        if (post == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<List<PostDTO>> getAuthorPosts(@PathVariable long id) {
        List<PostDTO> postDtos = postService.getPostDTOByAuthorId(id);
        if (postDtos == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePostById(@PathVariable long id){
        return postService.deletePostById(id);
    }
    @DeleteMapping("/post/{postid}")
    public ResponseEntity<String> deletePostById(@PathVariable long postid, @RequestParam long id ){
        return postService.deletePhotoById(id,postid);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> updateMainPostPhotoById(@PathVariable long id, @RequestParam("mainfile") MultipartFile file){
        return postService.updateMainPhoto(id,file);
    }

    private PersonalData getUserInformation(Principal user) {
        UserApp userApp = (UserApp) userAppService.loadUserByUsername(user.getName());
        return userApp.getPersonalInformation();
    }

    @GetMapping("/user")
    public String getUser(@AuthenticationPrincipal Principal user){
        return user.getName();
    }

}
