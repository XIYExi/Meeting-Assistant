package com.ruoyi.dev.push;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class PushDevApplication {
    public static void main(String[] args) throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(PushDevApplication.class);
        builder.headless(false).run(args);
        System.out.println("---------------启动成功---------------");

        //设置rtmp服务器推流地址
        String outputPath = "rtmp://127.0.0.1:1935/live/stream";
        RecordPush recordPush = new RecordPush();
        recordPush.getRecordPush(outputPath, 25);
    }
}
