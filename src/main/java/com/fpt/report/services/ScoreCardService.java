package com.fpt.report.services;

import com.fpt.report.models.ScoreCard;
import com.fpt.report.repositories.ScoreCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreCardService {
    private final ScoreCardRepository scoreCardRepository;

    public List<ScoreCard> getAllScoreCards() {
        return scoreCardRepository.findAll();
    }
    public List<ScoreCard> getScoreCardsByStudentId(Integer rollNumber) {
        return scoreCardRepository.findByStudent_RollNumber(rollNumber);
    }
}
