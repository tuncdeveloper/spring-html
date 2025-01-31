package com.quest_app.quest.DTO.mapStruct;

import com.quest_app.quest.DTO.PostDto;
import com.quest_app.quest.model.Post;


//@Mapper(componentModel = "spring")
public interface PostMapper {
    Post postDTOToPost(PostDto postDTO);
    PostDto postToPostDTO(Post post);
}


