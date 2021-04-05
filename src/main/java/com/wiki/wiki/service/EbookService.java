package com.wiki.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wiki.wiki.domain.Ebook;
import com.wiki.wiki.domain.EbookExample;
import com.wiki.wiki.mapper.EbookMapper;
import com.wiki.wiki.req.EbookQueryReq;
import com.wiki.wiki.req.EbookSaveReq;
import com.wiki.wiki.resp.EbookQueryResp;
import com.wiki.wiki.resp.PageResp;
import com.wiki.wiki.util.CopyUtil;
import com.wiki.wiki.util.SnowFlake;
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

    // 注入雪花算法工具类
    @Resource
    private SnowFlake snowFlake;

    public PageResp<EbookQueryResp> list(EbookQueryReq req){
        //往后端mapper发送请求
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%"+req.getName()+"%");
        }
        if(!ObjectUtils.isEmpty(req.getCategoryId2())){
            criteria.andCategory2IdEqualTo(req.getCategoryId2());
        }
        PageHelper.startPage(req.getPage(),req.getSize());
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
        List<EbookQueryResp> respList = CopyUtil.copyList(ebooksList, EbookQueryResp.class);

        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);
        return pageResp;
    }

    // 保存
    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if(ObjectUtils.isEmpty(req.getId())) {
            // id为空 新增
            ebook.setId( snowFlake.nextId());
            ebook.setDocCount(0);
            ebook.setViewCount(0);
            ebook.setVoteCount(0);
            ebookMapper.insert(ebook);
        } else {
            // id存在，更新
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }

    //  删除
    public void delete(Long id) {
        ebookMapper.deleteByPrimaryKey(id);
    }

}
