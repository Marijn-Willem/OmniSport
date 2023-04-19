package com.flush.servlet.html;

import java.io.IOException;
import java.io.Writer;

public class ManageFlushEntityInstanceCompSeason extends ManageFlushEntityInstanceNonCompSeason {
    @Override
    void writeSpecificFields(Writer w) throws IOException {
        w.append("<select id=\"cid\" onchange=\"loadSeasonList();\">\n</select><br/>\n");
        w.append("<select id=\"sid\">\n</select><br/>\n");
    }
}
