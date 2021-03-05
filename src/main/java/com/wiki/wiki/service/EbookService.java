package com.wiki.wiki.service;

import com.wiki.wiki.domain.Ebook;
import com.wiki.wiki.mapper.EbookMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {

    //用注解将EbookMapper注入
    @Resource
    private EbookMapper ebookMapper;

    public List<Ebook> list(){
        return ebookMapper.selectByExample(null);
    }
}
