package com.wiki.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wiki.wiki.domain.User;
import com.wiki.wiki.domain.UserExample;
import com.wiki.wiki.mapper.UserMapper;
import com.wiki.wiki.req.UserQueryReq;
import com.wiki.wiki.req.UserSaveReq;
import com.wiki.wiki.resp.UserQueryResp;
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
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    //用注解将UserMapper注入
    @Resource
    private UserMapper userMapper;

    // 注入雪花算法工具类
    @Resource
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> list(UserQueryReq req){
        //往后端mapper发送请求
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getLoginName())){
            criteria.andNameLike("%"+req.getLoginName()+"%");
        }
        PageHelper.startPage(req.getPage(),req.getSize());
        List<User> usersList = userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(usersList);
        LOG.info("总行数：{}",pageInfo.getTotal());
        LOG.info("总页数：{}",pageInfo.getPages());
        //往controller返回数据
//        List<UserResp> respList = new ArrayList<>();
//        for(User user : usersList) {
        //对象复制
//            UserResp userResp = CopyUtil.copy(user, UserResp.class);
//            respList.add(userResp);
//        }

        //列表复制
        List<UserQueryResp> respList = CopyUtil.copyList(usersList, UserQueryResp.class);

        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);
        return pageResp;
    }

    // 保存
    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if(ObjectUtils.isEmpty(req.getId())) {
            // id为空 新增
            user.setId( snowFlake.nextId());
//            user.setDocCount(0);
//            user.setViewCount(0);
//            user.setVoteCount(0);
            userMapper.insert(user);
        } else {
            // id存在，更新
            userMapper.updateByPrimaryKey(user);
        }
    }

    //  删除
    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

}
