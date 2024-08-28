package com.example.socialNetwork.security;

import com.example.socialNetwork.security.jwt.JWTAuthenticationEntryPoint;
import com.example.socialNetwork.security.jwt.JWTAuthenticationFilter;
import com.example.socialNetwork.service.ConfigUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
Этот класс расширяет WebSecurityConfigurerAdapter.
Это удобный класс, который позволяет настраивать как WebSecurity, так и HttpSecurity.
* */
//https://dzone.com/articles/spring-boot-security-json-web-tokenjwt-hello-world

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        proxyTargetClass = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    ConfigUserDetailService configUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
         * Cross-Origin Resource Sharing (CORS) — механизм, использующий дополнительные HTTP-заголовки,
         * чтобы дать возможность агенту пользователя получать разрешения на доступ к выбранным ресурсам с сервера
         * на источнике (домене), отличном от того, что сайт использует в данный момент.
         * */
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint) //зададим класс который будет обрабатывать ошибку в нашем случае - jwtAuthenticationEntryPoint
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //любой запрос приходящий на /api/auth/** - разрешить
                .authorizeRequests()
                .antMatchers(SecurityConstants.SIGN_UP_URLS).permitAll()
                .anyRequest().authenticated(); // остальные запросы - должны быть авторизированны

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(configUserDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**", "/actuator");

    }


    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    //для сохранения пароля в БД в шифрованном виде
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    //Украшение метода в классе @Configuration @Bean означает, что возвращаемое значение этого метода станет бобом Spring.
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }
}
