package com.fpt.report.models;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class ScoreCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String subjectName;
    private double totalMarks;
    private double marksObtained;

    // Many-to-One relationship with Student
    @ManyToOne
    @JoinColumn(name = "roll_number", nullable = false)
    private Student student;
}
