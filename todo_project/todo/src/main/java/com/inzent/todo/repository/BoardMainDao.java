package com.inzent.todo.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.inzent.todo.dto.DashBoardSubDto;
import com.inzent.todo.dto.DashBoardSuperDto;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BoardMainDao {

    @Autowired
    private SqlSession sqlSession;

    public List<DashBoardSuperDto> getTodaySuperList(String userId) {
        // System.out.println("BoardMain Dao : get TodayList");

        return sqlSession.selectList("dashboard.getTodaySuperList", userId);
    }

    public List<DashBoardSubDto> getTodaySubList(String todaySub, String userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("todaySub", todaySub);
        map.put("userId", userId);
        return sqlSession.selectList("dashboard.getTodaySubList", map);
    }

    public List<DashBoardSuperDto> getWeekSuperList(String userId) {
        // System.out.println("BoardMain Dao : get WeekList");

        return sqlSession.selectList("dashboard.getWeekSuperList", userId);
    }

    public List<DashBoardSubDto> getWeekSubList(String weekSub, String userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("weekSub", weekSub);
        map.put("userId", userId);
        // System.out.println("여기여기...." + weekSub);
        return sqlSession.selectList("dashboard.getWeekSubList", map);
    }

    public List<DashBoardSuperDto> getStarredList(String userId) {
        return sqlSession.selectList("dashboard.getStarredList", userId);
    }

    public int deleteStarred(int starId) {
        return sqlSession.delete("dashboard.deleteStarred", starId);
    }

}