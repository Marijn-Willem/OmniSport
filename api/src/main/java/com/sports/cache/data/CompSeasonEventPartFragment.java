package com.sports.cache.data;

import com.sports.cache.key.CacheKey;
import com.sports.cache.key.CompSeasonEventPartKey;
import com.sports.cache.util.DataFragmentUtil;
import com.sports.cache.util.DescribedEntityUtil;
import com.sports.cache.util.JsonUtil;
import com.sports.cache.util.XmlUtil;
import com.sports.entity.CompSeasonEventPart;
import com.sports.entity.key.CompSeasonEventKey;
import com.sports.entity.key.CompSeasonKey;
import com.sports.entity.manager.CompSeasonEventPartManager;
import com.sports.entity.manager.EventPartLocationManager;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompSeasonEventPartFragment extends WritableFragment {
    private final int competitionId;
    private final int seasonId;
    private final int compSeasonEventId;
    private final int compSeasonEventPartId;
    private final int clientId;

    private String description;
    private int order;
    private Integer stage;
    private LocalDateTime date;
    private EventPartNameFragment eventPartNameFragment;
    private final List<EventPartLocationFragment> eventPartLocationFragments = new ArrayList<>();

    public CompSeasonEventPartFragment(int competitionId, int seasonId, int compSeasonEventId,
                                       int compSeasonEventPartId, int clientId) {
        this.competitionId = competitionId;
        this.seasonId = seasonId;
        this.compSeasonEventId = compSeasonEventId;
        this.compSeasonEventPartId = compSeasonEventPartId;
        this.clientId = clientId;
    }

    @Override
    public CacheKey getCacheKey() {
        return new CompSeasonEventPartKey(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId);
    }

    @Override
    void fill(Statement stat) throws SQLException {
        com.sports.entity.key.CompSeasonEventPartKey compSeasonEventPartKey =
                new com.sports.entity.key.CompSeasonEventPartKey(
                        new CompSeasonEventKey(
                                new CompSeasonKey(competitionId, seasonId), compSeasonEventId
                        ), compSeasonEventPartId
                );

        CompSeasonEventPart compSeasonEventPart = new CompSeasonEventPartManager(stat)
                .getCompSeasonEventPart(compSeasonEventPartKey);

        description = new DescribedEntityUtil(clientId, getCacheDataKey(), stat)
                .getCompSeasonEventPartString(compSeasonEventPart);
        order = compSeasonEventPart.getOrder();
        stage = compSeasonEventPart.getStage();
        date = compSeasonEventPart.getDate();

        if (compSeasonEventPart.getEventPartNameId() != null)
            eventPartNameFragment = DataFragmentUtil.getFilledDataFragment(
                    new EventPartNameFragment(compSeasonEventPart.getEventPartNameId(), clientId),
                    getCacheDataKey(), stat);

        eventPartLocationFragments.addAll(
                new EventPartLocationManager(stat).getEventPartLocations(compSeasonEventPartKey).stream().map(x ->
                new EventPartLocationFragment(competitionId, seasonId, compSeasonEventId, compSeasonEventPartId,
                        x.getEventPartLocationId())).toList());

        DataFragmentUtil.fillDataFragments(eventPartLocationFragments, getCacheDataKey());
    }

    @Override
    public String toXML() {
        return XmlUtil.getTag("compSeasonEventPartId", compSeasonEventPartId) +
                XmlUtil.getTag("description", description) +
                XmlUtil.getTag("order", order) +
                XmlUtil.getTag("stage", stage) +
                XmlUtil.getTag("date", date) +
                XmlUtil.getNullableFragmentAsTag("eventPartName", eventPartNameFragment) +
                XmlUtil.getEnclosedXmlList("eventPartLocationList", "eventPartLocation",
                        eventPartLocationFragments);
    }

    @Override
    public String toJson() {
        return JsonUtil.getEntry("compSeasonEventPartId", compSeasonEventPartId) + "," +
                JsonUtil.getEntry("description", description) + "," +
                JsonUtil.getEntry("order", order) + "," +
                JsonUtil.getEntry("stage", stage) + "," +
                JsonUtil.getEntry("date", date) + "," +
                JsonUtil.getNullableFragmentAsEntry("eventPartName", eventPartNameFragment) + "," +
                JsonUtil.getArray("eventPartLocationList", eventPartLocationFragments);
    }
}
