package com.inzent.todo.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProjectCardDto {
    private String id;
    private String title;
    private String state;
    private boolean usePublic;
    private String description;
    private String startDate;
    private String endDate;
    private Date regDate;
    private String imgNo;
    private int manager;
    private String userId;
    private String imgCode;
    private int memberCnt;
    private int progressRate;

    public boolean getUsePublic() {
        return usePublic;
    }
}