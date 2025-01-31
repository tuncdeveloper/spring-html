const apiUrl = "http://localhost:8080"; // Backend API adresi
let userId = "";
let userName = "";

// ✅ Daha sağlam cookie okuma fonksiyonu
function getCookie(name) {
    return document.cookie
        .split('; ')
        .find(row => row.startsWith(name + '='))
        ?.split('=')[1];
}

document.addEventListener("DOMContentLoaded", function () {
    // ✅ Cookie'den kullanıcı bilgilerini al
    userId = getCookie("userId");
    userName = getCookie("userName");

    console.log('User ID from Cookie:', userId);
    console.log('User Name from Cookie:', userName);

    // ❌ Eğer cookie'de userId yoksa, giriş sayfasına yönlendir
    if (!userId) {
        console.error("Kullanıcı ID bulunamadı! Yeniden giriş yapmanız gerekiyor.");
        window.location.href = "login.html"; // Giriş sayfasına yönlendir
        return;
    }

    // ✅ Kullanıcı bilgilerini yükle
    loadUserInfo(userId);
    loadUserPosts(userId);  // Kullanıcının gönderilerini yükle
    loadAllPosts();   // Herkesin gönderilerini yükle

    // ✅ logoutBtn varsa event listener ekle
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", logout);
    }
});

// ✅ Kullanıcı bilgilerini API’den çek
function loadUserInfo(userId) {
    fetch(`${apiUrl}/users/oneUser/${userId}`)
        .then(response => {
            console.log('Response Status:', response.status); // Yanıtın statüsünü kontrol et
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(userData => {
            console.log('User Data:', userData); // API'den gelen veriyi kontrol et
            userName = userData.userName; // ✅ Kullanıcı adını güncelle

            // ✅ Kullanıcı adını ekrana yaz (ID varsa)
            const usernameElement = document.getElementById("username");
            if (usernameElement) {
                usernameElement.innerText = userName;
            }
        })
        .catch(error => {
            console.error("Kullanıcı verisi alınamadı:", error.message);
            const usernameElement = document.getElementById("username");
            if (usernameElement) {
                usernameElement.innerText = "Kullanıcı bilgisi yüklenemedi"; // Hata durumunda kullanıcıya bilgi ver
            }
        });
}


function loadComments(postId) {
    fetch(`${apiUrl}/comments/allComment?postId=${postId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            if (!Array.isArray(data)) {
                throw new Error("Yanıt bir dizi değil!");
            }
            return data;
        })
        .then(comments => {
            const commentDiv = document.getElementById(`comments-${postId}`);
            commentDiv.innerHTML = ""; // Önceki yorumları temizle

            comments.forEach(comment => {
                const commentElement = document.createElement("p");
                commentElement.innerHTML = `<strong>${comment.userName}:</strong> ${comment.text}`;
                commentDiv.appendChild(commentElement);
            });
        })
        .catch(error => {
            console.error("Yorumlar yüklenirken hata oluştu:", error);
        });
}

// Diğer fonksiyonlar (loadUserPosts, loadAllPosts, addComment, vs.) aynı kalacak.


function loadUserPosts(userId) {
    fetch(`http://localhost:8080/posts/allPostsByUser?userId=${userId}`)
        .then(response => response.json())
        .then(posts => {
            const postList = document.getElementById("user-post-list");
            postList.innerHTML = ""; // Listeyi temizle

            posts.forEach(post => {
                const postElement = document.createElement("div");
                postElement.classList.add("post");
                postElement.innerHTML = `
                    <h3>${post.title}</h3>
                    <p>${post.text}</p>
                    <button class="like-btn" onclick="likePost(${post.id}, ${userId})">❤️ Beğen</button>
                    <button onclick="deletePost(${post.id})">Sil</button>
                    <button onclick="editPost(${post.id}, '${post.title}', '${post.text}')">Düzenle</button>
                    <div>
                        <input type="text" id="comment-${post.id}" placeholder="Yorum yap">
                        <button onclick="addComment(${post.id}, ${userId})">Gönder</button>
                    </div>
                    <div id="comments-${post.id}"></div>
                `;
                postList.appendChild(postElement);
                loadComments(post.id, userId); // Yorumları yükle
            });
        })
        .catch(error => {
            console.error("Gönderiler yüklenirken hata oluştu:", error);
        });
}

function loadAllPosts() {
    fetch(`${apiUrl}/posts/allPosts`)
        .then(response => response.json())
        .then(posts => {
            const postList = document.getElementById("all-post-list");
            postList.innerHTML = ""; // Listeyi temizle

            posts.forEach(post => {
                const postElement = document.createElement("div");
                postElement.classList.add("post");
                postElement.innerHTML = `
                    <h3>${post.title}</h3>
                    <p>${post.text}</p>
                    <button class="like-btn" onclick="likePost(${post.id})">❤️ Beğen</button>
                    <div>
                        <input type="text" id="comment-${post.id}" placeholder="Yorum yap">
                        <button onclick="addComment(${post.id})">Gönder</button>
                    </div>
                    <div id="comments-${post.id}"></div>
                `;
                postList.appendChild(postElement);
                loadComments(post.id, userId); // Yorumları yükle
            });
        })
        .catch(error => {
            console.error("Gönderiler yüklenirken hata oluştu:", error);
        });
}

function deletePost(postId) {
    fetch(`${apiUrl}/posts/deletePost/${postId}`, { method: "DELETE" })
        .then(() => {
            alert("Gönderi silindi!");
            loadUserPosts();
            loadAllPosts();
        })
        .catch(error => {
            console.error("Gönderi silinirken hata oluştu:", error);
        });
}

function editPost(postId, oldTitle, oldText) {
    const newTitle = prompt("Yeni Başlık", oldTitle);
    const newText = prompt("Yeni İçerik", oldText);

    if (newTitle && newText) {
        fetch(`${apiUrl}/posts/updatePost/${postId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ title: newTitle, text: newText })
        })
        .then(() => {
            alert("Gönderi güncellendi!");
            loadUserPosts();
            loadAllPosts();
        })
        .catch(error => {
            console.error("Gönderi güncellenirken hata oluştu:", error);
        });
    }
}

function likePost(postId) {
    fetch(`${apiUrl}/likes/addLike`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userId: userId, postId: postId })
    })
    .then(() => alert("Gönderi beğenildi!"))
    .catch(error => {
        console.error("Gönderi beğenilirken hata oluştu:", error);
    });
}

function addComment(postId) {
    const commentText = document.getElementById(`comment-${postId}`).value;

    fetch(`${apiUrl}/comments/addComment`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ userId: userId, postId: postId, text: commentText })
    })
    .then(() => {
        alert("Yorum eklendi!");
        loadComments(postId); // Yorumları yeniden yükle
        document.getElementById(`comment-${postId}`).value = ""; // Yorum kutusunu temizle
    })
    .catch(error => {
        console.error("Yorum eklenirken hata oluştu:", error);
    });
}

function updateUser() {
    const newUserName = prompt("Yeni kullanıcı adı girin:");
    const newPassword = prompt("Yeni şifre girin:");

    if (newUserName && newPassword) {
        fetch(`${apiUrl}/users/updateUser/${userId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ userName: newUserName, password: newPassword })
        })
        .then(response => response.json())
        .then(updatedUser => {
            alert("Kullanıcı bilgileri güncellendi!");
            userName = updatedUser.userName; // userName'i güncelle
            document.getElementById("username").innerText = userName;
        })
        .catch(error => {
            console.error("Kullanıcı bilgileri güncellenirken hata oluştu:", error);
        });
    } else {
        alert("Kullanıcı adı ve şifre zorunludur!");
    }
}



function addPost() {
    const title = document.getElementById('postTitle').value;  // Başlık
    const text = document.getElementById('postText').value;  // İçerik

    if (title && text) {
        fetch(`${apiUrl}/posts/addPost`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                userId: userId,
                title: title,
                text: text,
                userName: userName // userName değişkenini kullanın
            })
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(error => {
                    throw new Error(error.message || "Gönderi eklenirken bir hata oluştu!");
                });
            }
            return response.json();
        })
        .then(newPost => {
            alert("Gönderi başarıyla eklendi!");
            loadUserPosts();
            loadAllPosts();
            document.getElementById('postTitle').value = '';
            document.getElementById('postText').value = '';
        })
        .catch(error => {
            alert(error.message); // Backend'den gelen hata mesajını göster
            console.error("Hata:", error);
        });
    } else {
        alert("Lütfen başlık ve içerik girin!");
    }
}

function logout() {
    alert("Çıkış yapıldı!");
    localStorage.removeItem("userId");
    window.location.href = "http://localhost:8080/users/login"; // Giriş sayfasına yönlendirme
}