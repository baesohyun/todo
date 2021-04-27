package com.inzent.todo.repository;

import java.util.HashMap;
import java.util.List;

import com.inzent.todo.dto.DeptDto;
import com.inzent.todo.dto.PwdDto;
import com.inzent.todo.dto.TokenDto;
import com.inzent.todo.dto.UserDto;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    @Autowired
    private SqlSession sqlSession;

    // public void selectDao() {
    // // System.out.println("Select DAO");

    // List<String> list = sqlSession.selectList("user.selectTest");
    // for (String s : list) {
    // // System.out.println(s);
    // }
    // }

    // 로그인, 토큰 발행을 위한 user 정보 추출
    // public UserVo selectUserIdPwd(UserVo user) {
    // return sqlSession.selectOne("user.selectUserIdPwd", user);
    // }
    public TokenDto selectUserIdPwd(UserDto user) {
        return sqlSession.selectOne("user.selectUserIdPwd", user);
    }

    public UserDto selectLoginUser(UserDto user) {
        return sqlSession.selectOne("user.selectLoginUser", user);
    }

    public UserDto selectLoginAdmin(UserDto user) {
        return sqlSession.selectOne("user.selectLoginAdmin", user);
    }

    public UserDto selectById(String id) {
        return sqlSession.selectOne("user.selectById", id);
    }

    public String selectId(PwdDto pwdDto) {
        return sqlSession.selectOne("user.selectId", pwdDto);
    }

    public List<DeptDto> selectDeptList() {
        return sqlSession.selectList("user.selectDeptList");
    }

    public List<UserDto> selectUserList(String[] deptList) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("deptList", deptList);
        return sqlSession.selectList("user.selectUserList", map);
    }

    public UserDto updateUser(UserDto user) {
        sqlSession.update("user.updateUserData", user);
        return sqlSession.selectOne("user.selectById", user.getId());
    }
}