package com.resumeiq.controller;

import com.resumeiq.model.Resume;
import com.resumeiq.model.User;
import com.resumeiq.repository.UserRepository;
import com.resumeiq.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ResumeController {

    private final ResumeService resumeService;
    private final UserRepository userRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Resume resume = resumeService.saveResume(file, user);
            return ResponseEntity.ok(resume);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Resume>> getUserResumes(@PathVariable Long userId) {
        return ResponseEntity.ok(resumeService.getUserResumes(userId));
    }
}