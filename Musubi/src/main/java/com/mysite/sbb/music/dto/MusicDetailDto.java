package com.mysite.sbb.music.dto;

import com.mysite.sbb.answer.Answer;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MusicDetailDto {
    private Long id;
    private String title;
    private String artist;
    private String url;
    private String content;
    private LocalDateTime createDate;
    private String authorName; 
    private int voterCount;    
    private List<Answer> answerList; 
}