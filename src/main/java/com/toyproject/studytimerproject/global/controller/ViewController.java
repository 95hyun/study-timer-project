package com.toyproject.studytimerproject.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    /**
     * 로그인 페이지를 렌더링하는 핸들러 메서드
     * @return login 페이지의 이름을 반환
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/")
    public String showIndexPage() {
        return "index";
    }

    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";
    }
}
