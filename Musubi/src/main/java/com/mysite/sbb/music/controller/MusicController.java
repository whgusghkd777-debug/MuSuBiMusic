package com.mysite.sbb.music.controller;

import com.mysite.sbb.music.Music;
import com.mysite.sbb.music.MusicService;
import com.mysite.sbb.user.SiteUser;
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
        
        // YouTube IDの抽出
        String videoId = this.musicService.extractYoutubeId(music.getUrl());
        model.addAttribute("youtubeId", videoId);
        
        return "music_detail";
    }

    
    /* ランダム再生: 既存のロジックを維持 */
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
/**
 * ファイルとURL、両形式に対応した投稿処理
 */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@RequestParam("title") String title, @RequestParam("artist") String artist,
                         @RequestParam("url") String url, @RequestParam("content") String content,
                         @RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        SiteUser author = this.userService.getUser(principal.getName());
        String fileName = null;
        // ファイルがアップロードされた場合、UUIDを用いて一意のファイル名を生成
        if (!file.isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + fileName));
        }
        // DBのフィールド名に合わせて保存 (filePathと想定)
        // 形式を問わず、一括でService層を通じてデータベースへ保存
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String musicDelete(@PathVariable("id") Long id) {
        Music music = this.musicService.getMusic(id);
        this.musicService.delete(music);
        return "redirect:/music/list";
    }
}
