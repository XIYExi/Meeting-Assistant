package com.ruoyi.rag.assistant.component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.utils.bean.BeanUtils;
import com.ruoyi.rag.assistant.domain.Meeting;
import com.ruoyi.rag.assistant.domain.MeetingAgenda;
import com.ruoyi.rag.assistant.entity.MeetingResponse;
import com.ruoyi.rag.assistant.mapper.MeetingAgendaMapper;
import com.ruoyi.rag.assistant.mapper.MeetingClipMapper;
import com.ruoyi.rag.assistant.mapper.MeetingMapper;
import com.ruoyi.rag.model.DomesticEmbeddingModel;
import com.ruoyi.rag.utils.IdGenerator;
import com.ruoyi.rag.utils.MilvusOperateUtils;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.output.Response;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class VectorSearchComponent {

    @Resource
    private DomesticEmbeddingModel domesticEmbeddingModel;
    @Resource
    private MilvusOperateUtils milvus;

    @Resource
    private MeetingMapper meetingMapper;
    @Resource
    private MeetingAgendaMapper meetingAgendaMapper;
    @Resource
    private MeetingClipMapper meetingClipMapper;


    public MeetingResponse vectorSearch(String title) {
        Response<Embedding> listResponse = domesticEmbeddingModel.embed(title);
        Embedding embeddings = listResponse.content();
        List<Float> features = embeddings.vectorAsList();
        List<?> meeting = milvus.searchByFeature("meeting_home", features);
        Long composeId = (Long)meeting.get(0);
        long originalId = IdGenerator.getOriginalId((Long) composeId);
        boolean isMeeting = IdGenerator.isMeeting((Long) composeId);
        MeetingResponse result = new MeetingResponse();
        if (isMeeting) {
            Meeting meetingData = meetingMapper.selectById(originalId);
            List<MeetingAgenda> agenda = meetingAgendaMapper.selectList(new QueryWrapper<MeetingAgenda>().eq("meeting_id", meetingData.getId()));
            BeanUtils.copyProperties(meetingData, result);
            result.setAgenda(agenda);
        }
        else {
            // 查出单条议程的信息
            MeetingAgenda meetingAgenda = meetingAgendaMapper.selectById(originalId);
            // 在meeting_agenda外键里面查出meeting
            Meeting meetingData = meetingMapper.selectById(meetingAgenda.getMeetingId());
            // 再通过meetingId反向查出所有的agenda
            List<MeetingAgenda> agenda = meetingAgendaMapper.selectList(new QueryWrapper<MeetingAgenda>().eq("meeting_id", meetingData.getId()));
            BeanUtils.copyProperties(meetingData, result);
            result.setAgenda(agenda);
        }
        return result;
    }


}
