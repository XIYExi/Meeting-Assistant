package com.ruoyi.agent.entity;

import lombok.Data;

/**
 * 所谓恒脑大模型的 文本模型消息体
 * {其实就是通义千问}
 */
@Data
public class MessageDas {
    private String role;

    /*
    当 role=assistant 时，用于标识 Bot 的消息类型，取值：
      answer：Bot 最终返回给用户的消息内容。
      function_call：Bot 对话过程中调用函数 (function call) 的中间结果。
      tool_response：调用工具 (function call) 后返回的结果。
      follow_up：如果在 Bot 上配置打开了用户问题建议开关，则会返回推荐问题相关的回复内容
     */
    private String type;

    /*
    消息内容的类型，取值。text: 文本类型。
    当 type = answer 时，消息内容格式为 Markdown。即 Bot 的最终回复的内容格式是 Markdown，不是纯文本。
     */
    private String content_type;

    private String content;
}
