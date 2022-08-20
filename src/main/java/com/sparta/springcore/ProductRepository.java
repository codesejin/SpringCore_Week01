package com.sparta.springcore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {


    public void createProduct(Product product) throws SQLException {

        // DB 연결 ( getConnection 에 빨간 밑줄 ? 예외 상황이 발생하는걸 해당함 수 위로 올려줘라
        // throw를 해서 넘겨줘라(show context actions)
        // 리포지토리에서 서비스로 , 서비스에서 컨트롤러로 에러 올려줌 (니가 처리해)
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
    }

    public Product getProduct(Long id) throws SQLException {
        Product product = new Product();
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
        }
        // DB 연결 해제
        ps.close();
        connection.close();

        return product;
    }

    public void updateMyprice(Long id, int myprice) throws SQLException {
        // DB 연결
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:springcoredb", "sa", "");

        // DB Query 작성  //db에서 아이디가 ?값인것을 물음표로 바꿔주세요 라는 의미
        PreparedStatement ps = connection.prepareStatement("update product set myprice = ? where id = ?");
        // 물음표의 순서대로 1,2
        //requestDto부분이 사용자의 body에 왔던 그 정보
        ps.setInt(1, myprice);
        //product.getId()는 db에서 가져온것
        // 사실은 클라이언트가 보내준 그 아이디 값을 가지고 db에서 가져온거라 같은 아이디값
        ps.setLong(2, id);

        // DB Query 실행
        ps.executeUpdate();

        // DB 연결 해제
        ps.close();
        connection.close();
    }

    public List<Product> getProducts() throws SQLException {
        //DB에서 새로 모아진 관심상품을 담을 배열
        List<Product> products = new ArrayList<>();
        // DB 연결
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:springcoredb", "sa", "");

        // DB Query 작성 및 실행
        Statement stmt = connection.createStatement();
        //product테이블의 전체를 가져올 것이다
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

        return products;
    }
}
