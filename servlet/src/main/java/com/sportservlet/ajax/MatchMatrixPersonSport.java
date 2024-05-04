package com.sportservlet.ajax;

import com.sports.entity.PersonSport;
import com.sports.logic.factory.CompSeasonPersonSportFactory;

public class MatchMatrixPersonSport extends MatchMatrix<PersonSport> {
    @Override
    protected CompSeasonPersonSportFactory getFactory() {
        return new CompSeasonPersonSportFactory();
    }
}
