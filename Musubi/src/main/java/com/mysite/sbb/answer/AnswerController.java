package com.mysite.sbb.answer;

import com.mysite.sbb.music.Music;
import com.mysite.sbb.music.MusicService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final MusicService musicService;
    private final AnswerService answerService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Long id, @RequestParam(value="content") String content, Principal principal) {
        /* コンパイルエラー解決：引数の型を Long に合わせて呼び出し */
        Music music = this.musicService.getMusic(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.answerService.create(music, content, siteUser);
        return String.format("redirect:/music/detail/%s", id);
    }
}