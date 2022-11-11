package com.service;

import com.model.ProjectModuleInfo;

import java.util.List;

/**
 * @author lenovo
 * @Title: DataService
 * @Package com.service
 * @Description: DataService
 * @date 2022/11/9 16:51
 */
public interface DataService {

    /**
     *processingProjectModuleData
     */
    void processingProjectModuleData();

    List<ProjectModuleInfo> getAllModules();
}
