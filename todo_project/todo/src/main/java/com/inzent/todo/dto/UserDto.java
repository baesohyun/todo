package com.inzent.todo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
    private String id;
    private String password;
    private String name;
    private String birth;
    private String gender;
    private String phone;
    private String email;
    private String userType;
    private String rank;
    private String imgCode;
    private String deptCode;
    private String deptName;
}