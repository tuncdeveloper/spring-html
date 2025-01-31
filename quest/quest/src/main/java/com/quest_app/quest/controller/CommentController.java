package com.quest_app.quest.controller;

import com.quest_app.quest.DTO.CommentDto;
import com.quest_app.quest.model.Comment;
import com.quest_app.quest.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/comments")
public class CommentController {


    private final CommentService commentService ;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }



    @GetMapping(path = "allComment")
    public ResponseEntity<List<CommentDto>> getAllComments(@RequestParam Optional<Integer>userId ,
                                                           @RequestParam Optional<Integer> postId){
        return commentService.getAllCommentWithParam(userId,postId);
    }


    @GetMapping("OneComment/ {id}")
    public ResponseEntity<CommentDto> getOneComment(@PathVariable("id")Integer commentId){
        return commentService.getOneCommentById(commentId);
    }

    @PostMapping("addComment")
    public ResponseEntity<Comment> addComment(@RequestBody CommentDto commentDto){
        return commentService.addComment(commentDto);
    }


    @PutMapping("updateComment / {id}")
    public ResponseEntity<Comment> updateComment(@PathVariable("id") Integer commentId , @RequestBody CommentDto commentDto){
        return commentService.updateComment(commentId,commentDto);
    }

    @DeleteMapping("deleteComment/ {id}")
    public void deleteComment(@PathVariable("id")Integer commentId){
        commentService.deleteComment(commentId);
    }


}
