package com.inzent.todo.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberVo {
    private int memberNo;
    private String projectId;
    private String userId;
    private int posNo;
}