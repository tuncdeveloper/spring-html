package com.quest_app.quest.repository;

import com.quest_app.quest.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface  CommentRepository extends JpaRepository<Comment,Integer> {

    List<Comment> findByUser_userIdAndPost_postId(Integer userId, Integer postId);

    List<Comment> findByUser_userId(Integer userId);

    List<Comment> findByPost_postId(Integer postId);
}
