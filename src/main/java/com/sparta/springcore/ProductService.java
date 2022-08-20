package com.sparta.springcore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    //12번 객체 생성으로 가져온 ProductRepository의 정보를 9번 멤버변수에 담아두겠다
    private final ProductRepository productRepository; // 멤버변수
    //DI적용, 미리 만들어져 있는 객체를가져다가 사용하겠다
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Product createProduct(ProductRequestDto requestDto) throws SQLException {
        // 요청받은 DTO 로 DB에 저장할 객체 만들기(서비스의 역할)
        // 반환타입이 Product이라 이걸 컨트롤러에게 넘겨줘야하는데, 아래 객체 생성
        //그리고 그걸 productRepository에 넣어서 세이브하니까 리턴없이 함수만들어도됨
        Product product = new Product(requestDto);
        // [리포지토리 호출하는 과정]
        //인풋으로 Product정보(DB에 저장할 객체)를 넘겨줘야 한다
        //reqeustDto를 인풋로 안하는 이유? myprice를 0원으로 설정하는 부분 때문에 Product해야함
        //Product랑 ProductRequestDto 클래스 각각 컨트롤+클릭으로 들어가서 확인해보기
        productRepository.createProduct(product);
        return product;
    }
    // 설정 가격 변경
    //서비스가 레포지토리를 2번 호출하고 있음 = DB Query 작성이 2번 나옴'
    // 1. 레포지토리에서 클라이언트가 보낸 ID가 있는지 체크하는 부분
    // 2.
    public Product updateProduct(Long id, ProductMypriceRequestDto requestDto) throws SQLException {
        if ( requestDto.getMyprice() <= 0 ) {
            throw new RuntimeException("희망 최저가는 0 원 이상으로 설정해주세요!!!");
        }
        Product product = productRepository.getProduct(id);
        //레포지토리가 아니라 서비스가 검사할 수 있게 한다
         if(product == null) {
            throw new NullPointerException("해당 아이디가 존재하지 않습니다.");
        }
         productRepository.updateMyprice(id, requestDto.getMyprice());
         return product;
    }

    public List<Product> getProducts() throws SQLException {
        List<Product> products = productRepository.getProducts();
        return products;
    }
}
