package com.resumeiq.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "analyses")
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    private Integer matchScore;
    private Integer qualityScore;

    @Column(columnDefinition = "TEXT")
    private String missingKeywords;

    @Column(columnDefinition = "TEXT")
    private String suggestions;

    private LocalDateTime createdAt = LocalDateTime.now();
}