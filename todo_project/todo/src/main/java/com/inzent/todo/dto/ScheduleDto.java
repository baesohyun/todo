package com.inzent.todo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScheduleDto {
    private String prjId;
    private String prjTitle;
    private String id;
    private String title;
    private String startDate;
    private String endDate;
    private String regDate;
    private String state;
    private boolean usePublic;
}