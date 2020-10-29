package com.zlst.data.common;

/**
 *
 * @author Quentin Zhang
 */
public class ResponseHaveDataResultBuilder {

    public static <T> ResponseHaveDataResult<T> success(T t) {
        ResponseHaveDataResult<T> responseHaveDataResult = new ResponseHaveDataResult<>();
        responseHaveDataResult.setResultCode(SystemConstant.SUCCESS_CODE)
                .setResultMsg(SystemConstant.SUCCESS_MSG).setData(t);
        return responseHaveDataResult;
    }

    public static <T> ResponseHaveDataResult<T> fail(String failMsg) {
        ResponseHaveDataResult<T> responseHaveDataResult = new ResponseHaveDataResult<>();
        responseHaveDataResult.setResultCode(SystemConstant.FAIL_CODE)
                .setResultMsg(failMsg);
        return responseHaveDataResult;
    }

    public static <T> ResponseHaveDataResult<T> fail(String failMsg,T t) {
        ResponseHaveDataResult<T> responseHaveDataResult = new ResponseHaveDataResult<>();
        responseHaveDataResult.setResultCode(SystemConstant.FAIL_CODE)
                .setResultMsg(failMsg).setData(t);
        return responseHaveDataResult;
    }

    public static <T> ResponseHaveDataResult<T> fail(String failMsg,String resultCode) {
        ResponseHaveDataResult<T> responseHaveDataResult = new ResponseHaveDataResult<>();
        responseHaveDataResult.setResultCode(resultCode)
                .setResultMsg(failMsg);
        return responseHaveDataResult;
    }
}
