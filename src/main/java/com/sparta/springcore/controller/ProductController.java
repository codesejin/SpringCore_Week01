package com.sparta.springcore.controller;

import com.sparta.springcore.model.Product;
import com.sparta.springcore.dto.ProductMypriceRequestDto;
import com.sparta.springcore.dto.ProductRequestDto;
import com.sparta.springcore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.List;

@RequiredArgsConstructor // final로 선언된 멤버 변수를 자동으로 생성합니다.
@RestController // JSON으로 데이터를 주고받음을 선언합니다. // 객체를 json타입으로
public class ProductController {

    private final ProductService productService;
    // 신규 상품 등록
    @PostMapping("/api/products")
    public Product createProduct(@RequestBody ProductRequestDto requestDto) throws SQLException {
        // [서비스 호출하는 과정]
        // 서비스 객체를 생성해야 사용할 수 있음
        // 함수의 인풋(requestDto)과 아웃풋(product)을 정의해주고 함수명 써주면 인텔리제이에서 함수명을 자동으로 만들 수 있다
        //서비스에 넘겨줄 정보는? 클라이언트로 받은 RequestDto를 넘겨줘야 작업 가능하다
        //컨트롤러 입장에서는 반환타입이 Product이기 때문에 Product정보가 나와야함
        Product product = productService.createProduct(requestDto); //productService.createProduct()앞에 this.가 생략된것 / 멤버변수사용하겠다는 의미
        // 응답 보내기
        // 위에 과정은 request를 받아서 db에 저장한 과정
        // 아래 리턴은 클라이언트한테 응답하는 과정 -> json으로 내려가게 됨
        return product;
    }

    // 설정 가격 변경
    @PutMapping("/api/products/{id}")
    public Long updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) throws SQLException {
        //클라이언트한테 받은 그값을 넘겨줘서 실제적으로 처리할 수 있게끔 매개변수처리
        //앞에 Product product = 는 updateProduct에서 product정보가 들어오면 반환하기 위한 것
        //리턴값에서 product.getID로 업데이트 된 상품의 아이디만 응답해줌
        Product product = productService.updateProduct(id,requestDto);
        // 응답 보내기 (업데이트된 상품 id)
        // 반환값이 id로 되어있는데, 이게 product여도 되는가?
        // 반환타입을 product으로 지정하고 반환값을 product로 했을때 전체 내용이 reponse됨
        return product.getId();
    }

    // 등록된 전체 상품 목록 조회
    @GetMapping("/api/products")
    public List<Product> getProducts() throws SQLException {
        List<Product> products = productService.getProducts();
        // 응답 보내기(DB연결해제하고 꼭 클라이언트와의 약속인. 응답을 보내줘야한다)
        return products;
    }
}
