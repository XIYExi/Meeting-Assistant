package com.ruoyi.im.common;

import com.ruoyi.im.constant.ImConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码器
 */
public class ImMsgDecoder extends ByteToMessageDecoder {

    // 一个short + 两个len
    // 每一个消息最小长度，必须包括magic，len，code
    private final static int BASE_LEN = 2 + 4 + 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        // 对 ByteBuf 内容进行校验
        if (byteBuf.readableBytes() >= BASE_LEN) {
            // 判断magic
            if (byteBuf.readShort() != ImConstants.DEFAULT_MAGIC) {
                ctx.close();
                return;
            }
            int code = byteBuf.readInt();
            int len = byteBuf.readInt();
            // 确保bytebuf剩余消息长度足够
            if (byteBuf.readableBytes() < len) {
                ctx.close();
                return;
            }
            byte[] body = new byte[len];
            byteBuf.readBytes(body);
            ImMsg imMsg = new ImMsg();
            imMsg.setCode(code);
            imMsg.setLen(len);
            imMsg.setBody(body);
            imMsg.setMagic(ImConstants.DEFAULT_MAGIC);
            out.add(imMsg);
        }
    }
}
