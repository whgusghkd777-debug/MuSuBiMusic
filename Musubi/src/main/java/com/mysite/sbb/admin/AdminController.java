package com.mysite.sbb.admin;

import com.mysite.sbb.music.MusicService;
import com.mysite.sbb.music.dto.MusicListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    private final MusicService musicService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<MusicListDto> musicList = this.musicService.getList();
        model.addAttribute("musicList", musicList);
        /* templates/admin/admin_dashboard.html を参照するように修正 */
        return "admin/admin_dashboard";
    }
}