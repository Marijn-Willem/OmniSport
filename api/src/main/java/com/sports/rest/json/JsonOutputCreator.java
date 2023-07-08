package com.sports.rest.json;

import com.sports.cache.data.OutputData;
import com.sports.entity.key.CompSeasonKey;
import com.sports.rest.OutputCreator;
import jakarta.servlet.http.HttpServletResponse;

public class JsonOutputCreator extends OutputCreator {
    public JsonOutputCreator(Integer clientId, HttpServletResponse response) {
        super(clientId, response);
    }

    public JsonOutputCreator(Integer clientId, HttpServletResponse response, CompSeasonKey compSeasonKey) {
        super(clientId, response, compSeasonKey);
    }

    @Override
    protected String createOutputFromOutputData(OutputData outputData) {
        return outputData.toJson();
    }
}
