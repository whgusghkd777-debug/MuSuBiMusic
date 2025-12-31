// AnswerDto.java
package com.mysite.sbb.music.dto;

import java.time.LocalDateTime;

public record AnswerDto(
        String username,
        String content,
        LocalDateTime createDate
) {}