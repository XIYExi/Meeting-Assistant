package com.ruoyi.rag.assistant.component;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
public class GeoMapComponent {

    @Value("${aliyun.market.appcode}")
    private String appCode;

    @Autowired
    private RestTemplate restTemplate;

    private static final String postForGeoUrl = "https://jmgeocode.market.alicloudapi.com";
    private String geoCodePath = "/geocode/geo/query";

    private static final String postForDistanceUrl = "https://jmlxgh.market.alicloudapi.com";
    private String distancePath = "/route/distance-measurement";
    private String routerPlanningPath = "/route/drive";


    /**
     * 根据实际位置名称获取信息（经纬度等）
     * @param address
     * @param city
     * @return
     */
    public Object geoCodeQuery(String address, String city){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "APPCODE " + appCode);
        // headers.add("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("address", address);
        paramsMap.add("city", city);
        paramsMap.add("output", "JSON");

        RequestEntity requestEntity = RequestEntity
                .post("")
                .headers(headers)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.ALL)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(paramsMap);

        // 构建完整的请求 URL
        String url = postForGeoUrl + geoCodePath;

        ResponseEntity<String> mapResponseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        //返回状态码
        HttpStatus statusCode = mapResponseEntity.getStatusCode();
        //返回数据
        String body = mapResponseEntity.getBody();
        Map<String,Object> map = JSON.parseObject(body, Map.class);
        return map;
    }


    /**
     * 查询当前位置到目标位置之间的距离
     * @param origins 当前位置
     * @param destination 目标位置
     * @return
     */
    public Object getCarDistanceAndTime(String origins, String destination) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "APPCODE " + appCode);
        // headers.add("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("origins", origins);
        paramsMap.add("destination", destination);

        RequestEntity requestEntity = RequestEntity
                .post("")
                .headers(headers)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.ALL)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(paramsMap);

        // 构建完整的请求 URL
        String url = postForDistanceUrl + distancePath;

        ResponseEntity<String> mapResponseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        //返回状态码
        HttpStatus statusCode = mapResponseEntity.getStatusCode();
        //返回数据
        String body = mapResponseEntity.getBody();
        Map<String,Object> map = JSON.parseObject(body, Map.class);
        return map;
    }


    /**
     * 开车路径规划，获取api结果
     * @param origins 起始点经纬度
     * @param distributions 终点经纬度
     * @return
     */
    public Object getPathPlanning(String origins, String distributions){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "APPCODE " + appCode);
        // headers.add("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");

        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("origin", origins);
        paramsMap.add("destination", distributions);
        //paramsMap.add("origin", "116.406243,39.901403");
        //paramsMap.add("destination", "120.228209,30.209501");
        paramsMap.add("carType", "0");
        paramsMap.add("ferry", "1");
        paramsMap.add("showFields", "polyline");

        RequestEntity requestEntity = RequestEntity
                .post("")
                .headers(headers)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.ALL)
                .acceptCharset(StandardCharsets.UTF_8)
                .body(paramsMap);

        // 构建完整的请求 URL
        String url = postForDistanceUrl + routerPlanningPath;

        ResponseEntity<String> mapResponseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
        //返回状态码
        HttpStatus statusCode = mapResponseEntity.getStatusCode();
        //返回数据
        String body = mapResponseEntity.getBody();
        Map<String,Object> map = JSON.parseObject(body, Map.class);
        return map;
    }

}