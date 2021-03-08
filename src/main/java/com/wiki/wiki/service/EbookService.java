package com.wiki.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wiki.wiki.domain.Ebook;
import com.wiki.wiki.domain.EbookExample;
import com.wiki.wiki.mapper.EbookMapper;
import com.wiki.wiki.req.EbookReq;
import com.wiki.wiki.resp.EbookResp;
import com.wiki.wiki.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    //用注解将EbookMapper注入
    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req){
        //往后端mapper发送请求
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%"+req.getName()+"%");
        }
        PageHelper.startPage(1,3);
        List<Ebook> ebooksList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebooksList);
        LOG.info("总行数：{}",pageInfo.getTotal());
        LOG.info("总页数：{}",pageInfo.getPages());
        //往controller返回数据
//        List<EbookResp> respList = new ArrayList<>();
//        for(Ebook ebook : ebooksList) {
        //对象复制
//            EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
//            respList.add(ebookResp);
//        }

        //列表复制
        List<EbookResp> respList = CopyUtil.copyList(ebooksList,EbookResp.class);
        return respList;
    }
}
