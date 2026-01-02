package com.sports.rest.xml;

import com.sports.cache.data.OutputData;
import com.sports.cache.util.CacheListObject;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.key.CompSeasonKey;
import com.sports.rest.OutputCreator;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public class XmlOutputCreator extends OutputCreator {
    public XmlOutputCreator(Integer clientId, HttpServletResponse response) {
        super(clientId, response);
    }

    public XmlOutputCreator(Integer clientId, HttpServletResponse response, CompSeasonKey compSeasonKey) {
        super(clientId, response, compSeasonKey);
    }

    @Override
    protected String createOutputFromOutputData(OutputData outputData) {
        return outputData.toXML();
    }

    @Override
    protected String createOutputFromCacheList(List<CacheListObject> cacheList) {
        return XmlUtil.getTopLevelXmlListFromCacheList(cacheList);
    }
}
