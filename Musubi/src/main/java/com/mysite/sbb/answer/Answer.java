package com.mysite.sbb.answer;

import java.time.LocalDateTime;
import com.mysite.sbb.music.Music;
import com.mysite.sbb.user.SiteUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    // Music 엔티티와 N:1 관계 (여러 답변이 하나의 음악에 달림)
    @ManyToOne
    private Music music;

    // 작성자 정보
    @ManyToOne
    private SiteUser author;
}