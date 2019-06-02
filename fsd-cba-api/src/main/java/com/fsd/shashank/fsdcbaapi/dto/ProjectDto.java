package com.fsd.shashank.fsdcbaapi.dto;

import lombok.Data;

@Data
public class ProjectDto {
    private Integer projectId;
    private String project;
    private String startDate;
    private String endDate;
    private Boolean startDateIsEndDate;
    private Integer priority;
    private UserDto manager;
    private Boolean completed;
    private Integer noOfTasks;
    private Integer noOfCompletedTasks;

    public ProjectDto() {

    }

    public ProjectDto(Integer projectId) {
        this.projectId = projectId;
    }

    public ProjectDto(Integer projectId, String project, String startDate, String endDate, Boolean startDateIsEndDate, Integer priority, UserDto manager) {
        this.projectId = projectId;
        this.project = project;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startDateIsEndDate = startDateIsEndDate;
        this.priority = priority;
        this.manager = manager;
    }
}
