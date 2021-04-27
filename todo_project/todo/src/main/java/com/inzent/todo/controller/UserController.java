package com.inzent.todo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.inzent.todo.dto.DeptDto;
import com.inzent.todo.dto.PwdDto;
import com.inzent.todo.dto.TokenDto;
import com.inzent.todo.dto.UserDto;
// import com.inzent.todo.repository.UserDao;
import com.inzent.todo.security.Auth;
import com.inzent.todo.service.JwtService;
import com.inzent.todo.service.UserService;
import com.inzent.todo.vo.UserVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class UserController {

    // @Autowired
    // private UserDao userDao;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    // @GetMapping("/api/hello")
    // public String hello() {
    // // System.out.println("HELLO");
    // // System.out.println(userDao);
    // // userDao.selectDao();
    // // System.out.println("db 성공!");

    // return "hello";
    // }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto user) {

        String token = null;

        // 들어온 로그인 정보(ID, PWD)로 DB에서 조회
        // UserVo userToken = userService.getUserToken(user);
        TokenDto userToken = userService.getUserToken(user);

        Map<String, Object> map = new HashMap<String, Object>();

        // 유저 정보가 있다면 token 생성
        if (userToken != null) {
            token = jwtService.createLoginToken(userToken); // 토큰 발급
            map.put("accessToken", token);
            // System.out.println(token);

            if (userToken.getUserType().equals("u")) {
                // 일반 유저
                UserDto loginUser = userService.getLoginUser(user); // 유저 정보 추출
                loginUser.setPassword(null);
                map.put("loginUser", loginUser);
            } else {
                // 관리자
                UserDto loginUser = userService.getLoginAdmin(user);
                loginUser.setPassword(null);
                map.put("loginUser", loginUser);
            }
        }

        return token != null ? new ResponseEntity<Object>(map, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @Auth
    @PostMapping("/loginByToken")
    public UserDto login(HttpServletRequest req, @RequestBody String accessToken) {
        // System.out.println("토큰이 이미 발급된 유저로그인");
        UserDto user = userService.getById(((UserVo) req.getAttribute("user")).getId());
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }

    @PostMapping("/pwdCheck")
    public boolean pwdCheck(@RequestBody PwdDto pwdDto) {
        return userService.pwdCheck(pwdDto);
    }

    @GetMapping("/deptList")
    public List<DeptDto> getDeptList() {
        return userService.getDeptList();
    }

    @PostMapping("/userList")
    public List<UserDto> getUserList(@RequestBody String[] deptList) {
        return userService.getUserList(deptList);
    }

    @Auth
    @PostMapping("/updateUser")
    public UserDto updateUser(HttpServletRequest req, @RequestBody UserDto user) {
        UserVo u = (UserVo) req.getAttribute("user");
        return userService.updateUser(user, u.getId());
    }

}