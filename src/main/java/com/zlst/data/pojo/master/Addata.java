package com.zlst.data.pojo.master;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: Quentin Zhang
 * @create: 2020-10-15 15:43
 **/
@Data
public class Addata {
    @ApiModelProperty(value = "广告名称")
    public String adName;
    @ApiModelProperty(value = "广告ID")
    public int madeId;
    @ApiModelProperty(value = "直播ID")
    public int maybeId;
    @ApiModelProperty(value = "广告起始时间")
    public java.sql.Time beginTime;
    @ApiModelProperty(value = "广告结束时间")
    public java.sql.Time endTime;
    @ApiModelProperty(value = "直播开始时间")
    public java.sql.Timestamp liveStartTime;
    @ApiModelProperty(value = "直播结束时间")
    public java.sql.Timestamp liveEndTime;
    @ApiModelProperty(value = "直播观看人数")
    public int liveAudienceCount;
    @ApiModelProperty(value = "直播视频路径")
    public String liveVideoPath;
    @ApiModelProperty(value = "直播平台编号")
    public int siteId;
    @ApiModelProperty(value = "直播平台名称")
    public String mediaName;
    @ApiModelProperty(value = "带货数量")
    public int goodsCount;
    @ApiModelProperty(value = "观看次数")
    public int watchCount;
    @ApiModelProperty(value = "直播时长")
    public long liveDuraTime;
    @ApiModelProperty(value = "直播房间主")
    public String liveRoomOwnerName;
    @ApiModelProperty(value = "广告类型ID")
    public long adTypeId;
    @ApiModelProperty(value = "广告类型名称")
    public String adTypeName;
    @ApiModelProperty(value = "广告大类型ID")
    public long adTypePid;
    @ApiModelProperty(value = "广告大类名称")
    public String adTypePname;
    @ApiModelProperty(value = "直播房主头像")
    public String liveUserHeadPortraits;
    @ApiModelProperty(value = "违法编码")
    public String illegalCode;
    @ApiModelProperty(value = "违法描述")
    public String illegalContent;
    @ApiModelProperty(value = "广告时长")
    public String dura;
}
