package com.sports.rest;

import com.sports.cache.data.OutputData;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.entity.key.CompSeasonKey;

public abstract class OutputCreator {
    private final Integer clientId;
    private CompSeasonKey compSeasonKey;

    protected abstract String getEmptyResponse();

    protected abstract String createOutputFromOutputData(OutputData outputData);

    public OutputCreator(Integer clientId) {
        this.clientId = clientId;
    }

    public OutputCreator(Integer clientId, CompSeasonKey compSeasonKey) {
        this.clientId = clientId;
        this.compSeasonKey = compSeasonKey;
    }

    public String createOutput(OutputData outputData) {
        return createOutputWithCheck(outputData, checkClientPermit());
    }

    public String createOutputForAdmin(OutputData outputData) {
        return createOutputWithCheck(outputData, clientId != null && RestClientUtil.clientIsAdmin(clientId));
    }

    private String createOutputWithCheck(OutputData outputData, boolean check) {
        if (check) {
            outputData = DataFragmentUtil.getFilledOutputData(outputData, null);

            if (outputData.isValidOutput())
                return createOutputFromOutputData(outputData);
        }

        return getEmptyResponse();
    }

    private boolean checkClientPermit() {
        return clientId != null && (compSeasonKey == null ||
                RestClientUtil.clientHasCompSeasonPermit(clientId, compSeasonKey));
    }
}
