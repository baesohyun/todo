package com.inzent.todo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDto {
    private int memberNo;
    private String userId;
    private String name;
    private String rank;
    private int posNo;
    private String imgCode;
    private String email;
    private String phone;
    private String deptName;
}