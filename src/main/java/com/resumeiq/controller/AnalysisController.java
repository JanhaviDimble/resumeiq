package com.resumeiq.controller;

import com.resumeiq.model.Analysis;
import com.resumeiq.model.Resume;
import com.resumeiq.model.User;
import com.resumeiq.repository.ResumeRepository;
import com.resumeiq.repository.UserRepository;
import com.resumeiq.service.AnalysisService;
import com.resumeiq.service.PdfParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnalysisController {

    private final AnalysisService analysisService;
    private final PdfParserService pdfParserService;
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    @PostMapping("/analyze")
    public ResponseEntity<?> analyze(
            @RequestParam("resumeId") Long resumeId,
            @RequestParam("userId") Long userId,
            @RequestBody String jobDescription) {
        try {
            Resume resume = resumeRepository.findById(resumeId)
                    .orElseThrow(() -> new RuntimeException("Resume not found"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String resumeText = pdfParserService.extractText(resume.getFilePath());

            Analysis analysis = analysisService.analyzeResume(
                    resumeText, jobDescription, resume, user);

            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Analysis>> getUserAnalyses(@PathVariable Long userId) {
        return ResponseEntity.ok(analysisService.getUserAnalyses(userId));
    }
}