package com.inzent.todo.vo;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RestoreSuperVo {
    private String taskId;
    private String title;
    private String description;
    private boolean usePublic;
    private Date regDate;
    private String startDate;
    private String endDate;
    private String state;
    private int sortNo;
    private int progressRate;
    private int memberNo;
    private String projectId;
    private String labels;
}