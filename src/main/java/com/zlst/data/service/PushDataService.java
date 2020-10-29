package com.zlst.data.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zlst.data.common.ResponseHaveDataResult;
import com.zlst.data.pojo.master.ImMaybeAd;
import com.zlst.data.pojo.master.LiveRequest;
/**
 *
 * @author Quentin Zhang
 */
public interface PushDataService extends IService<ImMaybeAd> {
    /**
     * 推送直播数据
     * @param dataCountRequest
     * @return
     */
    ResponseHaveDataResult<String> pushImMaybeAd(LiveRequest dataCountRequest);

}
