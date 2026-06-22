package com.resumeiq.service;

import com.resumeiq.model.Resume;
import com.resumeiq.model.User;
import com.resumeiq.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public Resume saveResume(MultipartFile file, User user) throws IOException {
    	String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String filePath = uploadDir + file.getOriginalFilename();
        file.transferTo(new File(filePath));

        Resume resume = new Resume();
        resume.setUser(user);
        resume.setFileName(file.getOriginalFilename());
        resume.setFilePath(filePath);

        return resumeRepository.save(resume);
    }

    public List<Resume> getUserResumes(Long userId) {
        return resumeRepository.findByUserId(userId);
    }
}