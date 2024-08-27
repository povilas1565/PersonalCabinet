package com.example.socialNetwork.security.jwt;

import com.example.socialNetwork.entity.User;
import com.example.socialNetwork.security.SecurityConstants;
import com.example.socialNetwork.service.ConfigUserDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/*
Фильтр — это объект класса, реализующего интерфейс javax.servlet.Filter, который перехватывает запросы на определённые URL и выполняет некоторые действия.
Если имеется несколько фильтров, то они образуют цепочку фильтров — HTTP-запрос после приёма приложением проходит через эту цепочку.
Каждый фильтр в цепочке может обработать запрос, пропустить его к следующим фильтрам в цепочке или не пропустить, сразу отправив ответ клиенту.
Задача нашего фильтра — передать токен из запроса менеджеру аутентификации и, в случае успешной аутентификации, установить контекст безопасности приложения.
* */
/*
JwtRequestFilter расширяет класс Spring Web Filter OncePerRequestFilter.
Для любого входящего запроса выполняется этот класс фильтра. Он проверяет, есть ли в запросе действительный токен JWT.
Если у него есть действующий токен JWT, он устанавливает аутентификацию в контексте, чтобы указать, что текущий пользователь аутентифицирован.
* */
//https://dzone.com/articles/spring-boot-security-json-web-tokenjwt-hello-world
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    public static final Logger LOG = LoggerFactory.getLogger(JWTProvider.class);

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private ConfigUserDetailService configUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJWTFromRequest(request);
            if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
                Long userId = jwtProvider.getUserIdFromToken(jwt);
                User user = configUserDetailService.loadUserById(userId);

                //попробуйем установить авторизацию полученного юзера, обратимся к Spring Security
                //https://www.baeldung.com/manually-set-user-authentication-spring-security
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, null, Collections.emptyList()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //отдаем нашу аутентификацию Spring Security Context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            LOG.error("Can not set user authentication");
        }

        //последний штрих - здесь мы вклиниваемся в цепочку фильтров
        // т.е. мы получили запрос, обработали его, и возвращаем обратно ответ
        filterChain.doFilter(request, response);
    }

    //напишем метод, который берет токен из запроса на сервер
    private String getJWTFromRequest(HttpServletRequest request) {
        //https://dev64.wordpress.com/2012/03/26/http-headers-in-servlet/
        String header = request.getHeader(SecurityConstants.HEADER_STRING);
        if (StringUtils.hasText(header) && header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return header.split(" ")[1]; //парсим наш header вытягивая токен
        }
        return null;
    }
}
