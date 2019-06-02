package com.fsd.shashank.fsdcbaapi.repository;

import com.fsd.shashank.fsdcbaapi.entity.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface ProjectRepo extends CrudRepository<Project, Integer> {
}
