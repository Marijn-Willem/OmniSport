package com.teamsports.servlet.html;

import jakarta.servlet.http.HttpServletRequest;

public class ManageParticipantsCompSeasonPhase extends com.sportservlet.html.ManageParticipantsCompSeasonPhase {
    @Override
    public void initSpecificProperties(HttpServletRequest req) {
        cssList.add("styling");
    }
}
