package com.inzent.todo.controller;

import com.inzent.todo.dto.UserDto;
import com.inzent.todo.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/insertUser")
    public UserDto insertUser(@RequestBody UserDto user) {
        String deptCode = adminService.selectDetpCode(user.getDeptName());
        System.out.println("deptCode : " + deptCode);

        user.setDeptCode(deptCode);

        return adminService.insertUser(user);
    }

    @PostMapping("/modifyUser")
    public UserDto modifyUser(@RequestBody UserDto user) {
        // id, 이름, 직급, 부서
        String deptCode = adminService.selectDetpCode(user.getDeptName());
        System.out.println("deptCode : " + deptCode);
        user.setDeptCode(deptCode);

        return adminService.modifyUser(user);
    }

    @PostMapping("/deleteUser")
    public int deleteUser(@RequestBody String userId) {
        userId = userId.substring(0, userId.indexOf("="));
        System.out.println("userId : " + userId);
        return adminService.deleteUser(userId);
    }

}