package com.inzent.todo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DashBoardSuperDto {

    private String prjId;
    private String pId;
    private String pTitle;
    private String prjTitle;
    private String startDate;
    private String endDate;
    private String regDate;
    private String state;
    private boolean usePublic;
    private String dueDate;
    private String progressRate;
    private int starId;
}