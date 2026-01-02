package com.sports.rest;

import com.sports.cache.data.OutputData;
import com.sports.cache.util.CacheListObject;
import com.sports.cache.util.CacheUtil;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.entity.key.CompSeasonKey;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public abstract class OutputCreator {
    private final Integer clientId;
    private final HttpServletResponse response;
    private CompSeasonKey compSeasonKey;

    protected abstract String createOutputFromOutputData(OutputData outputData);

    protected abstract String createOutputFromCacheList(List<CacheListObject> cacheList);

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

    public String createOutputForCacheList() throws IOException {
        if (clientId != null && RestClientUtil.clientIsAdmin(clientId))
            return createOutputFromCacheList(CacheUtil.getCacheList());

        return sendError(HttpServletResponse.SC_UNAUTHORIZED);
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

        return sendError(errorCode);
    }

    private String sendError(int errorCode) throws IOException {
        response.sendError(errorCode);
        return null;
    }

    private boolean checkClientPermit() {
        return clientId != null && (compSeasonKey == null ||
                RestClientUtil.clientHasCompSeasonPermit(clientId, compSeasonKey));
    }
}
