package com.ruoyi.rec.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection  = "content_meeting_rec")
public class ContentMeetingRec implements Serializable {

    private Integer meetingId;

    private List<Recommendation> recs;

}
