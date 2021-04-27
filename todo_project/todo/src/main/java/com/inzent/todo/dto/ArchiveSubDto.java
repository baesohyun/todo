package com.inzent.todo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ArchiveSubDto {
    private String taskId;
    private String title;
    private String superTaskId;
    private String superTitle;
    private String projectTitle;
    private boolean usePublic;
}