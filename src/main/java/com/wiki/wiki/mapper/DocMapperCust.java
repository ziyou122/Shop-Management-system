package com.wiki.wiki.mapper;

import org.apache.ibatis.annotations.Param;

public interface DocMapperCust {
    public void increaseViewCount(@Param("id") Long id);
}