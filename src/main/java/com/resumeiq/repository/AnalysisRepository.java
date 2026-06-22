package com.resumeiq.repository;

import com.resumeiq.model.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
    List<Analysis> findByUserId(Long userId);
}