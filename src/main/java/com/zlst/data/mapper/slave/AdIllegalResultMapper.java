package com.zlst.data.mapper.slave;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlst.data.pojo.master.Addata;
import com.zlst.data.pojo.slave.AdIllegalResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author: Quentin Zhang
 * @create: 2020-10-15 13:59
 **/
@Mapper
public interface AdIllegalResultMapper extends BaseMapper<AdIllegalResult> {
}
