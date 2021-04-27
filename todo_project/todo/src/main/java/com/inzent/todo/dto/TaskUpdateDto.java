package com.inzent.todo.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaskUpdateDto {
    private String taskId;
    private String title;
    private String description;
    private boolean usePublic;
    private String startDate;
    private String endDate;
    private char state;
    private String taskSuperId;
    private String labels;
    private MultipartFile[] attachFiles;
    private int[] addManager;
    private int[] subManager;
    private String managerString;
    private String projectId;

    public boolean getUsePublic() {
        return usePublic;
    }
}