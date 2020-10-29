package com.zlst.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlst.data.pojo.master.PubIllegalItem;

/**
 *
 * @author Quentin Zhang
 */
public interface PubIllegalItemService extends IService<PubIllegalItem> {
    /**
     * 获取违法项目
     * @param illegalCode
     * @return
     */
    public String getIllegalItems(String illegalCode);

    /**
     * 获取简单违法项目
     * @param illegalCode
     * @return
     */
    public String getIllegalSimpleItems(String illegalCode);
}
