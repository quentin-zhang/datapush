package com.zlst.data.pojo.master;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.lucene.document.FieldType;

import java.util.Map;

/**
 * @description: mock data entity
 * @author: Quentin Zhang
 * @create: 2020-10-14 13:06
 **/
@Data
public class MockData {
    @JsonProperty("AD_ADDRESS")
    private String AD_ADDRESS = "";
    @JsonProperty("BEGIN_TIME")
    private String BEGIN_TIME = "0";
    @JsonProperty("CITY_NAME")
    private String CITY_NAME = "";
    @JsonProperty("CUSTOMER_ID")
    private String CUSTOMER_ID = "";
    @JsonProperty("END_TIME")
    private String END_TIME = "";
    @JsonProperty("FTP_PATH")
    private String FTP_PATH = "";
    @JsonProperty("INSERT_TIME")
    private String INSERT_TIME = "";
    @JsonProperty("IS_DEL")
    private int IS_DEL = 0;
    @JsonProperty("IS_DISPLAY_ON_PLAT")
    private String IS_DISPLAY_ON_PLAT = "";
    @JsonProperty("MEDIA_ID")
    private String MEDIA_ID = "";
    @JsonProperty("MEDIA_NAME")
    private String MEDIA_NAME = "";
    @JsonProperty("OLD_ID")
    private String OLD_ID = "";
    @JsonProperty("PLAY_DATE")
    private String PLAY_DATE = "";
    @JsonProperty("PLAY_LEAN")
    private String PLAY_LEAN = "";
    @JsonProperty("PROVINCE_NAME")
    private String PROVINCE_NAME = "";
    @JsonProperty("SITE_TYPE")
    private String SITE_TYPE = "";
    @JsonProperty("SOURCE_ID")
    private String SOURCE_ID = "";
    @JsonProperty("SOURCE_NAME")
    private String SOURCE_NAME = "";
    @JsonProperty("STATE")
    private String STATE = "";

    @JsonProperty("GOODS_COUNT")
    private int GOODS_COUNT = 0;
    @JsonProperty("VIEWER_COUNT")
    private int VIEWER_COUNT = 0;
    @JsonProperty("WATCH_COUNT")
    private int WATCH_COUNT = 0;
    @JsonProperty("LIVE_USER_HEAD_PORTRAITS")
    private String LIVE_USER_HEAD_PORTRAITS = "0";

    @JsonProperty("LIVE_ROOM_OWNER_REMARK")
    private String LIVE_ROOM_OWNER_REMARK = "";
    @JsonProperty("STREAM_TITLE")
    private String STREAM_TITLE = "";
    @JsonProperty("STREAM_ID")
    private String STREAM_ID = "";
    @JsonProperty("LIVE_USER_ID")
    private String LIVE_USER_ID = "";
    @JsonProperty("STREAM_CONTENT_TAG")
    private String STREAM_CONTENT_TAG = "";
    @JsonProperty("FOLLOWING_COUNT")
    private int FOLLOWING_COUNT = 0;
    @JsonProperty("FOLLOWER_COUNT")
    private int FOLLOWER_COUNT = 0;

}
