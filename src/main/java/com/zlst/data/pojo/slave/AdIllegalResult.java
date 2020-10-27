package com.zlst.data.pojo.slave;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Quentin Zhang
 */
@Data
@TableName("ad_illegal_result")
public class AdIllegalResult {
  @JsonProperty("AD_ID")
  private long adId;
  @JsonProperty("SOURCE_ID")
  private long sourceId;
  @JsonProperty("AD_NAME")
  private String adName;
  @JsonProperty("AD_TYPE_ID")
  private long adTypeId;
  @JsonProperty("AD_ADDRESS")
  private String adAddress;
  @JsonProperty("BEGIN_TIME")
  private String beginTime;
  @JsonProperty("END_TIME")
  private String endTime;
  @JsonProperty("PLAY_DATE")
  private java.sql.Timestamp playDate;
  @JsonProperty("PLAY_LEAN")
  private String playLean;
  @JsonProperty("QUERY_AREA_LEVEL")
  private long queryAreaLevel;
  @JsonProperty("AREA_CODE")
  private String areaCode;
  @JsonProperty("IS_DEL")
  private long isDel;
  @JsonProperty("STATE")
  private long state;
  @JsonProperty("ILLEGAL_CODE")
  private String illegalCode;
  @JsonProperty("SITE_TYPE")
  private long siteType;
  @JsonProperty("AD_BEGIN_URL")
  private String adBeginUrl;
  @JsonProperty("AD_GROUND_URL")
  private String adGroundUrl;
  @JsonProperty("AD_BEGIN_SNAPSHOT")
  private String adBeginSnapshot;
  @JsonProperty("AD_GROUND_SNAPSHOT")
  private String adGroundSnapshot;
  @JsonProperty("INSERT_TIME")
  private java.sql.Timestamp insertTime;
  @JsonProperty("CUSTOMER_ID")
  private long customerId;
  @JsonProperty("MEDIA_ID")
  private long mediaId;
  @JsonProperty("IS_DISPOSE")
  private long isDispose;
  @JsonProperty("IS_VIEWED")
  private long isViewed;
  @JsonProperty("IS_SEND")
  private long isSend;
  @JsonProperty("SOURCE_TYPE")
  private long sourceType;
  @JsonProperty("IS_DISPLAY_ON_PLAT")
  private long isDisplayOnPlat;
  @JsonProperty("SOURCE_NAME")
  private String sourceName;
  @JsonProperty("AD_TYPE_NAME")
  private String adTypeName;
  @JsonProperty("PROOF_PATH")
  private String proofPath;
  @JsonProperty("SOURCE_TYPE_NAME")
  private String sourceTypeName;
  @JsonProperty("MEDIA_NAME")
  private String mediaName;
  @JsonProperty("PROVINCE_NAME")
  private String provinceName;
  @JsonProperty("CITY_NAME")
  private String cityName;
  @JsonProperty("COUNTY_NAME")
  private String countyName;
  @JsonProperty("OLD_ID")
  private long oldId;
  @JsonProperty("ILLEGAL_CONTENT")
  private String illegalContent;
  @JsonProperty("FTP_PATH")
  private String ftpPath;
  @JsonProperty("LONGITUDE")
  private String longitude;
  @JsonProperty("LATITUDE")
  private String latitude;
  @JsonProperty("ILLEGAL_LV")
  private long illegalLv;
  @JsonProperty("SON_TYPE_ID")
  private long sonTypeId;
  @JsonProperty("SON_TYPE_NAME")
  private String sonTypeName;
  @JsonProperty("DATAPUSH_ID")
  private String datapushId;

}
