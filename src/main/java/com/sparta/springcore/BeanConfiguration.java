package com.sparta.springcore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration// 스프링이 처음 기동될때 이 부분을 읽는다
public class BeanConfiguration {

    @Bean//빈등록 :리턴되는값을 빈에 등록하겠다
    public ProductRepository productRepository() {
        String dbUrl = "jdbc:h2:mem:springcoredb";
        String dbId = "sa";
        String dbPassword = "";

        return new ProductRepository(dbUrl, dbId, dbPassword);
    }
}
