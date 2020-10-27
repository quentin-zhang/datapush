package com.zlst.data.common;

/**
 * 系统级常量
 *
 * @author dzt
 */
public class SystemConstant {

    /** 成功标识 */
    public static final String SUCCESS_CODE = "0";
    /** 成功返回信息 */
    public static final String SUCCESS_MSG = "SUCCESS";
    /** 失败标识*/
    public static final String FAIL_CODE = "1";
    /** token有误返回code标识 */
    public static final String TOKEN_INVALID_CODE = "98";
    /** token有误返回消息 */
    public static final String TOKEN_INVALID__MSG = "token无效";
    /** 系统异常标识 */
    public static final String EXCEPTION_CODE = "99";
    /** 系统异常返回信息 */
    public static final String EXCEPTION_MSG = "系统异常，请稍后重试！";
    /** token在HTTP请求头储存键 */
    public static final String REQUEST_HEADER_TOKER = "Auth-Token";
    /** 成功失败标识key */
    public static final String RETURN_CODE_KEY = "resultCode";
    /** 返回信息 */
    public static final String RETURN_MSG_KEY = "resultMsg";
    /**用户id<=user_admin_id 为系统管理员之类的用户*/
    public static final int USER_ADMIN_ID = 2;




}
