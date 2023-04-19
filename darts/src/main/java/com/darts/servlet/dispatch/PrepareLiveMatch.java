package com.darts.servlet.dispatch;

import com.sports.entity.PersonMatch;
import com.sports.entity.PersonMatchPart;
import com.sports.entity.comparator.PersonMatchPartId;
import com.sports.entity.key.PersonMatchKey;
import com.sports.entity.key.PersonMatchPartKey;
import com.sports.entity.manager.PersonMatchManager;
import com.sports.entity.manager.PersonMatchPartManager;

import jakarta.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PrepareLiveMatch extends SuperDispatchServlet {
    protected void process(Statement stat, HttpServletRequest req) throws SQLException {
        dispatchURL = "LiveMatch";

        int mid = getIntValuedParameterValue(req, "mid");

        PersonMatchKey pmk = new PersonMatchKey(compSeasonKey, mid);
        PersonMatch personMatch = new PersonMatchManager(stat).getPersonMatch(pmk);

        prepareMatch(stat, req, personMatch);

        PersonMatchPartManager pmpm = new PersonMatchPartManager(stat);
        List<PersonMatchPart> setList = pmpm.getPersonMatchPartsWithoutParent(pmk);

        for (PersonMatchPart set : setList)
            if (set.isFinished())
                if (set.isPerson1Win())
                    personMatch.increaseScore1Person1();
                else
                    personMatch.increaseScore1Person2();

        if (setList.size() > 0) {
            Comparator<PersonMatchPart> comparator = new PersonMatchPartId();

            setList.sort(comparator);

            PersonMatchPart lastSet = setList.get(setList.size() - 1);
            personMatch.setMaxPersonMatchPart1Id(lastSet.getPersonMatchPartId());

            PersonMatchPartKey personMatchPartKey = new PersonMatchPartKey(pmk, lastSet.getPersonMatchPartId());

            List<PersonMatchPart> legList =
            pmpm.getPersonMatchPartsFromParents(Collections.singletonList(personMatchPartKey));

            legList.sort(comparator);
            personMatch.setMaxPersonMatchPart2Id(legList.get(legList.size() - 1).getPersonMatchPartId());

            if (!lastSet.isFinished())
                for (PersonMatchPart leg : legList)
                    if (leg.isFinished())
                        if (leg.isPerson1Win())
                            personMatch.increaseScore2Person1();
                        else
                            personMatch.increaseScore2Person2();

        }
    }
}
