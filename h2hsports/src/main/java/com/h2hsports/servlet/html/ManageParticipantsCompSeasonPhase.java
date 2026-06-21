package com.h2hsports.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

public class ManageParticipantsCompSeasonPhase extends com.sportservlet.html.ManageParticipantsCompSeasonPhase {
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
