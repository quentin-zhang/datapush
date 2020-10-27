package com.zlst.data.pojo.master;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 直播汇总
 * @author: Quentin Zhang
 * @create: 2020-10-13 10:18
 **/
@Data
public class LiveSummary {

    @ApiModelProperty(value = "主播名称")
    private String liveRoomOwnerName;

    @ApiModelProperty(value = "带货数量")
    private Integer withCount;

    @ApiModelProperty(value = "直播平台")
    private String mediaName;

    @ApiModelProperty(value = "观看人数")
    private Integer viewerCount;

    @ApiModelProperty(value = "观看次数")
    private Integer watchCount;

    @ApiModelProperty(value = "直播时长")
    private String liveDura;

    @ApiModelProperty(value = "违规条数")
    private Integer violationCount;
}
