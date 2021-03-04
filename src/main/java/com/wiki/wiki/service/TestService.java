package com.wiki.wiki.service;

import com.wiki.wiki.domain.Test;
import com.wiki.wiki.mapper.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TestService {

    //用注解将TestMapper注入
    @Resource
    private TestMapper testMapper;

    public List<Test> list(){
        return testMapper.list();
    }
}
