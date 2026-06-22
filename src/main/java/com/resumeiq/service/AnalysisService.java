package com.resumeiq.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumeiq.model.Analysis;
import com.resumeiq.model.Resume;
import com.resumeiq.model.User;
import com.resumeiq.repository.AnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final AnalysisRepository analysisRepository;
    private final ClaudeApiService claudeApiService;

    public Analysis analyzeResume(String resumeText, String jobDescription,
                                   Resume resume, User user) throws Exception {

        String aiResponse = claudeApiService.analyzeResume(resumeText, jobDescription);

        System.out.println("AI Raw Response: " + aiResponse);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(aiResponse);

     // Groq response parsing
        String textContent = root.path("choices")
                                 .get(0)
                                 .path("message")
                                 .path("content")
                                 .asText();
        

        System.out.println("Gemini Text: " + textContent);

        int start = textContent.indexOf("{");
        int end = textContent.lastIndexOf("}") + 1;
        String jsonStr = textContent.substring(start, end);

        JsonNode result = mapper.readTree(jsonStr);

        Analysis analysis = new Analysis();
        analysis.setUser(user);
        analysis.setResume(resume);
        analysis.setJobDescription(jobDescription);
        analysis.setMatchScore(result.path("matchScore").asInt());
        analysis.setQualityScore(result.path("qualityScore").asInt());
        analysis.setMissingKeywords(result.path("missingKeywords").toString());
        analysis.setSuggestions(result.path("suggestions").toString());

        return analysisRepository.save(analysis);
    }

    public List<Analysis> getUserAnalyses(Long userId) {
        return analysisRepository.findByUserId(userId);
    }
}