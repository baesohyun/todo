package com.inzent.todo.repository;

import com.inzent.todo.dto.UserDto;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDao {

    @Autowired
    private SqlSession sqlSession;

    public String selectDetpCode(String deptName) {
        return sqlSession.selectOne("admin.selectDetpCode", deptName);
    }

    public int insertUser(UserDto user) {
        return sqlSession.insert("admin.insertUser", user);
    }

    public int modifyUser(UserDto user) {
        return sqlSession.update("admin.modifyUser", user);
    }

    public int deleteUser(String userId) {
        return sqlSession.delete("admin.deleteUser", userId);
    }
}