package com.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.configuration.ProjectConfiguration;
import com.dao.ProjectModuleInfoRepository;
import com.model.ProjectModuleInfo;
import com.task.DataTask;
import com.util.ConstantConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.alibaba.fastjson.JSON.toJSONString;

/**
 * @author lenovo
 * @Title: DateServiceImpl
 * @Package com.service
 * @Description: DateServiceImpl
 * @date 2022/11/9 16:53
 */
@Service
public class DateServiceImpl implements DataService{

    private Logger logger = LoggerFactory.getLogger(DataTask.class);

    @Resource
    private HttpTemplate httpTemplate;

    @Resource
    MongoTemplate mongoTemplate;

    @Resource
    private ProjectModuleInfoRepository projectModuleInfoRepository;

    @Resource
    ProjectConfiguration projectConfiguration;

    @Override
    @Async("asyncServiceExecutor")
    public void processingProjectModuleData() {
        JSONArray mpArray = getWkArray();
        List<ProjectModuleInfo> toMongoData = parseData(mpArray);
        addDataToMongo(toMongoData);
    }

    @Override
    public List<ProjectModuleInfo> getAllModules() {
        List<ProjectModuleInfo> result = projectModuleInfoRepository.findAll();
        if (result.isEmpty()) {
            result = parseData(getWkArray());
        }
        return result;
    }

    private JSONArray getWkArray() {
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>(){};
        String wkResp = httpTemplate.get(projectConfiguration.getWbUrl(),responseType);
        JSONObject wkJsonObj = parseObject(wkResp);
        return wkJsonObj.getJSONArray("obj");
    }

    private List<ProjectModuleInfo> parseData(JSONArray data) {
        List<ProjectModuleInfo> projectModuleInfos = new ArrayList<>();

        if (data == null || data.isEmpty()) {
            return projectModuleInfos;
        }

        data.forEach(project -> {
            LocalDateTime updateTime = LocalDateTime.now();
            JSONObject projectInfo = (JSONObject) project;
            JSONArray modules = projectInfo.getJSONArray(ConstantConfig.MODULES_W);
            if (modules != null && !modules.isEmpty()) {
                String projectId = projectInfo.getString(ConstantConfig.PROJECT_ID_W);
                String projectName = projectInfo.getString(ConstantConfig.PROJECT_NAME_W);
                JSONArray labels = projectInfo.getJSONArray(ConstantConfig.LABELS_W);
                String labelInfoStr = "";
                AtomicReference<String> domain = new AtomicReference<>("");
                AtomicReference<String> dd = new AtomicReference<>("");
                AtomicReference<String> layerCode = new AtomicReference<>("");
                AtomicReference<String> projectLevel = new AtomicReference<>("");
                AtomicReference<String> projectType = new AtomicReference<>("");
                if (labels != null && !labels.isEmpty()) {
                    labels.forEach(label -> {
                        JSONObject labelItem = (JSONObject)label;
                        String labelName = labelItem.getString(ConstantConfig.ATTR_TYPE_W);
                        String labelValue = labelItem.getString(ConstantConfig.ATTR_VALUE_W);
                        if (ConstantConfig.DOMAIN.equalsIgnoreCase(labelName)) {
                            domain.set(labelValue);
                        } else if (ConstantConfig.DD.equalsIgnoreCase(labelName)) {
                            dd.set(labelValue);
                        } else if (ConstantConfig.LAYER_CODE.equalsIgnoreCase(labelName)) {
                            layerCode.set(labelValue);
                        } else if (ConstantConfig.PROJECT_LEVEL.equalsIgnoreCase(labelName)) {
                            projectLevel.set(labelValue);
                        } else if (ConstantConfig.PROJECT_TYPE_W.equalsIgnoreCase(labelName)) {
                            projectType.set(labelValue);
                        }
                    });
                }
                if (labels != null && !labels.isEmpty()) {
                    labels.removeIf(label ->{
                        JSONObject labelItem = (JSONObject)label;
                        String labelName = labelItem.getString(ConstantConfig.ATTR_TYPE_W);
                        return ConstantConfig.DOMAIN.equalsIgnoreCase(labelName) ||
                                ConstantConfig.DD.equalsIgnoreCase(labelName) ||
                                ConstantConfig.LAYER_CODE.equalsIgnoreCase(labelName) ||
                                ConstantConfig.PROJECT_LEVEL.equalsIgnoreCase(labelName) ||
                                ConstantConfig.PROJECT_TYPE_W.equalsIgnoreCase(labelName);
                    });
                    labelInfoStr = toJSONString(labels);
                }
                String finalLabelInfoStr = labelInfoStr;
                modules.forEach(module -> {
                    JSONObject moduleInfo = (JSONObject) module;
                    ProjectModuleInfo projectModuleInfo = new ProjectModuleInfo();
                    projectModuleInfo.setProjectId(projectId);
                    projectModuleInfo.setProjectName(projectName);
                    projectModuleInfo.setTenantCode(moduleInfo.getString(ConstantConfig.TENANT_CODE_W));
                    projectModuleInfo.setTenantName(moduleInfo.getString(ConstantConfig.TENANT_NAME_W));
                    projectModuleInfo.setModuleCode(moduleInfo.getString(ConstantConfig.MODULE_CODE_W));
                    projectModuleInfo.setModuleName(moduleInfo.getString(ConstantConfig.MODULE_NAME_W));
                    //设置标签信息
                    projectModuleInfo.setDomain(domain.get());
                    projectModuleInfo.setDd(dd.get());
                    projectModuleInfo.setLayerCode(layerCode.get());
                    projectModuleInfo.setProjectLevel(projectLevel.get());
                    projectModuleInfo.setProjectType(projectType.get());
                    projectModuleInfo.setLabelInfo(finalLabelInfoStr);
                    projectModuleInfo.setUpdateTime(updateTime);
                    projectModuleInfos.add(projectModuleInfo);
                });
            }
        });
        return projectModuleInfos;
    }

    private void addDataToMongo(List<ProjectModuleInfo> projectModuleInfos) {
        logger.info("开始将数据插入mongo，数据条数:{}", projectModuleInfos.size());
        projectModuleInfoRepository.deleteAll();
        projectModuleInfoRepository.insert(projectModuleInfos);
        logger.info("将数据插入mongo成功");
    }

}
