package com.inzent.todo.service;

import com.inzent.todo.dto.UserDto;
import com.inzent.todo.repository.AdminDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminDao adminDao;

    public String selectDetpCode(String deptName) {
        System.out.println("deptName: " + deptName);
        return adminDao.selectDetpCode(deptName);
    }

    public UserDto insertUser(UserDto user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setPassword("0000");
        dto.setName(user.getName());
        dto.setRank(user.getRank());
        dto.setDeptCode(user.getDeptCode());

        adminDao.insertUser(dto);

        return dto;
    }

    public UserDto modifyUser(UserDto user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setRank(user.getRank());
        dto.setDeptCode(user.getDeptCode());

        adminDao.modifyUser(dto);

        return dto;
    }

    public int deleteUser(String userId) {
        return adminDao.deleteUser(userId);
    }
}