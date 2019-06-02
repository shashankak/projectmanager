package com.fsd.shashank.fsdcbaapi.repository;

import com.fsd.shashank.fsdcbaapi.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends CrudRepository<Task, Integer> {
}
