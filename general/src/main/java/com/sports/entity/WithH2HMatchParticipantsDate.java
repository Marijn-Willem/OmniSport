package com.sports.entity;

import java.time.LocalDateTime;

public interface WithH2HMatchParticipantsDate {
    Integer getParticipant1Id();
    Integer getParticipant2Id();
    LocalDateTime getDate();
}
