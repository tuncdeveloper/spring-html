package com.quest_app.quest.controller;

import com.quest_app.quest.DTO.CommentDto;
import com.quest_app.quest.DTO.PostDto;
import com.quest_app.quest.model.Post;
import com.quest_app.quest.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/posts")
public class PostController {

    private final PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(path = "allPostsByUser")
    public ResponseEntity<List<PostDto>> getAllPostByUserId(@RequestParam Optional<Integer> userId) {
        return postService.getAllPostByUserId(userId);
    }

    @GetMapping(path = "allPosts")
    public ResponseEntity<List<PostDto>> getAllPost(){
        return postService.getAllPost();
    }


    @GetMapping(path = "onePost/{id}")
    public ResponseEntity<PostDto> getOnePostById(@PathVariable("id") Integer postId){
        return postService.getOnePostById(postId);
    }

    @PostMapping("addPost")
    public ResponseEntity<Post> createPost(@RequestBody PostDto postDTO){
        return postService.createPost(postDTO);
    }

    @DeleteMapping(path = "deletePost/ {id}")
    public void deletePost(@PathVariable("id") Integer postID){
        postService.deletePost(postID);
    }

    @PutMapping(path = "updatePost/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable("id") Integer postID , @RequestBody PostDto postDTO){
        return postService.updatePost(postID,postDTO);
    }



}
