package com.zlst.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlst.data.pojo.master.PubIllegalItem;

public interface PubIllegalItemService extends IService<PubIllegalItem> {
    public String getIllegalItems(String illegalCode);
    public String getIllegalSimpleItems(String illegalCode);
}
