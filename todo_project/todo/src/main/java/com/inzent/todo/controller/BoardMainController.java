package com.inzent.todo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.inzent.todo.dto.DashBoardSubDto;
import com.inzent.todo.dto.DashBoardSuperDto;
import com.inzent.todo.security.Auth;
import com.inzent.todo.service.BoardMainService;
import com.inzent.todo.vo.UserVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class BoardMainController {

    @Autowired
    private BoardMainService boardMainService;

    // 오늘
    @Auth
    @GetMapping("/today")
    public List<DashBoardSuperDto> getTodaySuperList(HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();
        List<DashBoardSuperDto> list = boardMainService.getTodaySuperList(userId);
        return list;
    }

    @Auth
    @PostMapping("/todaysub")
    public List<DashBoardSubDto> getTodaySubList(@RequestBody String todaySub, HttpServletRequest req) {
        int length = todaySub.length();
        todaySub = todaySub.substring(0, length - 1);
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();
        List<DashBoardSubDto> list = boardMainService.getTodaySubList(todaySub, userId);
        return list;
    }

    // 주간
    @Auth
    @GetMapping("/week")
    public List<DashBoardSuperDto> getWeekSuperList(HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();

        List<DashBoardSuperDto> list = boardMainService.getWeekSuperList(userId);

        return list;
    }

    @Auth
    @PostMapping("/weeksub")
    public List<DashBoardSubDto> getWeekSubList(@RequestBody String weekSub, HttpServletRequest req) {
        int length = weekSub.length();
        weekSub = weekSub.substring(0, length - 1);
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();
        // System.out.println(weekSub);
        List<DashBoardSubDto> list = boardMainService.getWeekSubList(weekSub, userId);
        return list;

    }

    // 관심
    @Auth
    @GetMapping("/starred")
    public List<DashBoardSuperDto> getStarredList(HttpServletRequest req) {
        UserVo user = (UserVo) req.getAttribute("user");
        String userId = user.getId();

        List<DashBoardSuperDto> list = boardMainService.getStarredList(userId);

        return list;
    }

    @GetMapping("deleteStarred/{starId}")
    public void deleteStarred(@PathVariable("starId") Integer starId) {
        int cnt = boardMainService.deleteStarred(starId);
        if (cnt == 1)
            System.out.println("성공!");
    }

}