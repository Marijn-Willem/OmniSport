package com.sports.rest;

import com.sports.cache.data.OutputData;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.entity.key.CompSeasonKey;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public abstract class OutputCreator {
    private final Integer clientId;
    private final HttpServletResponse response;
    private CompSeasonKey compSeasonKey;

    protected abstract String createOutputFromOutputData(OutputData outputData);

    public OutputCreator(Integer clientId, HttpServletResponse response) {
        this.clientId = clientId;
        this.response = response;
    }

    public OutputCreator(Integer clientId, HttpServletResponse response, CompSeasonKey compSeasonKey) {
        this(clientId, response);
        this.compSeasonKey = compSeasonKey;
    }

    public String createOutput(OutputData outputData) throws IOException {
        return createOutputWithPermission(outputData, checkClientPermit());
    }

    public String createOutputForAdmin(OutputData outputData) throws IOException {
        return createOutputWithPermission(outputData, clientId != null && RestClientUtil.clientIsAdmin(clientId));
    }

    private String createOutputWithPermission(OutputData outputData, boolean permissionCheck) throws IOException {
        int errorCode = HttpServletResponse.SC_UNAUTHORIZED;

        if (permissionCheck) {
            outputData = DataFragmentUtil.getFilledOutputData(outputData, null);

            if (outputData.isValidOutput())
                return createOutputFromOutputData(outputData);
            else
                errorCode = HttpServletResponse.SC_NOT_FOUND;
        }

        response.sendError(errorCode);
        return null;
    }

    private boolean checkClientPermit() {
        return clientId != null && (compSeasonKey == null ||
                RestClientUtil.clientHasCompSeasonPermit(clientId, compSeasonKey));
    }
}
