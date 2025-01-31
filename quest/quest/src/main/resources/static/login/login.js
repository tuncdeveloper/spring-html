const loginForm = document.getElementById('loginForm');

if (loginForm) {
    loginForm.addEventListener('submit', async function (e) {
        e.preventDefault(); // Sayfanın yeniden yüklenmesini önle

        // Kullanıcı adı ve şifreyi al
        const userName = document.getElementById('userName').value.trim();
        const password = document.getElementById('password').value.trim();

        // Boş girişleri kontrol et
        if (!userName || !password) {
            alert("Kullanıcı adı ve şifre boş olamaz!");
            return;
        }

        try {
            // ✅ Login API'sine istek gönder
            const response = await fetch('http://localhost:8080/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ userName, password }),
                credentials: 'include' // ✅ Cookie'yi almak için eklenmeli
            });

            // ✅ Yanıtın başarılı olup olmadığını kontrol et
            if (!response.ok) {
                const errorMessage = await response.text(); // Hata mesajını backend'den al
                throw new Error(errorMessage || "Giriş başarısız!");
            }

            const userData = await response.json(); // Backend'den JSON yanıt al
            console.log('Login Başarılı:', userData);

            // ✅ Kullanıcı bilgilerini cookie'ye kaydet
            document.cookie = `userId=${userData.id}; path=/;`;
            document.cookie = `userName=${userData.userName}; path=/;`;

            alert('Giriş başarılı!');

            // ✅ Kullanıcıyı yönlendir
            window.location.href = "http://localhost:8080/user/user.html";
        } catch (error) {
            // Hata mesajını göster
            const errorMessageElement = document.getElementById('error-message');
            if (errorMessageElement) {
                errorMessageElement.textContent = error.message;
            } else {
                alert("Hata: " + error.message);
            }
        }
    });
} else {
    console.error("Hata: loginForm bulunamadı!");
}
