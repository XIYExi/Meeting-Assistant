package com.ruoyi.collection.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collation = "user_ratings")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRating {
    private long userId;
    private long meetingId;
    private double score;
    private Date timestamp;
}
