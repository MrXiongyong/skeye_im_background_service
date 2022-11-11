package com.model;

import com.util.ConstantConfig;

/**
 * @author lenovo
 * @Title: RespData
 * @Package com.model
 * @Description: RespData
 * @date 2022/11/10 16:54
 */
public class RespData {

    private String code;
    private Object data;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public static RespData buildOk(Object data, String msg) {
        RespData respData = new RespData();
        respData.setCode(ConstantConfig.RESP_CODE_OK);
        respData.setData(data);
        respData.setMsg(msg);
        return respData;
    }

    public static RespData buildError(Object data, String msg) {
        RespData respData = new RespData();
        respData.setCode(ConstantConfig.RESP_CODE_ERROR);
        respData.setData(data);
        respData.setMsg(msg);
        return respData;
    }
}
