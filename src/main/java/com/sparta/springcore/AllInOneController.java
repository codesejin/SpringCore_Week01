package com.sparta.springcore;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor // final로 선언된 멤버 변수를 자동으로 생성합니다.
@RestController // JSON으로 데이터를 주고받음을 선언합니다.
public class AllInOneController {

    // 신규 상품 등록
    @PostMapping("/api/products")
    public Product createProduct(@RequestBody ProductRequestDto requestDto) throws SQLException {
// 요청받은 DTO 로 DB에 저장할 객체 만들기
        Product product = new Product(requestDto);

// DB 연결
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:springcoredb", "sa", "");

// DB Query 작성 /// id가 자동으로 계속 증가하니까 마지막 아이디는 max(id) => 불러오는 이유는 마지막으로 저장했던 id 다음으로 또 새로 id를 저장할거기때문
        PreparedStatement ps = connection.prepareStatement("select max(id) as id from product");
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
// product id 설정 = product 테이블의 마지막 id + 1
            product.setId(rs.getLong("id") + 1);
        } else {
            throw new SQLException("product 테이블의 마지막 id 값을 찾아오지 못했습니다.");
        }
        // 가장 중요한 부분 : product테이블에 produxt정보를 넣고 있는거임
        // 물음표가 1,2,3,4,5,6으로 매치가 된다
        ps = connection.prepareStatement("insert into product(id, title, image, link, lprice, myprice) values(?, ?, ?, ?, ?, ?)");
        ps.setLong(1, product.getId());
        ps.setString(2, product.getTitle());
        ps.setString(3, product.getImage());
        ps.setString(4, product.getLink());
        ps.setInt(5, product.getLprice());
        ps.setInt(6, product.getMyprice());

// DB Query 실행
        ps.executeUpdate();

// DB 연결 해제
        ps.close();
        connection.close();

// 응답 보내기
        // 위에 과정은 request를 받아서 db에 저장한 과정
        // 아래 리턴은 클라이언트한테 응답하는 과정 -> json으로 내려가게 됨
        return product;
    }

    // 설정 가격 변경
    @PutMapping("/api/products/{id}")
    public Long updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) throws SQLException {
        Product product = new Product();

        if(requestDto.getMyprice()<=0){
            throw new RuntimeException("최저가를 0이상으로 입력해 주세요");
        }

// DB 연결
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:springcoredb", "sa", "");

// DB Query 작성
        PreparedStatement ps = connection.prepareStatement("select * from product where id = ?");
        ps.setLong(1, id);

// DB Query 실행 // DB정보들을 하나씩 채워주는 과정
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            product.setId(rs.getLong("id"));
            product.setImage(rs.getString("image"));
            product.setLink(rs.getString("link"));
            product.setLprice(rs.getInt("lprice"));
            product.setMyprice(rs.getInt("myprice"));
            product.setTitle(rs.getString("title"));
        } else {
            throw new NullPointerException("해당 아이디가 존재하지 않습니다.");
        }

// DB Query 작성  //db에서 아이디가 ?값인것을 물음표로 바꿔주세요 라는 의미
        ps = connection.prepareStatement("update product set myprice = ? where id = ?");
        // 물음표의 순서대로 1,2
        //requestDto부분이 사용자의 body에 왔던 그 정보
        ps.setInt(1, requestDto.getMyprice());
        //product.getId()는 db에서 가져온것
        // 사실은 클라이언트가 보내준 그 아이디 값을 가지고 db에서 가져온거라 같은 아이디값
        ps.setLong(2, product.getId());

// DB Query 실행
        ps.executeUpdate();

// DB 연결 해제
        rs.close();
        ps.close();
        connection.close();

// 응답 보내기 (업데이트된 상품 id)
// 반환값이 id로 되어있는데, 이게 product여도 되는가?
// 반환타입을 Long으로 지정하고 반환값을 product로 했을때 전체 내용이 reponse됨
        return product.getId();
    }

    // 등록된 전체 상품 목록 조회
    @GetMapping("/api/products")
    public List<Product> getProducts() throws SQLException {
        //DB에서 새로 모아진 관심상품을 담을 배열
        List<Product> products = new ArrayList<>();

// DB 연결
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:springcoredb", "sa", "");

// DB Query 작성 및 실행
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select * from product");

// DB Query 결과를 상품 객체 리스트로 변환
        //  while문을 통해 DB에서 상품 하나 당 한 줄 돌고 있음
        while (rs.next()) {
            Product product = new Product();
            product.setId(rs.getLong("id"));
            product.setImage(rs.getString("image"));
            product.setLink(rs.getString("link"));
            product.setLprice(rs.getInt("lprice"));
            product.setMyprice(rs.getInt("myprice"));
            product.setTitle(rs.getString("title"));
            //products는 위에서 새로 선언한 ArrayList 배열에 담아줌
            //쭈욱 가져온다음에 배열에 add하고~
            products.add(product);
        }

// DB 연결 해제
        rs.close();
        connection.close();

// 응답 보내기(DB연결해제하고 꼭 클라이언트와의 약속인. 응답을 보내줘야한다)
        return products;
    }
}
