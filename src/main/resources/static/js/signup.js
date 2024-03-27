document.addEventListener("DOMContentLoaded", function() {
    const signupForm = document.getElementById("signupForm");

    signupForm.addEventListener("submit", function(e) {
        e.preventDefault();

        const formData = {
            username: document.getElementById("username").value,
            password: document.getElementById("password").value,
            nickname: document.getElementById("nickname").value
        };

        fetch("/api/auth/signup", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(formData)
        })
            .then(response => {
                if (response.ok) {
                    window.location.href = "/login";
                } else {
                    throw new Error('Signup failed');
                }
            })
            .catch(error => {
                alert("회원가입에 실패했습니다. 다시 시도해주세요.");
            });
    });
});
