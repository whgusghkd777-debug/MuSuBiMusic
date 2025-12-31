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
    private String authorName; // 작성자 이름 (SiteUser의 username)
    private int voterCount;    // 좋아요 개수 (voter.size())
    private List<Answer> answerList; // 이 음악에 달린 댓글들
}