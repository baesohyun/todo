package com.inzent.todo.vo;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentVo {
    private int commentNo;
    private String content;
    private Date regDate;
    private String taskId;
    private int memberNo;
    // private int parentCno;
}