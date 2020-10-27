package com.zlst.data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlst.data.mapper.master.PubIllegalItemMapper;
import com.zlst.data.pojo.master.PubIllegalItem;
import com.zlst.data.service.PubIllegalItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: Quentin Zhang
 * @create: 2020-10-17 17:30
 **/
@Service
public class PubIllegalItemSerivceImpl extends ServiceImpl<PubIllegalItemMapper, PubIllegalItem> implements PubIllegalItemService {

    @Resource
    PubIllegalItemMapper pubIllegalItemMapper;

    @Override
    public String getIllegalItems(String illegalCode)
    {
        StringBuilder result = new StringBuilder();
        List<String> items = Arrays.asList(illegalCode.split(","));
        items.forEach(item -> {
            QueryWrapper<PubIllegalItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .and(obj1 -> obj1.eq(PubIllegalItem::getIllegalCode, item));
            List<PubIllegalItem> pubIllegalItemList = list(queryWrapper);
            result.append(pubIllegalItemList.get(0).getIllegalTitle()).append(":::").append(pubIllegalItemList.get(0).getItemContent()).append("###");
        });
        return result.toString();
    }

    @Override
    public String getIllegalSimpleItems(String illegalCode)
    {
        StringBuilder result = new StringBuilder();
        List<String> items = Arrays.asList(illegalCode.split(","));
        items.forEach(item -> {
            QueryWrapper<PubIllegalItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda()
                    .and(obj1 -> obj1.eq(PubIllegalItem::getIllegalCode, item));
            List<PubIllegalItem> pubIllegalItemList = list(queryWrapper);
            result.append(pubIllegalItemList.get(0).getItemContent()).append("###");
        });
        return result.toString();
    }
}
