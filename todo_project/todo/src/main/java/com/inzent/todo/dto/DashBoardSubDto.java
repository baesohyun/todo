package com.inzent.todo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class DashBoardSubDto {
    private String cId;
    private String cTitle;
    private String startDate;
    private String endDate;
    private String state;
    private boolean usePublic;
    private String imgCode;
    private String managerName;
    private int managerCount;
}