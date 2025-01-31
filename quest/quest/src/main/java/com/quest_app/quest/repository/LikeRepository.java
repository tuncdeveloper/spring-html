package com.quest_app.quest.repository;

import com.quest_app.quest.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like,Integer> {
    List<Like> findByUser_userIdAndPost_postId(Integer integer, Integer integer1);

    List<Like> findByUser_userId(Integer integer);

    List<Like> findByPost_postId(Integer integer);
}
