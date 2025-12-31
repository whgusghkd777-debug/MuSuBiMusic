package com.mysite.sbb.answer;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerForm {

    @NotEmpty(message = "内容は必須項目です。")  // 일본어 에러 메시지 (원하면 한국어로 바꿔)
    private String content;
}