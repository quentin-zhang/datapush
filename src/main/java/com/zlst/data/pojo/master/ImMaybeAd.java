package com.zlst.data.pojo.master;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
/**
 *
 * @author Quentin Zhang
 */
public class ImMaybeAd {
  private long maybeId;
  private long sampleId;
  private String adTitle;
  private int adType;
  private int siteId;
  private String mediaName;
  private int state;
  private String delReason;
  private String makeUser;
  private java.sql.Timestamp insertTime;
  private java.sql.Timestamp allotTime;
  private String liveVideoPath;
  private String liveRoomOwnerName;
  private java.sql.Timestamp liveStartTime;
  private java.sql.Timestamp liveEndTime;
  private String liveRoomOwnerRemark;
  private int liveAudienceCount;
  private String liveUserHeadPortraits;
  private String streamTitle;
  private String streamId;
  private String liveUserId;
  private String streamContentTag;
  private int followingCount;
  private int followerCount;
  private String city;
  private int goodsCount;
  private int watchCount;
  private long liveDuraTime;
  @TableField(exist=false)
  private int madeId;
}
