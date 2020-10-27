package com.zlst.data.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zlst.data.common.ResponseHaveDataResult;
import com.zlst.data.pojo.master.Addata;
import com.zlst.data.pojo.master.ImMaybeAd;
import com.zlst.data.pojo.master.LiveRequest;

import java.util.List;

public interface MockDataService extends IService<ImMaybeAd> {
    ResponseHaveDataResult<String> productLiveMock(LiveRequest dataCountRequest);
    ResponseHaveDataResult<String> pushImMaybeAd(LiveRequest dataCountRequest);
    List<Addata> findAddata();
}
