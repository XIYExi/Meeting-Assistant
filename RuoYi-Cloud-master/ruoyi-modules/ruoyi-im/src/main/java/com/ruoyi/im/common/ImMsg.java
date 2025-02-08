package com.ruoyi.im.common;

import com.ruoyi.im.constant.ImConstants;

import java.io.Serializable;
import java.util.Arrays;

/**
 *  消息体
 */
public class ImMsg implements Serializable {

    // 魔数 数据校验
    private short magic;

    // 消息种类标识
    private int code;

    // 用于记录body长度
    private int len;

    // 消息体内容
    private byte[] body;


    public static ImMsg build(int code, String data) {
        ImMsg imMsg = new ImMsg();
        imMsg.setMagic(ImConstants.DEFAULT_MAGIC);
        imMsg.setCode(code);
        imMsg.setBody(data.getBytes());
        imMsg.setLen(imMsg.getBody().length);
        return imMsg;
    }


    @Override
    public String toString() {
        return "ImMsg{" +
                "magic=" + magic +
                ", code=" + code +
                ", len=" + len +
                ", body=" + Arrays.toString(body) +
                '}';
    }

    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
