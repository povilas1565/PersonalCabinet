package com.example.socialNetwork.service;

import com.example.socialNetwork.entity.Image;
import com.example.socialNetwork.entity.User;
import com.example.socialNetwork.repository.ImageRepository;
import com.example.socialNetwork.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageService.class);

    private final ImageRepository imageRepository;

    private final UserRepository userRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

        public Image uploadImageToProfile(MultipartFile file, Principal principal) throws IOException {
            User user = getUserByPrincipal(principal);
            Image userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);

        //проверяем - есть ли уже фотография пользователя в базе.
        //если есть - тогда удаляем и загружаем новую

        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageRepository.delete(userProfileImage);
        }

        Image image = new Image();
        image.setUserId(user.getId());
        image.setImageBytes(compressImage(file.getBytes()));
        image.setName(file.getName());
        LOG.info("Upload image to use {}", user.getId());

        return imageRepository.save(image);
    }

    public Image getUserProfileImage(Principal principal) {
        User user = getUserByPrincipal(principal);

        Image userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            userProfileImage.setImageBytes(decompressImage(userProfileImage.getImageBytes()));
        }

        return userProfileImage;
    }

    // компрессия фото
    public static byte[] compressImage(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
        byte[] segment = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(segment); //фактическое количество байтов сжатых данных, записанных в выходной буфер
            byteArrayOutputStream.write(segment, 0, count);
        }

        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            LOG.error("Cannot compress image");
        }

        System.out.println("Compressed image size = " + byteArrayOutputStream.toByteArray().length);
        return byteArrayOutputStream.toByteArray();

    }

    // декомпрессия фото
    public static byte[] decompressImage(byte[] data) {
    Inflater inflater = new Inflater();
       inflater.setInput(data);

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
    byte[] segment = new byte[1024];
    try {
        while (!inflater.finished()) {
            int count = inflater.inflate(segment);
            byteArrayOutputStream.write(segment, 0, count);
        }
        byteArrayOutputStream.close();

    } catch (IOException | DataFormatException e) {
        LOG.error("Cannot decompress image");
    }

    return byteArrayOutputStream.toByteArray();
    }

    public User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
    }

}
