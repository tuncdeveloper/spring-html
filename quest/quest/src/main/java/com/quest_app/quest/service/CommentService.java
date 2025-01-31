package com.quest_app.quest.service;

import com.quest_app.quest.DTO.CommentDto;
import com.quest_app.quest.DTO.PostDto;
import com.quest_app.quest.DTO.UserDto;
import com.quest_app.quest.model.Comment;
import com.quest_app.quest.model.Post;
import com.quest_app.quest.model.User;
import com.quest_app.quest.repository.CommentRepository;
import com.quest_app.quest.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;

    public CommentService(CommentRepository commentRepository, UserService userService, PostService postService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.postService = postService;
    }


    public Comment toComment(CommentDto commentDto){

        UserDto userDto = userService.getOneUserById(commentDto.getUserIdFk()).getBody();
        User user = userService.toUser(userDto);

        PostDto postDto = postService.getOnePostById(commentDto.getPostIdFk()).getBody();
        Post post = postService.toPost(postDto);

        Comment comment=new Comment();

        comment.setCommentId(commentDto.getCommentId());
        comment.setUser(user);
        comment.setPost(post);
        comment.setText(commentDto.getText());

        return comment;
    }

    public CommentDto tocommentDto(Comment comment){

        CommentDto commentDto = new CommentDto();

        commentDto.setCommentId(comment.getCommentId());
        commentDto.setText(comment.getText());
        commentDto.setUserIdFk(comment.getUser().getUserId());
        commentDto.setPostIdFk(comment.getPost().getPostId());
        commentDto.setUserName(comment.getUser().getUserName());

        return commentDto;

    }


    public ResponseEntity<List<CommentDto>> getAllCommentWithParam(Optional<Integer> userId, Optional<Integer> postId) {
        List<Comment> commentList;
        List<CommentDto> commentDtoList = new ArrayList<>();

        if (userId.isPresent() && postId.isPresent()) {
            commentList = commentRepository.findByUser_userIdAndPost_postId(userId.get(), postId.get());
        } else if (userId.isPresent()) {
            commentList = commentRepository.findByUser_userId(userId.get());
        } else if (postId.isPresent()) {
            commentList = commentRepository.findByPost_postId(postId.get());
        } else {
            commentList = commentRepository.findAll();
        }

        for (Comment c : commentList) {
            CommentDto commentDto = tocommentDto(c);
            commentDtoList.add(commentDto);
        }

        return ResponseEntity.ok(commentDtoList);
    }



    public ResponseEntity<CommentDto> getOneCommentById(Integer commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with ID " + commentId + " not found"));

        return ResponseEntity.ok(tocommentDto(comment));
    }


    public ResponseEntity<Comment> addComment(CommentDto commentDto) {
        if (commentDto == null) {
            return ResponseEntity.badRequest().build();
        }

        Comment comment = toComment(commentDto);
        Comment savedComment = commentRepository.save(comment);

        return ResponseEntity.status(201).body(savedComment);
    }



    public ResponseEntity<Comment> updateComment(Integer commentId, CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with ID " + commentId + " not found"));

        if (commentDto.getText() != null) {
            comment.setText(commentDto.getText());
        }
        if (commentDto.getPostIdFk() != null) {
            PostDto postDto = postService.getOnePostById(commentDto.getPostIdFk()).getBody();
            Post post = postService.toPost(postDto);
            comment.setPost(post);
        }
        if (commentDto.getUserIdFk() != null) {
            UserDto userDto = userService.getOneUserById(commentDto.getUserIdFk()).getBody();
            User user = userService.toUser(userDto);
            comment.setUser(user);
        }

        Comment updatedComment = commentRepository.save(comment);
        return ResponseEntity.ok(updatedComment);
    }



    public ResponseEntity<Void> deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
        return ResponseEntity.noContent().build();
    }
}
