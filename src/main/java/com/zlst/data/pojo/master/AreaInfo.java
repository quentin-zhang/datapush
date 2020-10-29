package com.zlst.data.pojo.master;

import lombok.Data;

/**
 * @description: 区域信息
 * @author: Quentin Zhang
 * @create: 2020-10-29 17:39
 **/
@Data
public class AreaInfo {
    private String provinceName;
    private String cityName;
    private String countryName;
    private String provinceCode;
    private String cityCode;
    private String countryCode;
}
