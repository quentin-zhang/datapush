package com.zlst.data.controller;

import com.zlst.data.common.ResponseHaveDataResult;
import com.zlst.data.pojo.master.LiveRequest;
import com.zlst.data.service.MockDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @description: mock data generate
 * @author: Quentin Zhang
 * @create: 2020-10-14 11:04
 **/
@Slf4j
@Api(tags = "mock数据接口")
@RequestMapping("/live/mock")
@ResponseBody
@Controller
public class MockDataController {

    @Resource
    private MockDataService mockDataService;

    @PostMapping(value = "/push/immaybead")
    @ApiOperation(value = "Mysql造数据", notes = "Mysql造数据")
    public ResponseHaveDataResult<String> pushImMaybeAd(@RequestBody LiveRequest liveRequest) {
        return mockDataService.pushImMaybeAd(liveRequest);
    }
}
