package com.mysite.sbb.user;

import jakarta.persistence.*;
import java.util.Set;
import com.mysite.sbb.music.Music;

@Entity
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    // 推薦機能のために追加
    @ManyToMany
    private Set<Music> votedMusic;

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public Set<Music> getVotedMusic() { return votedMusic; }
    public void setVotedMusic(Set<Music> votedMusic) { this.votedMusic = votedMusic; }
}