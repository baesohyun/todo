package com.inzent.todo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ArchiveSuperDto {
    private String taskId;
    private String title;
    private String projectTitle;
    private boolean usePublic;
}