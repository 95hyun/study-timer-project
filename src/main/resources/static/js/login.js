// login.js
document.addEventListener("DOMContentLoaded", function() {
    const loginForm = document.getElementById("loginForm");

    loginForm.addEventListener("submit", function(e) {
        e.preventDefault();

        const formData = {
            username: document.getElementById("username").value,
            password: document.getElementById("password").value
        };

        fetch("/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(formData)
        })
            .then(response => {
                if (response.ok) {
                    // 응답 헤더에서 토큰을 추출하여 로컬 스토리지에 저장
                    const accessToken = response.headers.get("AccessToken"); // ACCESS_TOKEN_HEADER는 백엔드에서 설정한 헤더의 이름으로 변경해야 합니다.
                    const refreshToken = response.headers.get("RefreshToken"); // REFRESH_TOKEN_HEADER는 백엔드에서 설정한 헤더의 이름으로 변경해야 합니다.

                    if (accessToken && refreshToken) {
                        localStorage.setItem('accessToken', accessToken);
                        localStorage.setItem('refreshToken', refreshToken);
                        window.location.href = "/"; // 로그인 성공 시 index 페이지로 리다이렉트
                    } else {
                        throw new Error('Token not found in response');
                    }
                } else {
                    throw new Error('Login failed');
                }
            })
            .catch((error) => {
                alert("로그인에 실패했습니다"); // 로그인 실패 시 경고창
            });
    });
});
