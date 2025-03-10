package com.ruoyi.collection.controller;


import com.ruoyi.collection.domain.UserRating;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.web.domain.AjaxResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/collect")
public class CollectionController {

    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 采集一条数据
     * @return
     */
    @GetMapping("/collect")
    public AjaxResult collect() {
        Long userId = 400L;
        Integer timestamp = Math.toIntExact(DateUtils.getNowDate().getTime() / 1000);
        redisTemplate.opsForList().leftPush("rec:rating:userId:"+userId, 200 + ":" + 3.5);
        redisTemplate.opsForList().leftPush("rec:rating:userId:"+userId, 201 + ":" + 2.5);
        redisTemplate.opsForList().leftPush("rec:rating:userId:"+userId, 202 + ":" + 1.5);
        redisTemplate.opsForList().leftPush("rec:rating:userId:"+userId, 203 + ":" + 4.5);
        redisTemplate.opsForList().leftPush("rec:rating:userId:"+userId, 204 + ":" + 5.0);
        redisTemplate.opsForList().leftPush("rec:rating:userId:"+userId, 205 + ":" + 3.5);
        return AjaxResult.success();
    }


    /**
     * 为随机3500个用户，创造3~10条判分数据，方便训练推荐算法
     * @return
     */
    @GetMapping("/mock2500Interact")
    public AjaxResult mockInteract() {
        Random random = new Random();
        int numberOfUsers = 3500;
        for (int i = 0; i < numberOfUsers; i++) {
            long userId = random.nextInt(5000) + 100; // 生成100到5099之间的随机userId
            int numberOfInteractions = random.nextInt(8) + 3; // 生成3到10之间的随机交互数量
            for (int j = 0; j < numberOfInteractions; j++) {
                UserRating userRating = new UserRating();
                userRating.setUserId(userId);
                userRating.setMeetingId(random.nextInt(989) + 24); // 生成24到1012之间的随机meetingId
                userRating.setScore((random.nextInt(11) * 0.5)); // 生成0到5之间的随机score，步长为0.5
                userRating.setTimestamp(new Date()); // 当前时间戳
                mongoTemplate.save(userRating, "user_ratings"); // 将数据保存到MongoDB的user_ratings集合中
            }
        }
        return AjaxResult.success();
    }

    @GetMapping("/count")
    public AjaxResult count() {

        return AjaxResult.success();
    }

}
