package com.model;

import com.util.ConstantConfig;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * @author lenovo
 * @Title: ProjectModuleInfo
 * @Package com.model
 * @Description: ProjectModuleInfo
 * @date 2022/11/9 17:49
 */
@Document(collection = "project_module_info")
public class ProjectModuleInfo {

    @Field(ConstantConfig.PROJECT_ID)
    private String projectId;

    @Field(ConstantConfig.PROJECT_NAME)
    private String projectName;

    @Field(ConstantConfig.TENANT_CODE)
    private String tenantCode;

    @Field(ConstantConfig.TENANT_NAME)
    private String tenantName;

    @Field(ConstantConfig.MODULE_CODE)
    private String moduleCode;

    @Field(ConstantConfig.MODULE_NAME)
    private String moduleName;

    @Field(ConstantConfig.DOMAIN)
    private String domain;

    @Field(ConstantConfig.DD)
    private String dd;

    @Field(ConstantConfig.LAYER_CODE)
    private String layerCode;

    @Field(ConstantConfig.PROJECT_LEVEL)
    private String projectLevel;

    @Field(ConstantConfig.LABEL_INFO)
    private String labelInfo;

    /**
     * 集中:1 省分:0
     */
    @Field(ConstantConfig.PROJECT_TYPE)
    private String projectType;

    @Field(ConstantConfig.UPDATE_TIME)
    private LocalDateTime updateTime;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDd() {
        return dd;
    }

    public void setDd(String dd) {
        this.dd = dd;
    }

    public String getLayerCode() {
        return layerCode;
    }

    public void setLayerCode(String layerCode) {
        this.layerCode = layerCode;
    }

    public String getProjectLevel() {
        return projectLevel;
    }

    public void setProjectLevel(String projectLevel) {
        this.projectLevel = projectLevel;
    }

    public String getLabelInfo() {
        return labelInfo;
    }

    public void setLabelInfo(String labelInfo) {
        this.labelInfo = labelInfo;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
