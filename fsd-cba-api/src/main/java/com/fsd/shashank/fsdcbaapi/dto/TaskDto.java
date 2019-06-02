package com.fsd.shashank.fsdcbaapi.dto;


import lombok.Data;

@Data
public class TaskDto {
    private Integer taskId;
    private ProjectDto projectDto;
    private String task;
    private Boolean thisIsParent;
    private Integer priority;
    private TaskDto parentTask;
    private String startDate;
    private String endDate;
    private UserDto userDto;
    private String status;

    public TaskDto() {

    }

    public TaskDto(Integer taskId, String task) {
        this.taskId = taskId;
        this.task = task;
    }

    public TaskDto(Integer taskId, ProjectDto projectDto, String task, Boolean thisIsParent, Integer priority, TaskDto parentTask, String startDate, String endDate, UserDto userDto, String status) {
        this.taskId = taskId;
        this.projectDto = projectDto;
        this.task = task;
        this.thisIsParent = thisIsParent;
        this.priority = priority;
        this.parentTask = parentTask;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userDto = userDto;
        this.status = status;
    }
}
