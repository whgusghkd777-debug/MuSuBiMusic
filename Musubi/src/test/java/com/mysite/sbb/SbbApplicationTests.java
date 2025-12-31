package com.mysite.sbb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.mysite.sbb.music.Music;
import com.mysite.sbb.music.MusicRepository;

@SpringBootTest
class SbbApplicationTests {

    @Autowired
    private MusicRepository musicRepository;

    @Test
    void testMusicJpa() {
        // 기존 Question 테스트를 지우고 Music 테스트로 교체
        Music m = new Music();
        m.setTitle("테스트 음악");
        m.setArtist("가수");
        m.setUrl("https://youtube.com/test");
        this.musicRepository.save(m);

        List<Music> all = this.musicRepository.findAll();
        assertEquals(1, all.size());
        assertEquals("테스트 음악", all.get(0).getTitle());
    }
}