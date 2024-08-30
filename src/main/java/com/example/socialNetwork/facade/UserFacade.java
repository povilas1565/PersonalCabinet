package com.example.socialNetwork.facade;

import com.example.socialNetwork.dto.UserDTO;
import com.example.socialNetwork.entity.User;
import org.springframework.stereotype.Component;

/*Класс для мапинга данных и передачи их на контроллер. */
@Component
public class UserFacade {

    public static UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUserName(user.getUsername());
        userDTO.setInfo(user.getInfo());

        return userDTO;

    }
}
