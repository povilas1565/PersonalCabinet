package com.example.socialNetwork.controller;

import com.example.socialNetwork.dto.CommentDTO;
import com.example.socialNetwork.entity.Comment;
import com.example.socialNetwork.facade.CommentFacade;
import com.example.socialNetwork.payload.response.MessageResponse;
import com.example.socialNetwork.service.CommentService;
import com.example.socialNetwork.validators.ResponseErrorValidator;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comment")
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentFacade commentFacade;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ResponseErrorValidator responseErrorValidator;

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDTO commentDTO,
                                                @PathVariable("postId") String postId,
                                                BindingResult bindingResult,
                                                Principal principal) {

        ResponseEntity<Object> listErrors = responseErrorValidator.mappedValidatorService(bindingResult);
        if (!ObjectUtils.isEmpty(listErrors)) return listErrors;

        Comment comment = commentService.saveComment(Long.parseLong(postId), commentDTO, principal);
        CommentDTO commentCreated = commentFacade.commentToCommentDTO(comment);

        return new ResponseEntity<>(commentCreated, HttpStatus.OK);

    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId) {
        commentService.deleteComment(Long.parseLong(commentId));
        return new ResponseEntity<>(new MessageResponse("The comment " + commentId + " were deleted"), HttpStatus.OK);
    }

    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForPost(@PathVariable("postId") String postId) {
        List<CommentDTO> commentDTOList = commentService.getAllCommentsForPost(Long.parseLong(postId))
                .stream()
                .map(commentFacade::commentToCommentDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
    }


}
