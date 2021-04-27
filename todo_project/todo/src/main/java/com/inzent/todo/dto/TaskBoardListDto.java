package com.inzent.todo.dto;

import java.util.List;

import com.inzent.todo.vo.SuperTaskVo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaskBoardListDto {
    private TaskDto superTask;
    private List<TaskDto> subTaskList;
}