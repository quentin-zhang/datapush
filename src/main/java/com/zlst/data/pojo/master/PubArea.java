package com.zlst.data.pojo.master;

import lombok.Data;

/**
 * @description: 区域编码
 * @author: Quentin Zhang
 * @create: 2020-10-29 17:26
 **/
@Data
public class PubArea {
    private long areaId;
    private String areaCode;
    private String areaName;
    private String parentAreaCode;
    private long areaLevel;
    private long state;
}
