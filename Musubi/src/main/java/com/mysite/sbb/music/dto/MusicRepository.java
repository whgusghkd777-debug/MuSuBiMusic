package com.mysite.sbb.music.dto;

import com.mysite.sbb.music.Music;
import com.mysite.sbb.user.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long> {
    // ユーザー別の投稿リストを取得するためのメソッド
    List<Music> findByAuthorOrderByCreateDateDesc(SiteUser author);
    
    List<Music> findAllByOrderByCreateDateDesc();

    @Query(value = "SELECT * FROM music ORDER BY (SELECT COUNT(*) FROM music_voter WHERE music_id = music.id) DESC LIMIT 10", nativeQuery = true)
    List<Music> findTopRanking();
}