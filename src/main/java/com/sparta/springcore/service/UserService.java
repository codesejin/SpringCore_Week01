package com.sparta.springcore.service;

import com.sparta.springcore.dto.SignupRequestDto;
import com.sparta.springcore.model.User;
import com.sparta.springcore.model.UserRoleEnum;
import com.sparta.springcore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    //무의미하고 긴 랜덤 스트링
    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    @Autowired//UserRepository를 DI받음
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //회원가입 처리 : 사용자가 등록 요청시 requestDto로 클라이언트가 보내준 값을 받아오고
    public void registerUser(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        // 회원 ID 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 ID 가 존재합니다.");
        }
        //이메일 가져오고
        String email = requestDto.getEmail();
        // 사용자 ROLE 확인 : 롤 가져옴
        // 사용자가 유저와 어드민으로 보내주는것이 아니라
        // 유저로 롤을 세팅해놓고나서
        UserRoleEnum role = UserRoleEnum.USER;
        // 어드민이 true면 (관리자로 요청)
        // 관리자로의 요청이라면 클라이언트로온 어드민 토큰과
        //미리 만들어놓은 16줄 토큰이 동일한지 체크
        if (requestDto.isAdmin()) {
            if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            //마지막에 롤에 어드민으로 이렇게 바꿔서 역할을 해줌
            role = UserRoleEnum.ADMIN;
        }
        //위의 과정이 문제없이 지나왔다면 db에 저장할 수 있게 유저 객체를 만들어줌
        //new 뒤에 User는 엔티티 객체였는데,  이걸 가지고
        //userRepository로 세이브해주면 db에 저장됨
        User user = new User(username, password, email, role);
        userRepository.save(user);
    }
}
