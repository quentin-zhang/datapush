package com.zlst.data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlst.data.common.ElasticsearchRestClient;
import com.zlst.data.common.ResponseHaveDataResult;
import com.zlst.data.common.ResponseHaveDataResultBuilder;
import com.zlst.data.mapper.master.ImMaybeAdMapper;
import com.zlst.data.mapper.master.MockDataDao;
import com.zlst.data.pojo.master.Addata;
import com.zlst.data.pojo.master.ImMaybeAd;
import com.zlst.data.pojo.master.LiveRequest;
import com.zlst.data.pojo.master.MockData;
import com.zlst.data.pojo.slave.AdIllegalResult;
import com.zlst.data.service.AdIllegalResultService;
import com.zlst.data.service.MockDataService;
import com.zlst.data.service.PubIllegalItemService;
import com.zlst.data.utils.DateUtil3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: mock data service impl
 * @author: Quentin Zhang
 * @create: 2020-10-14 11:09
 **/
@Slf4j
@Service
public class MockDataServiceImpl extends ServiceImpl<ImMaybeAdMapper, ImMaybeAd> implements MockDataService {
    @Resource
    ElasticsearchRestClient elasticsearchRestClient;
    @Resource
    MockDataDao mockDataDao;
    @Resource
    ImMaybeAdMapper imMaybeAdMapper;
    @Resource
    PubIllegalItemService pubIllegalItemService;
    @Resource
    AdIllegalResultService adIllegalResultService;

    private static final String PATTERN = "yyyy-MM-dd";
    private static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    DateTimeFormatter df = DateTimeFormatter.ofPattern(PATTERN);
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_DATE_TIME);

    @Override
    public ResponseHaveDataResult<String> pushImMaybeAd(LiveRequest dataCountRequest) {
//        QueryWrapper<ImMaybeAd> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda()
//                .and(obj1 -> obj1.ge(ImMaybeAd::getMaybeId, 23))
//                .or(obj2 -> obj2.le(ImMaybeAd::getMaybeId, 33));
//        List<ImMaybeAd> imMaybeAdList = list(queryWrapper);

        //直播表导入 MySQL --> ES
//        QueryWrapper<ImMaybeAd> queryWrapper = new QueryWrapper<>();
//        List<ImMaybeAd> imMaybeAdList = list(queryWrapper);
//        List<MockData> mockDataList = new ArrayList<MockData>();
//        for (ImMaybeAd imMaybeAd : imMaybeAdList)
//        {
//            mockDataList.add(liveESData(imMaybeAd));
//        }
//        mockDataDao.bulkInsert(mockDataList);
        //违法广告表 MySQL --> MySQL
        List<Addata> addataList = imMaybeAdMapper.findAd();
        List<AdIllegalResult> adIllegalResultList = new ArrayList<>();
        for (Addata addata : addataList)
        {
            adIllegalResultList.add(convertAdIllegalResult(addata));
        }
//        adIllegalResultService.saveBatch(adIllegalResultList);
        return ResponseHaveDataResultBuilder.success("导入成功");
    }



    @Override
    public List<Addata> findAddata() {
        return  imMaybeAdMapper.findAd();
    }

    private MockData liveEsData(ImMaybeAd imMaybeAd)
    {
        MockData mockData = new MockData();

        mockData.setAD_ADDRESS(imMaybeAd.getCity());
        mockData.setBEGIN_TIME(imMaybeAd.getLiveStartTime() != null ? imMaybeAd.getLiveStartTime().toLocalDateTime().format(dateTimeFormatter):"");
        mockData.setCITY_NAME(imMaybeAd.getCity());
        mockData.setCUSTOMER_ID("");
        mockData.setEND_TIME(imMaybeAd.getLiveEndTime() != null ? imMaybeAd.getLiveEndTime().toLocalDateTime().format(dateTimeFormatter) :"");
        mockData.setFTP_PATH(imMaybeAd.getLiveVideoPath());
        mockData.setINSERT_TIME(dateTimeFormatter.format(LocalDateTime.now()));
        mockData.setIS_DEL(0);
        mockData.setIS_DISPLAY_ON_PLAT("1");
        mockData.setMEDIA_ID(String.valueOf(imMaybeAd.getSiteId()));
        mockData.setMEDIA_NAME(imMaybeAd.getMediaName());
        mockData.setOLD_ID(String.valueOf(imMaybeAd.getMaybeId()));
        mockData.setPLAY_DATE(imMaybeAd.getLiveStartTime() != null ? imMaybeAd.getLiveStartTime().toLocalDateTime().format(df):"");
        mockData.setPLAY_LEAN(String.valueOf(imMaybeAd.getLiveDuraTime()));
        mockData.setPROVINCE_NAME(imMaybeAd.getCity());
        mockData.setSOURCE_ID(String.valueOf(imMaybeAd.getSiteId()));
        mockData.setSOURCE_NAME(imMaybeAd.getLiveRoomOwnerName());
        mockData.setSTATE("0");
        mockData.setGOODS_COUNT(imMaybeAd.getGoodsCount());
        mockData.setVIEWER_COUNT(imMaybeAd.getLiveAudienceCount());
        mockData.setWATCH_COUNT(imMaybeAd.getWatchCount());
        mockData.setLIVE_USER_HEAD_PORTRAITS(imMaybeAd.getLiveUserHeadPortraits());
        mockData.setLIVE_ROOM_OWNER_REMARK(imMaybeAd.getLiveRoomOwnerRemark());
        mockData.setSTREAM_TITLE(imMaybeAd.getStreamTitle());
        mockData.setSTREAM_ID(imMaybeAd.getStreamId());
        mockData.setLIVE_USER_ID(imMaybeAd.getLiveUserId());
        mockData.setSTREAM_CONTENT_TAG(imMaybeAd.getStreamContentTag());
        mockData.setFOLLOWING_COUNT(imMaybeAd.getFollowingCount());
        mockData.setFOLLOWER_COUNT(imMaybeAd.getFollowerCount());

        return mockData;
    }

    private MockData mysqlData(Addata addata)
    {
        MockData mockData = new MockData();
//        Random random = new Random();
//        int randomIndex = random.nextInt(LiveConstant.AD_ADDRESS_ARRAY.length);
//        int idIndex = random.nextInt(LiveConstant.ID_INDEX);
//        int viewerCount = random.nextInt(LiveConstant.COUNT_INDEX);
//        mockData.setAD_ADDRESS(LiveConstant.AD_ADDRESS_ARRAY[randomIndex]);
//        mockData.setAD_ID(String.valueOf(addata.getMadeId()));
//        mockData.setAD_NAME(LiveConstant.AD_NAME_ARRAY[randomIndex]);
//        mockData.setAD_TYPE_ID(String.valueOf(addata.getAdTypeId()));
//        mockData.setAD_TYPE_NAME(addata.getAdTypeName());
//        mockData.setAREA_CODE(LiveConstant.AD_AREA_CODE_ARRAY[randomIndex]);
//        mockData.setBEGIN_TIME(addata.getLiveStartTime() != null ? addata.getLiveStartTime().getTime() : 0);
//        mockData.setCITY_NAME("重庆市");
//        mockData.setCOUNTY_NAME(LiveConstant.AD_COUNTY_NAME_ARRAY[randomIndex]);
//        mockData.setCUSTOMER_ID("888");
//        mockData.setEND_TIME(addata.getLiveEndTime() != null ? addata.getLiveEndTime().getTime():0);
//        mockData.setFTP_PATH(addata.getLiveVideoPath());
//        mockData.setILLEGAL_CODE(addata.getIllegalCode());
//        mockData.setILLEGAL_LV(0);
//        mockData.setINSERT_TIME(System.currentTimeMillis());
//        mockData.setIS_DEL(0);
//        mockData.setIS_DISPLAY_ON_PLAT("1");
//        mockData.setIS_LEGAL("0");
//        mockData.setLATITUDE("");
//        mockData.setLONGITUDE("");
//        mockData.setMEDIA_ID(LiveConstant.MEDIA_ID_ARRAY[randomIndex]);
//        mockData.setMEDIA_NAME(addata.getMediaName());
//        mockData.setOLD_ID(String.valueOf(addata.getMaybeId()));
//        mockData.setPLAY_DATE(addata.getLiveStartTime().toLocalDateTime().format(df));
//        mockData.setPLAY_LEAN(addata.getLiveDuraTime());
//        mockData.setILLEGAL_CONTENT(pubIllegalItemService.getIllegalItems(addata.getIllegalCode()));
//        mockData.setPROOF_PATH("");
//        mockData.setPROVINCE_NAME("重庆市");
//        mockData.setQUERY_AREA_LEVEL("2");
//        mockData.setSITE_TYPE(LiveConstant.SITE_TYPE_ARRAY[randomIndex]);
//        mockData.setSOURCE_ID(LiveConstant.SOURCE_ID_ARRAY[randomIndex]);
//        mockData.setSOURCE_NAME(addata.getLiveRoomOwnerName());
//        mockData.setSTATE("2");
//        mockData.setGOODS_COUNT(addata.getGoodsCount());
//        mockData.setVIEWER_COUNT(addata.getLiveAudienceCount());
//        mockData.setWATCH_COUNT(addata.getWatchCount());
//        mockData.setLIVE_USER_HEAD_PORTRAITS(addata.getLiveUserHeadPortraits());
        return mockData;
    }

    private AdIllegalResult convertAdIllegalResult(Addata addata)
    {
        AdIllegalResult adIllegalResult = new AdIllegalResult();
        adIllegalResult.setAdId(addata.getMadeId());
        adIllegalResult.setSourceId(addata.getSiteId());
        adIllegalResult.setAdName(addata.getAdName());
        adIllegalResult.setAdTypeId(addata.getAdTypePid());
        adIllegalResult.setAdTypeName(addata.getAdTypePname());
        //TODO:需要查询地区重构
        adIllegalResult.setAdAddress("");
        adIllegalResult.setBeginTime(addata.getBeginTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        adIllegalResult.setEndTime(addata.getEndTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        adIllegalResult.setPlayDate(addata.getLiveStartTime());
        adIllegalResult.setPlayLean(adPlayLean(addata.getDura()));
        //TODO:需要查询地区重构
        adIllegalResult.setQueryAreaLevel(1);
        //TODO:需要查询地区重构
        adIllegalResult.setAreaCode("");
        adIllegalResult.setIsDel(0);
        adIllegalResult.setState(0);
        adIllegalResult.setIllegalCode(addata.getIllegalCode());
        //TODO:100-999为直播ID，100为直播平台状态
        adIllegalResult.setSiteType(100);
        adIllegalResult.setAdBeginUrl("");
        adIllegalResult.setAdGroundUrl("");
        adIllegalResult.setAdBeginSnapshot("");
        adIllegalResult.setAdGroundSnapshot("");
        adIllegalResult.setInsertTime(new Timestamp(System.currentTimeMillis()));
        adIllegalResult.setCustomerId(0);
        //TODO:媒体编号
        adIllegalResult.setMediaId(0);
        adIllegalResult.setIsDispose(0);
        adIllegalResult.setIsViewed(0);
        adIllegalResult.setIsSend(0);
        //TODO:设置资源类型
        adIllegalResult.setSourceType(6);
        adIllegalResult.setIsDisplayOnPlat(0);
        adIllegalResult.setSourceName(addata.getLiveRoomOwnerName());
        adIllegalResult.setMediaName(addata.getMediaName());
        //TODO:省份
        adIllegalResult.setProvinceName("");
        adIllegalResult.setCityName("");
        adIllegalResult.setCountyName("");
        adIllegalResult.setOldId(addata.getMadeId());
        adIllegalResult.setIllegalContent(pubIllegalItemService.getIllegalItems(addata.getIllegalCode()));
        adIllegalResult.setFtpPath(addata.getLiveVideoPath());
        adIllegalResult.setLongitude("");
        adIllegalResult.setLatitude("");
        adIllegalResult.setIllegalLv(1);
        adIllegalResult.setSonTypeId(addata.getAdTypeId());
        adIllegalResult.setSonTypeName(addata.getAdTypeName());
        adIllegalResult.setDatapushId("");
        return adIllegalResult;
    }

    private MockData generateMockData()
    {
        MockData mockData = new MockData();
//        Random random = new Random();
//        int randomIndex = random.nextInt(LiveConstant.AD_ADDRESS_ARRAY.length);
//        int idIndex = random.nextInt(LiveConstant.ID_INDEX);
//        int viewerCount = random.nextInt(LiveConstant.COUNT_INDEX);
//        mockData.setAD_ADDRESS(LiveConstant.AD_ADDRESS_ARRAY[randomIndex]);
//        mockData.setAD_ID(Integer.toString(idIndex));
//        mockData.setAD_NAME(LiveConstant.AD_NAME_ARRAY[randomIndex]);
//        mockData.setAD_TYPE_ID("108");
//        mockData.setAD_TYPE_NAME("衣服");
//        mockData.setAREA_CODE(LiveConstant.AD_AREA_CODE_ARRAY[randomIndex]);
//        mockData.setBEGIN_TIME(0);
//        mockData.setCITY_NAME("重庆市");
//        mockData.setCOUNTY_NAME(LiveConstant.AD_COUNTY_NAME_ARRAY[randomIndex]);
//        mockData.setCUSTOMER_ID("888");
//        mockData.setEND_TIME(0);
//        mockData.setFTP_PATH("");
//        mockData.setILLEGAL_CODE("");
//        mockData.setILLEGAL_LV(0);
//        mockData.setINSERT_TIME(System.currentTimeMillis());
//        mockData.setIS_DEL(0);
//        mockData.setIS_DISPLAY_ON_PLAT("1");
//        mockData.setIS_LEGAL("0");
//        mockData.setLATITUDE("");
//        mockData.setLONGITUDE("");
//        mockData.setMEDIA_ID(LiveConstant.MEDIA_ID_ARRAY[randomIndex]);
//        mockData.setMEDIA_NAME(LiveConstant.MEDIA_NAME_ARRAY[randomIndex]);
//        mockData.setOLD_ID(Integer.toString(idIndex));
//        mockData.setPLAY_DATE("");
//        mockData.setPLAY_LEAN(0);
//        mockData.setILLEGAL_CONTENT("名称不符");
//        mockData.setPROOF_PATH("");
//        mockData.setPROVINCE_NAME("重庆市");
//        mockData.setQUERY_AREA_LEVEL("2");
//        mockData.setSITE_TYPE(LiveConstant.SITE_TYPE_ARRAY[randomIndex]);
//        mockData.setSOURCE_ID(LiveConstant.SOURCE_ID_ARRAY[randomIndex]);
//        mockData.setSOURCE_NAME(LiveConstant.SOURCE_NAME_ARRAY[randomIndex]);
//        mockData.setSTATE("2");
//        mockData.setGOODS_COUNT(1);
//        mockData.setVIEWER_COUNT(viewerCount);
//        mockData.setWATCH_COUNT(viewerCount);
        return mockData;
    }

    private String adPlayLean(String dura)
    {
        return DateUtil3.timeToSecond(dura);
    }
}
