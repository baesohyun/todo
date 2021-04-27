package com.inzent.todo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CalDateDetailDto {
    private String prjId;
    private String prjTitle;
    private String pId;
    private String pTitle;
    private String cId;
    private String cTitle;
    private String pStartDate;
    private String pEndDate;
    private String pState;
    private boolean pUsePublic;
    private String cStartDate;
    private String cEndDate;
    private String cState;
    private boolean cUsePublic;
    private String managerName;
    private String imgCode;
}