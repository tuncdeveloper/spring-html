package com.quest_app.quest.service;

import com.quest_app.quest.DTO.LikeDto;
import com.quest_app.quest.DTO.PostDto;
import com.quest_app.quest.DTO.UserDto;
import com.quest_app.quest.model.Comment;
import com.quest_app.quest.model.Like;
import com.quest_app.quest.model.Post;
import com.quest_app.quest.model.User;
import com.quest_app.quest.repository.LikeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    public LikeService(LikeRepository likeRepository, UserService userService, PostService postService, CommentService commentService) {
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
    }

    public Like toLike(LikeDto likeDto) {
        if (likeDto == null) {
            return null;
        }

        UserDto userDto = userService.getOneUserById(likeDto.getUserIdFk()).getBody();
        User user = userService.toUser(userDto);

        PostDto postDto = postService.getOnePostById(likeDto.getPostIdFk()).getBody();
        Post post = postService.toPost(postDto);

        Like like = new Like();
        like.setLikesId(likeDto.getLikeId());
        like.setUser(user);
        like.setPost(post);

        return like;
    }

    public LikeDto toLikeDto(Like like) {
        LikeDto likeDto = new LikeDto();


        likeDto.setLikeId(like.getLikesId());
        likeDto.setUserIdFk(like.getUser().getUserId());
        likeDto.setPostIdFk(like.getPost().getPostId());
        likeDto.setUserName(like.getUser().getUserName());
        likeDto.setPostTitle(like.getPost().getTitle());
        return likeDto;
    }

    public ResponseEntity<List<LikeDto>> getAllLikeWithParam(Optional<Integer> userId, Optional<Integer> postId) {
        List<Like> listLikes;
        List<LikeDto> likeDtoList = new ArrayList<>();

        if (userId.isPresent() && postId.isPresent()) {
            listLikes = likeRepository.findByUser_userIdAndPost_postId(userId.get(), postId.get());
        } else if (userId.isPresent()) {
            listLikes = likeRepository.findByUser_userId(userId.get());
        } else if (postId.isPresent()) {
            listLikes = likeRepository.findByPost_postId(postId.get());
        } else {
            listLikes = likeRepository.findAll();
        }

        for (Like l : listLikes) {
            likeDtoList.add(toLikeDto(l));
        }

        if (likeDtoList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(likeDtoList);
        }
        return ResponseEntity.ok(likeDtoList);
    }

    public ResponseEntity<LikeDto> getOneLikeById(Integer likeId) {
        Like like = likeRepository.findById(likeId)
                .orElseThrow(() -> new EntityNotFoundException("Like with ID " + likeId + " not found"));

        return ResponseEntity.ok(toLikeDto(like));
    }


    public ResponseEntity<Like> addLike(LikeDto likeDto) {
        if (likeDto == null) {
            return ResponseEntity.badRequest().build();
        }

        // Yeni bir Like nesnesi oluşturuluyor.
        Like like = toLike(likeDto);

        // Eğer zaten var olan bir like varsa, bunun yerine güncelleme yapmak istemeyiz.
        if (like.getLikesId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // Yeni kaydın ID'si olmamalı.
        }

        // Save işlemi sadece yeni bir like oluşturulacaksa yapılır.
        likeRepository.save(like);
        return ResponseEntity.status(HttpStatus.CREATED).body(like);
    }


    public ResponseEntity<Like> updateLike(Integer likeId, LikeDto likeDto) {
        Like like = likeRepository.findById(likeId)
                .orElseThrow(() -> new EntityNotFoundException("Like with ID " + likeId + " not found"));

        if (likeDto.getPostIdFk() != null) {
            PostDto postDto = postService.getOnePostById(likeDto.getPostIdFk()).getBody();
            Post post = postService.toPost(postDto);
            like.setPost(post);
        }

        if (likeDto.getUserIdFk() != null) {
            UserDto userDto = userService.getOneUserById(likeDto.getUserIdFk()).getBody();
            User user = userService.toUser(userDto);
            like.setUser(user);
        }

        Like updatedLike = likeRepository.save(like);
        return ResponseEntity.ok((updatedLike));
    }

    public ResponseEntity<Void> deleteLike(Integer likeId) {
        if (!likeRepository.existsById(likeId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        likeRepository.deleteById(likeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
