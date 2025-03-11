package com.ruoyi.rec.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;


/**
 * 被自己气笑了，spark存数据的时候忘了加下划线，实时推荐的库叫“stream rec”！
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection  = "stream rec")
public class StreamMeetingRec implements Serializable {
    private Integer userId;
    private List<Recommendation> recs;
}
