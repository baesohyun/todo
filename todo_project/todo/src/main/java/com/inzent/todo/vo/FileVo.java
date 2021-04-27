package com.inzent.todo.vo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FileVo {
    private int fileNo;
    private String path;
    private String orgName;
    private String SaveName;
    private String ext;
    private int size;
    private String taskId;
    private Date regDate;
}