package com.mysite.sbb.music.controller;

import com.mysite.sbb.music.Music;
import com.mysite.sbb.music.MusicService;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserRole;
import com.mysite.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@RequestMapping("/music")
@RequiredArgsConstructor
@Controller
public class MusicController {

    private final MusicService musicService;
    private final UserService userService;

    @Value("${file.upload-path}")
    private String uploadPath;

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id) {
        Music music = this.musicService.getMusic(id);
        model.addAttribute("music", music);
        String videoId = this.musicService.extractYoutubeId(music.getUrl());
        model.addAttribute("youtubeId", videoId);
        return "music_detail";
    }

    @GetMapping("/random")
    public String randomMusic() {
        Long randomId = this.musicService.getRandomMusicId();
        if (randomId == null) {
            return "redirect:/music/list";
        }
        return String.format("redirect:/music/detail/%s", randomId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create() {
        return "music_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@RequestParam("title") String title, @RequestParam("artist") String artist,
                         @RequestParam("url") String url, @RequestParam("content") String content,
                         @RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        SiteUser author = this.userService.getUser(principal.getName());
        String fileName = null;
        if (!file.isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + fileName));
        }
        this.musicService.create(title, artist, url, content, fileName, author);
        return "redirect:/music/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String musicVote(Principal principal, @PathVariable("id") Long id) {
        Music music = this.musicService.getMusic(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.musicService.vote(music, siteUser);
        return String.format("redirect:/music/detail/%s", id);
    }

    @GetMapping("/ranking")
    public String ranking(Model model) {
        model.addAttribute("musicList", this.musicService.getRankingList());
        return "ranking";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("musicList", this.musicService.getList());
        return "music_list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String musicDelete(Principal principal, @PathVariable("id") Long id) {
        Music music = this.musicService.getMusic(id);
        SiteUser user = this.userService.getUser(principal.getName());

        boolean isAdmin = user.getRole() == UserRole.ADMIN;
        boolean isAuthor = music.getAuthor().getUsername().equals(principal.getName());

        if (!isAdmin && !isAuthor) {
            return "redirect:/music/list";
        }

        this.musicService.delete(music);

        if (isAdmin) {
            return "redirect:/music/list";
        } else {
            return "redirect:/user/mypage";
        }
    }
}