WITH compseasoneventpart AS (
    SELECT csep.competitionid, csep.seasonid, csep.compseasoneventid, csep.compseasoneventpartid
    FROM compseasoneventpart csep
             JOIN compseasonevent cse ON csep.competitionid = cse.competitionid
        AND csep.seasonid = cse.seasonid
        AND csep.compseasoneventid = cse.compseasoneventid
             JOIN competition c ON csep.competitionid = c.id
             JOIN season s ON csep.seasonid = s.id
    WHERE c.name = 'Vuelta a España'
      AND s.name = '2026'
      AND (cse.sporteventid = -2 OR (cse.sporteventid = -3 AND csep.isfinal))
)
SELECT ps.id, ps.description, SUM(CASE epps.rank
                                      WHEN 1 THEN 35
                                      WHEN 2 THEN 30
                                      WHEN 3 THEN 26
                                      WHEN 4 THEN 24
                                      WHEN 5 THEN 22
                                      ELSE 26 - epps.rank END) AS score
FROM eventpartpersonsport epps
         JOIN personsport ps ON epps.personsportid = ps.id
         JOIN compseasoneventpart csep
              ON epps.competitionid = csep.competitionid
                  AND epps.seasonid = csep.seasonid
                  AND epps.compseasoneventid = csep.compseasoneventid
                  AND epps.compseasoneventpartid = csep.compseasoneventpartid
WHERE epps.rank <= 25
GROUP BY ps.id, ps.description
ORDER BY score DESC;
