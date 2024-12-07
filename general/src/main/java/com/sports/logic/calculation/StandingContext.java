package com.sports.logic.calculation;

import com.sports.entity.Participant;

import java.time.LocalDateTime;
import java.util.List;

public record StandingContext<P extends Participant>(LocalDateTime date, List<P> standing) {}
