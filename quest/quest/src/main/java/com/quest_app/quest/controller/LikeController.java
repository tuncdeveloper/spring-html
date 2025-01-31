package com.quest_app.quest.controller;

import com.quest_app.quest.DTO.LikeDto;
import com.quest_app.quest.model.Like;
import com.quest_app.quest.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/likes")
public class LikeController {


    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }


    @GetMapping("allLike")
    public ResponseEntity<List<LikeDto>> getAllLike(@RequestParam Optional<Integer> userId, @RequestParam Optional<Integer> postId) {
        return likeService.getAllLikeWithParam(userId, postId);
    }

    @GetMapping("oneLike/{id}")
    public ResponseEntity<LikeDto> getOneLikeById(@PathVariable("id") Integer likeId) {
        return likeService.getOneLikeById(likeId);
    }

    @PostMapping("addLike")
    public ResponseEntity<Like> addLike(@RequestBody LikeDto likeDto) {
        return likeService.addLike(likeDto);
    }

    @PutMapping("updateLike/{id}")
    public ResponseEntity<Like> updateLike(@PathVariable("id") Integer likeId, @RequestBody LikeDto likeDto) {
        return likeService.updateLike(likeId,likeDto);
    }

    @DeleteMapping("deleteLike/{id}")
    public void deleteLike(@PathVariable("id") Integer likeId) {
        likeService.deleteLike(likeId);
    }

}