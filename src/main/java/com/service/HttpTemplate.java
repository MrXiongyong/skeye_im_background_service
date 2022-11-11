package com.service;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

/**
 * @author lenovo
 * @Title: HttpTemplate
 * @Package com.skyeye.util
 * @Description: HttpTemplate
 * @date 2022/10/13 17:11
 */
public class HttpTemplate extends RestTemplate {
    private final HttpTemplateBuilder builder;

    public HttpTemplate(HttpTemplateBuilder builder) {
        this.builder = builder;
    }

    /**
     * APPLICATION/JSON 请求（有请求头）
     *
     * @param url           请求url
     * @param request       请求体
     * @param responseClass 响应类型
     * @return 结果
     */

    public <T> T postForJson(String url, Object request, Class<T> responseClass, HttpHeaders headers) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        return postForObject(url, entity, responseClass);
    }

    /**
     * APPLICATION/JSON 请求（无请求头）
     *
     * @param url           请求url
     * @param request       请求体
     * @param responseClass 响应类型
     * @return 结果
     */
    public <T> T postForJson(String url, Object request, Class<T> responseClass) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity(request, headers);
        return postForObject(url, entity, responseClass);
    }

    /**
     * APPLICATION/JSON 请求（无请求头）
     *
     * @param url                        请求url
     * @param request                    请求体
     * @param parameterizedTypeReference 响应类型
     * @return 结果
     */
    public <T> T postForJson(String url, Object request, ParameterizedTypeReference<T> parameterizedTypeReference) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        return exchange(url, HttpMethod.POST, entity, parameterizedTypeReference).getBody();
    }

    public <T> T get(String url, ParameterizedTypeReference<T> parameterizedTypeReference) {
        return exchange(url, HttpMethod.GET, null, parameterizedTypeReference).getBody();
    }


    /**
     * APPLICATION_FORM_URLENCODED 表单请求
     *
     * @param url           请求url
     * @param request       请求体
     * @param responseClass 响应类型
     * @return 结果
     */
    public <T> ResponseEntity<T> postForm(String url, MultiValueMap<Object, Object> request, Class<T> responseClass) {
        MultiValueMap<Object, Object> jsonParams = new LinkedMultiValueMap<>();
        jsonParams.addAll(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<Object, Object>> params = new HttpEntity<>(jsonParams, headers);
        return postForEntity(url, params, responseClass);
    }


    /**
     * 初始化
     */
    @PostConstruct
    private void init() {
        this.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        this.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient()));
    }

    /**
     * 内部httpClient
     *
     * @return httpClient
     */
    private HttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(builder.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(builder.getMaxPerRoute());
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(builder.getSocketTimeout())
                .setConnectTimeout(builder.getConnectTimeout())
                .setConnectionRequestTimeout(1000)
                .build();
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
    }

    public static HttpTemplateBuilder newBuilder() {
        return new HttpTemplateBuilder();
    }

    public static class HttpTemplateBuilder {

        /**
         * 连接池的总连接限制
         */
        private Integer maxTotal;

        /**
         * 每个HTTP路由的连接限制
         */
        private Integer maxPerRoute;

        /**
         * 读超时
         */
        private Integer socketTimeout;

        /**
         * 连接超时
         */
        private Integer connectTimeout;

        public HttpTemplateBuilder maxTotal(Integer maxTotal) {
            this.maxTotal = maxTotal;
            return this;
        }

        public HttpTemplateBuilder maxPerRoute(Integer maxPerRoute) {
            this.maxPerRoute = maxPerRoute;
            return this;
        }

        public HttpTemplateBuilder socketTimeout(Integer socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        public HttpTemplateBuilder connectTimeout(Integer connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Integer getMaxTotal() {
            return maxTotal;
        }

        public Integer getMaxPerRoute() {
            return maxPerRoute;
        }

        public Integer getSocketTimeout() {
            return socketTimeout;
        }

        public Integer getConnectTimeout() {
            return connectTimeout;
        }
    }


}
