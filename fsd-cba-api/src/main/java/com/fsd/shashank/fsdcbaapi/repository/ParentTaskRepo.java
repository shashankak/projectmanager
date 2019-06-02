package com.fsd.shashank.fsdcbaapi.repository;

import com.fsd.shashank.fsdcbaapi.entity.ParentTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentTaskRepo extends CrudRepository<ParentTask, Integer> {
    ParentTask findFirstByParentTask(String parentTask);
}
