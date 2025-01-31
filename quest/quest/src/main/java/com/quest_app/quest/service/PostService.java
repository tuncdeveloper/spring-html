package com.quest_app.quest.service;

import com.quest_app.quest.DTO.CommentDto;
import com.quest_app.quest.DTO.PostDto;
import com.quest_app.quest.DTO.UserDto;
import com.quest_app.quest.model.Comment;
import com.quest_app.quest.model.Post;
import com.quest_app.quest.model.User;
import com.quest_app.quest.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public Post toPost(PostDto postDto) {
        UserDto userDto = userService.getOneUserById(postDto.getUserIdFk()).getBody();
        User user = userService.toUser(userDto);
        Post post = new Post();
        post.setPostId(postDto.getPostId());
        post.setTitle(postDto.getTitle());
        post.setText(postDto.getText());
        post.setUser(user);
        return post;
    }

    public PostDto toPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setPostId(post.getPostId());
        postDto.setText(post.getText());
        postDto.setTitle(post.getTitle());
        postDto.setUserIdFk(post.getUser().getUserId());
        postDto.setUserName(post.getUser().getUserName());

        return postDto;
    }

    public ResponseEntity<List<PostDto>> getAllPostByUserId(Optional<Integer> userId) {
        List<PostDto> postDtoList = new ArrayList<>();
        if (userId.isPresent()) {
            List<Post> posts = postRepository.findByUser_UserId(userId.get());
            for (Post p : posts) {
                postDtoList.add(toPostDto(p));
            }
            return ResponseEntity.ok(postDtoList);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(postDtoList);
    }

    public ResponseEntity<Post> createPost(PostDto postDTO) {
        if (postDTO == null) {
            return ResponseEntity.badRequest().build();
        }
        Post post = toPost(postDTO);
        Post savedPost = postRepository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    public ResponseEntity<PostDto> getOnePostById(Integer postID) {
        Post post = postRepository.findById(postID)
                .orElseThrow(() -> new EntityNotFoundException("Post with ID " + postID + " not found"));
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(toPostDto(post));
    }

    public ResponseEntity<Void> deletePost(Integer postID) {
        if (!postRepository.existsById(postID)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        postRepository.deleteById(postID);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<Post> updatePost(Integer postID, PostDto newPostDto) {
        Post foundPost = postRepository.findById(postID)
                .orElseThrow(() -> new EntityNotFoundException("Post with ID " + postID + " not found"));

        if (newPostDto.getUserIdFk() != null) {
            UserDto userDto = userService.getOneUserById(newPostDto.getUserIdFk()).getBody();
            User user = userService.toUser(userDto);
            foundPost.setUser(user);
        }

        if (newPostDto.getTitle() != null && !newPostDto.getTitle().isBlank()) {
            foundPost.setTitle(newPostDto.getTitle());
        }
        if (newPostDto.getText() != null && !newPostDto.getText().isBlank()) {
            foundPost.setText(newPostDto.getText());
        }

        Post updatedPost = postRepository.save(foundPost);
        return ResponseEntity.ok(updatedPost);
    }

    public ResponseEntity<List<PostDto>> getAllPost() {
        List<Post> postList = postRepository.findAll();
        List<PostDto> postDtoList = postList.stream()
                .map(this::toPostDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postDtoList);
    }


}
