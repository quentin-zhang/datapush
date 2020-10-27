package com.zlst.data.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * 存在返回数据返回
 * @param <T> 数据
 * @author dzt
 */
@ApiModel(description = "通用响应返回对象")
public class ResponseHaveDataResult<T> {

    @ApiModelProperty(value = "成功失败标识，0成功，1失败")
    private String resultCode;

    @ApiModelProperty(value = "失败时返回描述信息")
    private String resultMsg;

    @ApiModelProperty(value = "业务数据")
    private T data;

    public String getResultCode() {
        return resultCode;
    }

    public ResponseHaveDataResult<T> setResultCode(String resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public ResponseHaveDataResult<T> setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResponseHaveDataResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public boolean success(){
        return StringUtils.equals(this.resultCode, "0");
    }
}
