package com.inzent.todo.vo;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProjectVo {
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
    private int progressRate;

    public boolean getUsePublic() {
        return usePublic;
    }

}