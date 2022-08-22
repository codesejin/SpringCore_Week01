package com.sparta.springcore.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration //스프링이 처음 기동할때 설정해주는
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                // image폴더를 login없이 허용
                .antMatchers("/images/**").permitAll()
                // css폴더를 login없이 허용
                .antMatchers("/css/**").permitAll()
                // 어떤 요청이든 '인증' -> 스프링 서버로 요청이 오는 모든 request에 대해서 인증 과정을 거치겠다
                .anyRequest().authenticated()
                //조건 추가
                .and()
                     //로그인기능 허용
                    .formLogin()
                    .loginPage("/user/login")//url주소 http://localhost:8080/만 쳐도 리다이렉션 시켜줄것임
                    .defaultSuccessUrl("/")
                    .failureUrl("/user/login?error")
                    .permitAll()
                .and()
                    // 로그아웃 기능 허용
                    .logout()
                    .permitAll();
    }
}