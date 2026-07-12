package com.sentimentanalysis.controller;

import com.sentimentanalysis.dto.DashboardStats;
import com.sentimentanalysis.dto.ReviewResponse;
import com.sentimentanalysis.dto.UserDto;
import com.sentimentanalysis.service.DashboardService;
import com.sentimentanalysis.service.ReviewService;
import com.sentimentanalysis.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PageController {

    private final DashboardService dashboardService;
    private final ReviewService reviewService;
    private final UserService userService;

    public PageController(DashboardService dashboardService, ReviewService reviewService, UserService userService) {
        this.dashboardService = dashboardService;
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        DashboardStats stats = dashboardService.loadDashboardStats();
        model.addAttribute("stats", stats);
        return "dashboard";
    }

    @GetMapping("/submit-review")
    public String submitReview() {
        return "submit-review";
    }

    @GetMapping("/review-history")
    public String reviewHistory(Authentication authentication, Model model) {
        List<ReviewResponse> reviews = reviewService.getReviewsByUser(authentication.getName());
        model.addAttribute("reviews", reviews);
        return "review-history";
    }

    @GetMapping("/review-details/{id}")
    public String reviewDetails(@PathVariable @NonNull Long id, Model model) {
        ReviewResponse review = reviewService.getReviewById(id);
        model.addAttribute("review", review);
        return "review-details";
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        UserDto user = userService.getByUsername(authentication.getName());
        model.addAttribute("currentUser", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(Authentication authentication,
                                @RequestParam String fullName,
                                @RequestParam String username,
                                @RequestParam String email,
                                Model model) {
        UserDto dto = new UserDto();
        dto.setFullName(fullName);
        dto.setUsername(username);
        dto.setEmail(email);
        userService.updateProfile(authentication.getName(), dto);
        model.addAttribute("currentUser", userService.getByUsername(dto.getUsername()));
        model.addAttribute("updateSuccess", true);
        return "profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(Authentication authentication,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("passwordError", "New passwords do not match");
            model.addAttribute("currentUser", userService.getByUsername(authentication.getName()));
            return "profile";
        }
        userService.changePassword(authentication.getName(), currentPassword, newPassword);
        model.addAttribute("passwordSuccess", true);
        model.addAttribute("currentUser", userService.getByUsername(authentication.getName()));
        return "profile";
    }

    @GetMapping("/settings")
    public String settings(Authentication authentication, Model model) {
        model.addAttribute("currentUser", userService.getByUsername(authentication.getName()));
        return "settings";
    }

    @GetMapping("/admin")
    public String adminPanel(Model model) {
        DashboardStats stats = dashboardService.loadDashboardStats();
        model.addAttribute("stats", stats);
        return "admin";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/login";
    }
}
