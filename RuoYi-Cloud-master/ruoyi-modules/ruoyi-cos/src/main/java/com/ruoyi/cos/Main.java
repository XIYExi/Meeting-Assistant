package com.ruoyi.cos;

public class Main {
    public static void main(String[] args) {

        String url = "https://jn-1306384632.cos.ap-nanjing.myqcloud.com/common/C41CA329.jpeg";



        String[] split = url.split("/");
        String imageFileName = split[split.length - 1];
        String prefix = split[split.length - 2];
        // image命名格式为 {imageId}.{图片格式}
        String imageId = imageFileName.split("\\.")[0];

        String key = prefix + "/" + imageFileName;

        System.out.println(key);



    }
}
