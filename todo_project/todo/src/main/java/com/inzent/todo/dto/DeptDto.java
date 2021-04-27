package com.inzent.todo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeptDto {
    private String id;
    private String deptCode;
    private String deptName;
    private String parentDeptCode;
}