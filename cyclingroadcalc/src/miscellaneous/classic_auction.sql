DECLARE @season VARCHAR(4);
SET @season = '2023';

SELECT sub.id, sub.description, SUM(sub.points) AS pointsTotal
FROM (SELECT ps.id,
             ps.description,
             IIF(subc.category = 3, 2, 1) * COALESCE(subp.points, (submr.maxrank + 1 - epps.rank)) AS points
      FROM eventpartpersonsport epps
               JOIN personsport ps ON epps.personsportid = ps.id
               JOIN season s ON epps.seasonid = s.id
               JOIN (SELECT 1 AS id, 2 AS category
                     UNION
                     SELECT 2 AS id, 1 AS category
                     UNION
                     SELECT 3 AS id, 2 AS category
                     UNION
                     SELECT 4 AS id, 3 AS category
                     UNION
                     SELECT 5 AS id, 2 AS category
                     UNION
                     SELECT 6 AS id, 2 AS category
                     UNION
                     SELECT 7 AS id, 2 AS category
                     UNION
                     SELECT 8 AS id, 2 AS category
                     UNION
                     SELECT 9 AS id, 3 AS category
                     UNION
                     SELECT 10 AS id, 1 AS category
                     UNION
                     SELECT 11 AS id, 3 AS category
                     UNION
                     SELECT 12 AS id, 1 AS category
                     UNION
                     SELECT 13 AS id, 3 AS category
                     UNION
                     SELECT 14 AS id, 2 AS category
                     UNION
                     SELECT 15 AS id, 3 AS category) subc
                    ON epps.competitionid = subc.id
               JOIN (SELECT 1 AS category, 30 AS maxrank
                     UNION
                     SELECT 2 AS category, 50 AS maxrank
                     UNION
                     SELECT 3 AS category, 50 AS maxrank) submr
                    ON subc.category = submr.category
               LEFT JOIN (SELECT 1 AS category, 1 AS rank, 70 AS points
                          UNION
                          SELECT 1 AS category, 2 AS rank, 50 AS points
                          UNION
                          SELECT 1 AS category, 3 AS rank, 42 AS points
                          UNION
                          SELECT 1 AS category, 4 AS rank, 38 AS points
                          UNION
                          SELECT 1 AS category, 5 AS rank, 34 AS points
                          UNION
                          SELECT 1 AS category, 6 AS rank, 30 AS points
                          UNION
                          SELECT 1 AS category, 7 AS rank, 26 AS points
                          UNION
                          SELECT 1 AS category, 8 AS rank, 24 AS points
                          UNION
                          SELECT 2 AS category, 1 AS rank, 100 AS points
                          UNION
                          SELECT 2 AS category, 2 AS rank, 80 AS points
                          UNION
                          SELECT 2 AS category, 3 AS rank, 70 AS points
                          UNION
                          SELECT 2 AS category, 4 AS rank, 65 AS points
                          UNION
                          SELECT 2 AS category, 5 AS rank, 60 AS points
                          UNION
                          SELECT 2 AS category, 6 AS rank, 55 AS points
                          UNION
                          SELECT 2 AS category, 7 AS rank, 50 AS points
                          UNION
                          SELECT 2 AS category, 8 AS rank, 45 AS points
                          UNION
                          SELECT 2 AS category, 9 AS rank, 43 AS points
                          UNION
                          SELECT 3 AS category, 1 AS rank, 100 AS points
                          UNION
                          SELECT 3 AS category, 2 AS rank, 80 AS points
                          UNION
                          SELECT 3 AS category, 3 AS rank, 70 AS points
                          UNION
                          SELECT 3 AS category, 4 AS rank, 65 AS points
                          UNION
                          SELECT 3 AS category, 5 AS rank, 60 AS points
                          UNION
                          SELECT 3 AS category, 6 AS rank, 55 AS points
                          UNION
                          SELECT 3 AS category, 7 AS rank, 50 AS points
                          UNION
                          SELECT 3 AS category, 8 AS rank, 45 AS points
                          UNION
                          SELECT 3 AS category, 9 AS rank, 43 AS points) subp
                         ON subc.category = subp.category AND epps.rank = subp.rank
      WHERE s.name = @season
        AND epps.rank <= submr.maxrank) sub
GROUP BY sub.id, sub.description
ORDER BY pointsTotal DESC;
