
# MuSuBi（結び）

> **検索しない音楽体験**
> 共感でつながる音楽コミュニティ

---

##  概要

**MuSuBi（結び）**は、
再生数・ランキング・検索中心の音楽体験ではなく、
**「共感（いいね）」を通じて音楽と出会うこと**を目的とした
音楽共有Webアプリケーションです。

ユーザーは

* YouTubeの音楽
* 自身で制作・演奏・カバーした音源

を投稿し、
コメントや共感を通じて
音楽を介した自然なつながりを生み出します。

---

##  開発背景（Why）

私は音楽を専攻しており、
現在の音楽市場における

* 再生数・ランキング至上主義
* ストリーミング数操作などの問題
* 「検索ありき」の受動的な音楽体験

に違和感を持っていました。

> **音楽は探すものだけではなく、
> 偶然出会い、共感する体験でも良いのではないか**

この考えを形にするため、
MuSuBiでは**あえて検索機能を実装しない**という設計判断を行いました。

---

## 検索機能を実装しなかった理由

一般的なWebサービスでは検索機能が必須ですが、
MuSuBiでは以下を優先しました。

* すでに知っている音楽を探すこと
* 他人の共感を通じて新しい音楽に出会うこと

その代替として

* 最新投稿一覧
* 共感数を基準としたランキング
* ランダム再生機能

を中心に設計しています。

これは機能不足ではなく、
**サービスの思想を優先した意図的な設計**です。

---

##  サービス名の意味

**MuSuBi（結び）**は、

* Music
* 結び（人と人をつなぐ）

を組み合わせた造語です。

音楽を通じて人が自然につながる場所、
その意味を込めて名付けました。

---

## 主な機能

###  音楽投稿

* YouTube URL投稿
* 音源ファイルアップロード
* YouTube動画IDを抽出し、サムネイル自動生成

###  共感（いいね）

* ログインユーザーのみ可能
* 単なる数値ではなく「誰が共感したか」を保持

### 共感ベースランキング

* 再生数ではなく共感数を基準
* 「人気」より「推薦」に近い概念

### コメント機能

* 音楽ごとのコメント投稿
* 音楽削除時にコメントも連動削除

###  権限管理

* USER / ADMIN ロール分離
* 管理者のみ削除操作が可能

---

##  設計・実装のポイント（コード抜粋）

### ① 共感を「数」ではなく「関係」として設計

共感（いいね）を
**Music と User の多対多関係**としてモデル化しました。

```java
@ManyToMany
Set<SiteUser> voter;
```

これにより
「何人にいいねされたか」ではなく
**「誰に共感された音楽か」**を表現できます。

将来的な拡張（推薦・履歴表示）も考慮した設計です。

---

### ② 共感数を基準としたランキング取得

サービスの思想を
データ取得ロジックにも反映しています。

```java
@Query(
  value = "SELECT * FROM music ORDER BY " +
          "(SELECT COUNT(*) FROM music_voter WHERE music_id = music.id) DESC LIMIT 10",
  nativeQuery = true
)
List<Music> findTopRanking();
```

---

### ③ 検索の代替としてのランダム再生

検索を行わず、
偶然の出会いを生むためのランダム再生機能です。

```java
public Long getRandomMusicId() {
    List<Music> musicList = this.musicRepository.findAll();
    if (musicList.isEmpty()) {
        return null;
    }
    Random random = new Random();
    return musicList.get(random.nextInt(musicList.size())).getId();
}
```

---

### ④ 音楽削除時のデータ整合性

音楽削除時に
関連コメントも自動削除されるよう設計しています。

```java
@OneToMany(mappedBy = "music", cascade = CascadeType.REMOVE)
private List<Answer> answerList;
```

---

### ⑤ 実運用を想定したセキュリティ設計

誤操作や悪意ある操作を防ぐため、
削除処理は管理者権限のみに制限しています。

```java
.requestMatchers("/admin/**").hasRole("ADMIN")
.requestMatchers("/music/delete/**").hasRole("ADMIN")
```

---

## 🛠 使用技術

### Backend

* Java 21
* Spring Boot 3.2
* Spring Security
* Spring Data JPA（Hibernate）

### Frontend

* Thymeleaf
* HTML / CSS / JavaScript
  （操作ボタンを最小限にしたUI設計）

### Database

* PostgreSQL

### その他

* Gradle
* Lombok

---

## 学んだこと・工夫した点

* Spring Securityによる認可設計
* 共感機能のドメイン設計
* 「なぜその機能を入れたか／入れなかったか」を説明できる設計

技術だけでなく、
**サービス思想とコードの一貫性**を意識しました。

---

##  今後の改善予定

* 例外処理（Global Exception Handler）
* ファイルアップロードのセキュリティ強化
* テストコード追加
* REST API構成への拡張検討

---

##  開発者

* 名前：**チョ・ヒョンファン（조현황）**
* 専攻：音楽
* GitHub：本リポジトリ参照

---

##  このプロジェクトで伝えたいこと

* 技術の前に「なぜ作るか」を考える姿勢
* サービス思想を設計・コードに落とし込む力
* 新卒・未経験でも運用を意識した開発ができること

---

##  リポジトリ

👉 [https://github.com/whgusghkd777-debug/MuSuBiMusic](https://github.com/whgusghkd777-debug/MuSuBiMusic)

---

