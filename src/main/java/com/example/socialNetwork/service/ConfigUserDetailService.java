package com.example.socialNetwork.service;

import com.example.socialNetwork.entity.User;
import com.example.socialNetwork.exceptions.UserAlreadyExistException;
import com.example.socialNetwork.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/*Для чего это нужно? Задача которую мы тут решаем - нам нужно найти пользователя в базе данных и положить его и его роли в Spring Security контекст.
 Но я не могу положить какой попало объект. Спринг будет понимать только объекты из «своего круга». Поэтому мне нужно будет достать моего пользователя из базы данных и «перегнать» его в объект который будет подходящий для Spring Security.
 А это и есть UserDetails. UserDetailsService будет служить вспомогательным интерфейсом для этой цели.*/
@Service
public class ConfigUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public ConfigUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

        //1. ищем пользователя в БД
        @Override
        public UserDetails loadUserByUsername(String username) {
            User user = userRepository.findUserByEmail(username).orElseThrow(() -> new UserAlreadyExistException("Пользовательной не найден" + username));
            return initUser(user);
        }

        public User loadUserById(Long id) {
             return userRepository.findUserById(id).orElse(null);
        }

       //2. «Перегоняем» его в объект который будет подходящий для Spring Security
    public static User initUser(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(ERole -> new SimpleGrantedAuthority(ERole.name()))
                .collect(Collectors.toList());
        return new User(user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                authorities);
    }
}
