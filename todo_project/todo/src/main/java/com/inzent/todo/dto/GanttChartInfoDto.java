package com.inzent.todo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GanttChartInfoDto {
    private int rowNumber;
    private String id;
    private String parentId;
    private String projectId;
    private String title;
    private String start;
    private String end;
    private String regDate;
    private long duration;
    private long progressRate;
    private String state;
    private String managerName;
    private String managerCount;
}