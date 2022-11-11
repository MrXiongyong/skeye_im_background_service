package com.dao;

import com.model.ProjectModuleInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author lenovo
 * @Title: ProjectModuleInfoRespsitory
 * @Package com.dao
 * @Description: ProjectModuleInfoRespsitory
 * @date 2022/11/9 17:58
 */

public interface ProjectModuleInfoRepository extends MongoRepository<ProjectModuleInfo, String> {
}
