package com.sports.cache.data;

import com.sports.cache.util.XmlUtil;
import com.sports.cache.util.YamlUtil;
import com.sports.calc.darts.stat.StatObject;

public abstract class DartsStatsFragment extends WritableFragment {
    protected String person1XML;
    protected String person2XML;
    protected String person1Json;
    protected String person2Json;
    protected String person1Yaml;
    protected String person2Yaml;

    public DartsStatsFragment(int nestingLevel, boolean isInList) {
        super(nestingLevel, isInList);
    }

    protected void fillStatOutputFromStatObject(StatObject statObject) {
        int nestingLevelNested = YamlUtil.getLevelForNestedFragment(nestingLevel);

        person1XML = statObject.getPerson1StatsXmlTags();
        person2XML = statObject.getPerson2StatsXmlTags();
        person1Json = statObject.getPerson1StatsJsonEntries();
        person2Json = statObject.getPerson2StatsJsonEntries();
        person1Yaml = statObject.getPerson1StatsYamlEntries(nestingLevelNested);
        person2Yaml = statObject.getPerson2StatsYamlEntries(nestingLevelNested);
    }

    protected String getStatOutputXML() {
        return XmlUtil.encloseContent("person1Stats", person1XML) +
                XmlUtil.encloseContent("person2Stats", person2XML);
    }

    protected String getStatOutputJson() {
        return ",\"person1Stats\": {" + person1Json + "}" +
                ",\"person2Stats\": {" + person2Json + "}";
    }

    protected String getStatOutputYaml() {
        YamlUtil yamlUtil = new YamlUtil(nestingLevel);

        return yamlUtil.getEntryHeader("person1Stats") + person1Yaml +
                yamlUtil.getEntryHeader("person2Stats") + person2Yaml;
    }
}
