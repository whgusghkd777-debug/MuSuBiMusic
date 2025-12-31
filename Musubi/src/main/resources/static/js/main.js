// 예시: 삭제 버튼 클릭 시
async function deleteMusic(id) {
    if(!confirm("本当に削除しますか？")) return;
    
    const response = await fetch(`/api/music/delete/${id}`, {
        method: 'DELETE',
        headers: {
            [csrfHeader]: csrfToken // 보안용 토큰 필수!
        }
    });

    if (response.ok) {
        alert("削除に成功しました。");
        location.reload();
    } else {
        alert("権限がありません。(admin 계정으로 로그인하세요)");
    }
}