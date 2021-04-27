package com.inzent.todo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FilterDto {
    private String prjId;
    private String prjTitle;
    private String userId;
    private String userName;
}