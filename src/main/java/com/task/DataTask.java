package com.task;

import com.configuration.ProjectConfiguration;
import com.service.DataService;
import com.util.ConstantConfig;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author lenovo
 * @Title: DataTask
 * @Package com.task
 * @Description: DataTask
 * @date 2022/11/11 10:36
 */
@Component
public class DataTask {

    private Logger logger = LoggerFactory.getLogger(DataTask.class);
    @Resource
    DataService dataService;
    @Resource
    ProjectConfiguration projectConfiguration;

    /**
     *更新系统租户模块信息
     */
    @Scheduled(cron = "${params.modulesCron}")
    @SchedulerLock(name = "updateModules", lockAtMostFor = 180 * 1000)
    public void updateModules() {

        logger.info("开始执行updateModules任务");
        dataService.processingProjectModuleData();
        logger.info("执行updateModules任务成功");
    }

}
