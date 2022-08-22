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
                // 어떤 요청이든 '인증' -> 스프링 서버로 요청이 오는 모든 request에 대해서 인증 과정을 거치겠다
                .anyRequest().authenticated()
                //조건 추가
                .and()
                    // 로그인 기능 허용
                    .formLogin()
                    //formLogin이 성공하게 되면 어느쪽으로 이동시킬까요 -> /로 루트 위치
                    .defaultSuccessUrl("/")
                    //로그인에 관련된 기능에 대해 허용을 해줘라
                    //맨위에 어떤 요청이든 인증을 하게되면 로그인 페이지도 못 들어갈 수 있기에
                    .permitAll()
                .and()
                    // 로그아웃 기능 허용
                    .logout()
                    .permitAll();
    }
}