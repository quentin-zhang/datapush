package com.zlst.data.mapper.master;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlst.data.pojo.master.Addata;
import com.zlst.data.pojo.master.ImMaybeAd;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: Quentin Zhang
 * @create: 2020-10-15 13:59
 **/
@Mapper
public interface ImMaybeAdMapper extends BaseMapper<ImMaybeAd> {
    public List<Addata> findAd();
    public List<ImMaybeAd> findLive();
}
