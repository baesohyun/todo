package com.inzent.todo.service;

import java.util.List;

import com.inzent.todo.dto.DeptDto;
import com.inzent.todo.dto.PwdDto;
import com.inzent.todo.dto.TokenDto;
import com.inzent.todo.dto.UserDto;
import com.inzent.todo.repository.UserDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    // 로그인, 토큰 발행을 위한 user 정보 추출
    // public UserVo getUserToken(UserVo user) {
    // UserVo userToken = userDao.selectUserIdPwd(user);
    // return userToken;
    // }
    // public UserVo getUserToken(UserVo user) {
    // UserVo userToken = userDao.selectUserIdPwd(user);
    // return userToken;
    // }
    public TokenDto getUserToken(UserDto user) {
        return userDao.selectUserIdPwd(user);
    }

    public UserDto getLoginUser(UserDto user) {
        return userDao.selectLoginUser(user);
    }

    public UserDto getLoginAdmin(UserDto user) {
        return userDao.selectLoginAdmin(user);
    }

    public UserDto getById(String id) {
        if (id.equals("admin"))
            return null;
        else
            return userDao.selectById(id);
    }

    public boolean pwdCheck(PwdDto pwdDto) {
        String password = userDao.selectId(pwdDto);

        if (password.equals(pwdDto.getPassword()))
            return true;
        else
            return false;
    }

    public List<DeptDto> getDeptList() {
        return userDao.selectDeptList();
    }

    public List<UserDto> getUserList(String[] deptList) {
        return userDao.selectUserList(deptList);
    }

    public UserDto updateUser(UserDto user, String id) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setImgCode(user.getImgCode());
        dto.setName(user.getName());
        if (user.getPassword() != null)
            dto.setPassword(user.getPassword());
        dto.setBirth(user.getBirth());
        if (user.getGender().equals("남성"))
            dto.setGender("m");
        else if (user.getGender().equals("여성"))
            dto.setGender("f");
        else
            dto.setGender("");
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setRank(user.getRank());

        return userDao.updateUser(dto);
    }
}