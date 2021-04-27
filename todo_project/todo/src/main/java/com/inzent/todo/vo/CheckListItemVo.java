package com.inzent.todo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CheckListItemVo {
    private int itemNo;
    private String title;
    private boolean isChecked;
    private int listNo;
    private int memberNo;
}