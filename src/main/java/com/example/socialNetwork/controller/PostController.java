package com.example.socialNetwork.controller;

import com.example.socialNetwork.dto.PostDTO;
import com.example.socialNetwork.entity.Post;
import com.example.socialNetwork.facade.PostFacade;
import com.example.socialNetwork.payload.response.MessageResponse;
import com.example.socialNetwork.service.PostService;
import com.example.socialNetwork.validators.ResponseErrorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/post")
@CrossOrigin
public class PostController {

    @Autowired
    private PostFacade postFacade;

    @Autowired
    private PostService postService;

    @Autowired
    private ResponseErrorValidator responseErrorValidator;

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDTO postDTO, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> listErrors = responseErrorValidator.mappedValidatorService(bindingResult);
        if (!ObjectUtils.isEmpty(listErrors)) return listErrors;

        Post post = postService.createPost(postDTO, principal);
        PostDTO postCreated = postFacade.postToPostDTO(post);

        return new ResponseEntity<>(postCreated, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") String postId, Principal principal) {
        postService.deletePost(Long.parseLong(postId), principal);
        return new ResponseEntity<>(new MessageResponse("The post " + postId + " were deleted"), HttpStatus.OK);
    }

    // ("/all) - getAllPosts (GET-запрос)
    // ("/user/posts) - getAllPostsForUser (GET-запрос)
    // ("{postId}/{username}/{like}") - likePost (POST-запрос)
}
