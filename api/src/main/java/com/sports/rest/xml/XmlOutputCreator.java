package com.sports.rest.xml;

import com.sports.cache.data.OutputData;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.key.CompSeasonKey;
import com.sports.rest.OutputCreator;

public class XmlOutputCreator extends OutputCreator {
    public XmlOutputCreator(Integer clientId) {
        super(clientId);
    }

    public XmlOutputCreator(Integer clientId, CompSeasonKey compSeasonKey) {
        super(clientId, compSeasonKey);
    }

    @Override
    protected String getEmptyResponse() {
        return XmlUtil.getEmptyResponse();
    }

    @Override
    protected String createOutputFromOutputData(OutputData outputData) {
        return outputData.toXML();
    }
}
