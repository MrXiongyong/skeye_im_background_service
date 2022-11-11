package com.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lenovo
 * @Title: ProjectConfiguration
 * @Package com.configuration
 * @Description: ProjectConfiguration
 * @date 2022/11/10 23:46
 */
@Component
@ConfigurationProperties(prefix = "params")
public class ProjectConfiguration {

    private String wbUrl;

    public void setWbUrl(String wbUrl) {
        this.wbUrl = wbUrl;
    }

    public String getWbUrl() {
        return wbUrl;
    }

}
