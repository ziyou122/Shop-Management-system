package com.wiki.wiki.service;

import com.wiki.wiki.domain.Demo;
import com.wiki.wiki.mapper.DemoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DemoService {

    //用注解将DemoMapper注入
    @Resource
    private DemoMapper demoMapper;

    public List<Demo> list(){
        return demoMapper.selectByExample(null);
    }
}
