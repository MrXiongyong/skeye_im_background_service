package com.controller;

import com.model.ProjectModuleInfo;
import com.model.RespData;
import com.service.DataService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lenovo
 * @Title: ProjectModuleController
 * @Package com.controller
 * @Description: ProjectModuleController
 * @date 2022/11/9 15:53
 */
@RestController
public class ProjectModuleController {

    @Resource
    DataService dataService;
    @RequestMapping("/updateModules")
    @ResponseBody
    public RespData hello() {
        dataService.processingProjectModuleData();
        return RespData.buildOk(null, "数据处理成功");
    }

    @RequestMapping("/getAllModules")
    @ResponseBody
    public RespData getAllModules() {
        List<ProjectModuleInfo> projectModuleInfos = dataService.getAllModules();
        return RespData.buildOk(projectModuleInfos, "");
    }

}
