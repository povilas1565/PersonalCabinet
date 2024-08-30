package com.example.socialNetwork.controller;

import com.example.socialNetwork.entity.Image;
import com.example.socialNetwork.payload.response.MessageResponse;
import com.example.socialNetwork.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/image")
@CrossOrigin
public class ImageController {

    @Autowired
    ImageService imageService;

    // метод загружает фотографию в профиль пользователя
    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToProfile(@RequestParam("file") MultipartFile file,
                                                                Principal principal) throws IOException {
        imageService.uploadImageToProfile(file, principal);
        return ResponseEntity.ok(new MessageResponse("Image uploaded successfully"));
    }

    @PostMapping("{postid}/upload")
    public ResponseEntity<MessageResponse> uploadImageToPost(@PathVariable("postId") String postId,
                                                             @RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {
        imageService.uploadImageToPost(file, principal, Long.parseLong(postId));
        return ResponseEntity.ok(new MessageResponse("Image uploaded successfully to post " + postId));

    }

    @GetMapping("/{postId}/image")
    public ResponseEntity<Image> getPostImage(@PathVariable String postId) {
        Image postImage = imageService.getPostImage(Long.parseLong(postId));
        return new ResponseEntity<>(postImage, HttpStatus.OK);
    }

}
