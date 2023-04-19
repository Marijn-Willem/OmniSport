package com.sports.rest.json;

import com.sports.cache.data.OutputData;
import com.sports.cache.util.JsonUtil;
import com.sports.entity.key.CompSeasonKey;
import com.sports.rest.OutputCreator;

public class JsonOutputCreator extends OutputCreator {
    public JsonOutputCreator(Integer clientId) {
        super(clientId);
    }

    public JsonOutputCreator(Integer clientId, CompSeasonKey compSeasonKey) {
        super(clientId, compSeasonKey);
    }

    @Override
    protected String getEmptyResponse() {
        return JsonUtil.getEmptyResponse();
    }

    @Override
    protected String createOutputFromOutputData(OutputData outputData) {
        return outputData.toJson();
    }
}
