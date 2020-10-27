package com.zlst.data.pojo.master;

import lombok.Data;

@Data
public class PubIllegalItem {

  private long illegalId;
  private String illegalCode;
  private String itemContent;
  private String illegalTitle;
  private String illegalLevel;
  private long themeId;
  private long typeId;
  private java.sql.Timestamp createTime;
  private java.sql.Timestamp updateTime;
  private long sortNum;

}
