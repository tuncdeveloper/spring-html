package com.quest_app.quest.repository;

import com.quest_app.quest.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Integer> {

    List<Post> findByUser_UserId(Integer userId);


}
