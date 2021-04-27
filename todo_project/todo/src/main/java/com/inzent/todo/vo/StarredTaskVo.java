package com.inzent.todo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StarredTaskVo {
    private int starId;
    private String taskId;
    private int memberNo;
}