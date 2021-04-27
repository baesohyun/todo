package com.inzent.todo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CalFilterItemDto {
    private String[] prjData;
    private String[] memData;
    private String useData;
    private String id;
}