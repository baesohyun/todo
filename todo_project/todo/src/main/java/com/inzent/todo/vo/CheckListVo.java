package com.inzent.todo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CheckListVo {
    private int listNo;
    private String title;
    private String taskId;
    private int progressRate;
    private int memberNo;
}
