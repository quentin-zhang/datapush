package com.zlst.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlst.data.mapper.master.PubAreaMapper;
import com.zlst.data.pojo.master.AreaInfo;
import com.zlst.data.pojo.master.PubArea;
import com.zlst.data.service.PubAreaService;
import com.zlst.data.utils.BusinessUtil;
import org.springframework.stereotype.Component;

/**
 * @description: 区域编码实现类
 * @author: Quentin Zhang
 * @create: 2020-10-29 17:33
 **/
@Component
public class PubAreaServiceImpl extends ServiceImpl<PubAreaMapper, PubArea> implements PubAreaService {
    @Override
    public String getNameByAreaCode(String areaCode) {
        QueryWrapper<PubArea> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .and(obj1 -> obj1.eq(PubArea::getAreaCode, areaCode));
        PubArea pubArea = getOne(queryWrapper);
        return pubArea.getAreaName();
    }

    @Override
    public AreaInfo getAreaInfoByCode(String areaCode) {
        int intAreaCode = BusinessUtil.parseInt(areaCode,10);
        AreaInfo areaInfo = new AreaInfo();
        areaInfo.setProvinceName(getProvinceName(intAreaCode));
        areaInfo.setCityName(getCityName(intAreaCode));
        areaInfo.setCountryName(getCountryName(intAreaCode));
        return areaInfo;
    }

    /**
     * 获取省名称
     * @param code
     * @return
     */
    private String getProvinceName(int code) {
        if (code / 100 == 0) {
            code = code * 10000;
        } else {
            code = code / 10000 * 10000;
        }
        return getNameByAreaCode(String.valueOf(code));
    }

    /**
     * 获取市名称
     * @param code
     * @return
     */
    private String getCityName(int code) {
        if (code / 10000 == 0) {
            code = code * 100;
        } else {
            code = code / 100 * 100;
        }
        return getNameByAreaCode(String.valueOf(code));
    }

    /**
     * 获取县区名称
     * @param code
     * @return
     */
    public String getCountryName(int code) {
        if(code % 10000 != 100 )
        {
            return getNameByAreaCode(String.valueOf(code));
        }
        else
        {
            return "";
        }
    }
}
