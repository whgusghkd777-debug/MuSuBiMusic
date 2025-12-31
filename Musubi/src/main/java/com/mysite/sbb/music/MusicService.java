package com.mysite.sbb.music;

import com.mysite.sbb.music.dto.MusicListDto;
import com.mysite.sbb.music.dto.MusicRepository;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class MusicService {
    private final MusicRepository musicRepository;
    private final AnswerRepository answerRepository;

    private List<MusicListDto> convertToDtoList(List<Music> musicList) {
        return musicList.stream()
                .map(music -> new MusicListDto(
                    music.getId(), 
                    music.getTitle(), 
                    music.getArtist(), 
                    music.getUrl(), 
                    music.getThumbnailUrl(), 
                    music.getCreateDate(),
                    music.getVoter() != null ? music.getVoter().size() : 0,
                    music.getFilePath())) 
                .collect(Collectors.toList());
    }

    public List<MusicListDto> getRankingList() {
        List<Music> musicList = this.musicRepository.findAll();
        return musicList.stream()
                .sorted((m1, m2) -> Integer.compare(
                    m2.getVoter() != null ? m2.getVoter().size() : 0, 
                    m1.getVoter() != null ? m1.getVoter().size() : 0))
                .map(music -> new MusicListDto(
                    music.getId(), music.getTitle(), music.getArtist(), 
                    music.getUrl(), music.getThumbnailUrl(), music.getCreateDate(),
                    music.getVoter() != null ? music.getVoter().size() : 0, 
                    music.getFilePath()))
                .collect(Collectors.toList());
    }

    public List<MusicListDto> getMyMusicList(SiteUser author) {
        List<Music> musicList = this.musicRepository.findByAuthorOrderByCreateDateDesc(author);
        return convertToDtoList(musicList);
    }

    public List<MusicListDto> getList() {
        return convertToDtoList(this.musicRepository.findAllByOrderByCreateDateDesc());
    }

    public Music getMusic(Long id) {
        return this.musicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Music not found"));
    }

    /* ランダム再生用のID取得メソッドを追加 */
    public Long getRandomMusicId() {
        List<Music> musicList = this.musicRepository.findAll();
        if (musicList.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(musicList.size());
        return musicList.get(randomIndex).getId();
    }

    @Transactional
    public void create(String title, String artist, String url, String content, String fileName, SiteUser author) {
        Music music = new Music();
        music.setTitle(title);
        music.setArtist(artist);
        music.setUrl(url);
        music.setContent(content);
        music.setFilePath(fileName);
        music.setAuthor(author);
        music.setCreateDate(LocalDateTime.now());
        
        String videoId = extractYoutubeId(url);
        if (videoId != null) {
            music.setThumbnailUrl("https://img.youtube.com/vi/" + videoId + "/mqdefault.jpg");
        }
        this.musicRepository.save(music);
    }

    @Transactional
    public void vote(Music music, SiteUser siteUser) {
        music.getVoter().add(siteUser);
        this.musicRepository.save(music);
    }

    public String extractYoutubeId(String url) {
        if (url == null || url.trim().isEmpty()) return null;
        String pattern = "(?<=watch\\?v=|/videos/|embed/|youtu.be/|/v/|/e/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%2F|youtu.be%2F|%2Fv%2F)[^#&?\\n]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        return matcher.find() ? matcher.group() : null;
    }

    @Transactional
    public void createAnswer(Music music, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setMusic(music);
        answer.setAuthor(author);
        this.answerRepository.save(answer);
    }

    @Transactional
    public void delete(Music music) {
        this.musicRepository.delete(music);
    }
}