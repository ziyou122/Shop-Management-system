package com.wiki.wiki.job;

import com.wiki.wiki.service.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DocJob {

    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Resource
    private DocService docService;

    /**
     * 每30s 更新电子书信息
     */
    @Scheduled(cron = "5/30 * * * * ?")
    public void cron() {
       LOG.info("开始更新电子书下的文档数据");
       long start = System.currentTimeMillis();
       docService.updateEbookInfo();
        LOG.info("结束更新电子书下的文档数据，耗时：{}毫秒", System.currentTimeMillis() - start);
    }

}
