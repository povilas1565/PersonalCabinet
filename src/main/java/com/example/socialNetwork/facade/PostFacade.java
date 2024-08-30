package com.example.socialNetwork.facade;

import com.example.socialNetwork.dto.PostDTO;
import com.example.socialNetwork.entity.Post;
import org.springframework.stereotype.Component;

/*Класс для мапинга данных и передачи их на контроллер. */
@Component
public class PostFacade {

    public PostDTO postToPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setUsername(post.getUser().getUsername());
        postDTO.setCaption(postDTO.getCaption());
        postDTO.setTitle(post.getTitle());
        postDTO.setLocation(post.getLocation());
        postDTO.setLikes(post.getLikes());
        postDTO.setLikedUsers(post.getLikedUsers());

        return postDTO;
    }
}
