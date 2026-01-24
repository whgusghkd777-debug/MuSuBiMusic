
async function deleteMusic(id) {
    if(!confirm("本当に削除しますか？")) return;
    
    const response = await fetch(`/api/music/delete/${id}`, {
        method: 'DELETE',
        headers: {
            [csrfHeader]: csrfToken 
        }
    });

    if (response.ok) {
        alert("削除に成功しました。");
        location.reload();
    } else {
        alert("権限がありません。(admin しかできません。)");
    }
}
