package com.inzent.todo.service;

import java.util.List;

import com.inzent.todo.dto.DashBoardSubDto;
import com.inzent.todo.dto.DashBoardSuperDto;
import com.inzent.todo.repository.BoardMainDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardMainService {

    @Autowired
    private BoardMainDao boardMainDao;

    // 오늘 할일 목록
    public List<DashBoardSuperDto> getTodaySuperList(String userId) {
        // System.out.println("BoardMain Service : get TodayList");
        List<DashBoardSuperDto> list = boardMainDao.getTodaySuperList(userId);
        String dueDate = null;
        String rate = null;
        for (DashBoardSuperDto dto : list) {
            if (dto.getStartDate().isEmpty() || dto.getEndDate().isEmpty()) {
                dueDate = "미정";
            } else {
                dueDate = dto.getStartDate() + " ~ " + dto.getEndDate();
            }
            rate = dto.getProgressRate() + "%";
            dto.setProgressRate(rate);
            dto.setDueDate(dueDate);
        } // end else
        return list;
    }

    public List<DashBoardSubDto> getTodaySubList(String todaySub, String userId) {

        return boardMainDao.getTodaySubList(todaySub, userId);
    }

    // 주간 할일 목록
    public List<DashBoardSuperDto> getWeekSuperList(String userId) {
        // System.out.println("BoardMain Service : get WeekList");

        List<DashBoardSuperDto> list = boardMainDao.getWeekSuperList(userId);
        String dueDate = null;
        String rate = null;
        for (DashBoardSuperDto dto : list) {
            if (dto.getStartDate().isEmpty() || dto.getEndDate().isEmpty()) {
                dueDate = "미정";
            } else {
                dueDate = dto.getStartDate() + " ~ " + dto.getEndDate();
            }
            rate = dto.getProgressRate() + "%";
            dto.setProgressRate(rate);
            dto.setDueDate(dueDate);
        } // end else
        return list;
    }

    public List<DashBoardSubDto> getWeekSubList(String weekSub, String userId) {

        return boardMainDao.getWeekSubList(weekSub, userId);
    }

    // 관심 할일 목록
    public List<DashBoardSuperDto> getStarredList(String userId) {

        List<DashBoardSuperDto> list = boardMainDao.getStarredList(userId);
        String dueDate = null;
        String rate = null;
        for (DashBoardSuperDto dto : list) {
            if (dto.getStartDate().isEmpty() || dto.getEndDate().isEmpty()) {
                dueDate = "미정";
            } else {
                dueDate = dto.getStartDate() + " ~ " + dto.getEndDate();
            }
            rate = dto.getProgressRate() + "%";
            dto.setProgressRate(rate);
            dto.setDueDate(dueDate);
        } // end else
        return list;
    }

    public int deleteStarred(Integer starId) {
        return boardMainDao.deleteStarred(starId);
    }

}
