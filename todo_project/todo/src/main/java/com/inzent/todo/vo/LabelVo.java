package com.inzent.todo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LabelVo {
    private int labelNo;
    private String labelName;
    private String labelColor;
    private String projectId;
}