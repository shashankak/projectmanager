package com.fsd.shashank.fsdcbaapi.entity;

import javax.persistence.*;

@Entity
@Table(name = "ParentTask")
public class ParentTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Parent_ID")
    private Integer parentId;

    @Column(name = "Parent_Task")
    private String parentTask;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentTask() {
        return parentTask;
    }

    public void setParentTask(String parentTask) {
        this.parentTask = parentTask;
    }
}
