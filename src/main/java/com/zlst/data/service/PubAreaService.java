package com.zlst.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlst.data.pojo.master.AreaInfo;
import com.zlst.data.pojo.master.PubArea;

/**
 * @description: 区域编码接口
 * @author: Quentin Zhang
 * @create: 2020-10-29 17:31
 **/
public interface PubAreaService extends IService<PubArea> {
    public String getNameByAreaCode(String areaCode);
    public AreaInfo getAreaInfoByCode(String areaCode);
}
