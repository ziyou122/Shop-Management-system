package com.wiki.wiki.controller;

import com.wiki.wiki.req.EbookReq;
import com.wiki.wiki.resp.CommonResp;
import com.wiki.wiki.resp.EbookResp;
import com.wiki.wiki.resp.PageResp;
import com.wiki.wiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;


    @GetMapping("/list")
    public CommonResp list(EbookReq req){
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        PageResp list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }
}
