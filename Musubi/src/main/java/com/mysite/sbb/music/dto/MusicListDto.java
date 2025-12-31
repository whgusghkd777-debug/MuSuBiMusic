package com.mysite.sbb.music.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MusicListDto {
    private Long id;
    private String title;
    private String artist;
    private String url;
    private String thumbnailUrl;
    private LocalDateTime createDate;
    private int voteCount;
    private String filePath;
}