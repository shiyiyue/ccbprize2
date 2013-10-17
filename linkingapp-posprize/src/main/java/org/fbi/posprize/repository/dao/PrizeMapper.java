package org.fbi.posprize.repository.dao;

import org.apache.ibatis.annotations.Select;

public interface PrizeMapper {
    @Select("SELECT count(*) FROM  ptoper")
    int selectCount();
}
