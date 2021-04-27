package com.inzent.todo.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SearchDao {

    @Autowired
    private SqlSession sqlSession;

    public List<String> selectAllList(Map<String, Object> map) {
        return sqlSession.selectList("search.selectAllList", map);
    }

    public List<String> selectProjectList(Map<String, Object> map) {
        return sqlSession.selectList("search.selectProjectList", map);
    }

    public List<String> selectTaskList(Map<String, Object> map) {
        return sqlSession.selectList("search.selectTaskList", map);
    }

    public List<String> selectManagerList(Map<String, Object> map) {
        return sqlSession.selectList("search.selectManagerList", map);
    }
}