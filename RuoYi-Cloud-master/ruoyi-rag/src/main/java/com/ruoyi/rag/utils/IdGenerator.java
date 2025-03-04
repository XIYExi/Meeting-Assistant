package com.ruoyi.rag.utils;


/**
 * 用来解决一个id重复的问题，
 * meeting meeting_agenda 的 id 可能重复，通过这种方法来保证存储到 milvus 之中 id 唯一
 */
public class IdGenerator {
    // 高位标记类型：会议=1，议程=2（占用最高 4 位）
    private static final long MEETING_FLAG = 1L << 60;
    private static final long AGENDA_FLAG = 2L << 60;

    public static long generateMeetingId(long originalId) {
        return MEETING_FLAG | (originalId & 0x0FFFFFFFFFFFFFFFL); // 确保原始 ID 不超过 2^56
    }

    public static long generateAgendaId(long originalId) {
        return AGENDA_FLAG | (originalId & 0x0FFFFFFFFFFFFFFFL);
    }

    // 解析原始 ID 和类型
    public static boolean isMeeting(long id) {
        return (id & MEETING_FLAG) != 0;
    }

    public static long getOriginalId(long id) {
        return id & 0x0FFFFFFFFFFFFFFFL;
    }
}
