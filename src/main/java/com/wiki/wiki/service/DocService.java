package com.wiki.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wiki.wiki.domain.Content;
import com.wiki.wiki.domain.Doc;
import com.wiki.wiki.domain.DocExample;
import com.wiki.wiki.mapper.ContentMapper;
import com.wiki.wiki.mapper.DocMapper;
import com.wiki.wiki.mapper.DocMapperCust;
import com.wiki.wiki.req.DocQueryReq;
import com.wiki.wiki.req.DocSaveReq;
import com.wiki.wiki.resp.DocQueryResp;
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
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    //用注解将DocMapper注入
    @Resource
    private DocMapper docMapper;

    @Resource
    private DocMapperCust docMapperCust;

    @Resource
    private ContentMapper contentMapper;

    // 注入雪花算法工具类
    @Resource
    private SnowFlake snowFlake;

    public PageResp<DocQueryResp> list(DocQueryReq req){
        //往后端mapper发送请求
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();

        PageHelper.startPage(req.getPage(),req.getSize());
        List<Doc> docsList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docsList);
        LOG.info("总行数：{}",pageInfo.getTotal());
        LOG.info("总页数：{}",pageInfo.getPages());
        //往controller返回数据
//        List<DocResp> respList = new ArrayList<>();
//        for(Doc doc : docsList) {
        //对象复制
//            DocResp docResp = CopyUtil.copy(doc, DocResp.class);
//            respList.add(docResp);
//        }

        //列表复制
        List<DocQueryResp> respList = CopyUtil.copyList(docsList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);
        return pageResp;
    }

    public List<DocQueryResp> all(Long ebookId){
        //往后端mapper发送请求
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docsList = docMapper.selectByExample(docExample);

        //列表复制
        List<DocQueryResp> list = CopyUtil.copyList(docsList, DocQueryResp.class);

        return list;
    }

    // 保存
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if(ObjectUtils.isEmpty(req.getId())) {
            // id为空 新增
            doc.setId( snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);
            content.setId( doc.getId() );
            contentMapper.insert(content);
        } else {
            // id存在，更新
            docMapper.updateByPrimaryKey(doc);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            // 若表中不存在id，说明doc表中有这个id，但是content没有，需要在content中插入
            if (count == 0) {
                contentMapper.insert(content);
            }
        }
    }

    //  删除
    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        // 文档阅读数+1
        docMapperCust.increaseViewCount(id);
        if (ObjectUtils.isEmpty(content)) {
            return "";
        } else {
            return content.getContent();
        }
    }
}
