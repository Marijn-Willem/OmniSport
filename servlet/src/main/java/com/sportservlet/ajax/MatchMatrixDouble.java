package com.sportservlet.ajax;

import com.sports.entity.Double;
import com.sports.logic.factory.CompSeasonDoubleFactory;

public class MatchMatrixDouble extends MatchMatrix<Double> {
    @Override
    protected CompSeasonDoubleFactory getFactory() {
        return new CompSeasonDoubleFactory();
    }
}
