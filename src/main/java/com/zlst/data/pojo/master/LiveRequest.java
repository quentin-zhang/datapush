package com.zlst.data.pojo.master;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 直播列表查询参数
 * @author: Quentin Zhang
 * @create: 2020-10-13 14:18
 **/
@Data
public class LiveRequest {
    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "地域编号")
    private String areaCode;

    @ApiModelProperty(value = "直播ID")
    private String oldId;
}
