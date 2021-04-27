package com.inzent.todo.repository;

import java.util.Map;

import com.inzent.todo.vo.MemberVo;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {
    @Autowired
    private SqlSession sqlSession;

    public int insertMember(MemberVo member) {
        // System.out.println("MemberDao - insertMember");
        sqlSession.insert("member.insertMember", member);
        return member.getMemberNo();
    }

    public void insertMembers(Map<String, Object> map) {
        sqlSession.insert("member.insertMembers", map);
    }

    public void insertManagers(Map<String, Object> map) {
        sqlSession.insert("member.insertManagers", map);
    }

    public void deleteManagers(Map<String, Object> map) {
        sqlSession.delete("member.deleteManagers", map);
    }

    public void deleteMembers(Map<String, Object> map) {
        sqlSession.delete("member.deleteMembers", map);
    }

}