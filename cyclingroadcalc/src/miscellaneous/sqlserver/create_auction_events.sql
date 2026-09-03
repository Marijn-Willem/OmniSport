DECLARE @season NVARCHAR(4)
DECLARE @season_id INT

SET @season = '2026'

SELECT @season_id = id FROM season WHERE name = @season

IF @season_id IS NULL
    BEGIN
        DECLARE @order INT

        SELECT @season_id = COALESCE(MAX(id), 0) + 1, @order = COALESCE(MAX([order]), 0) + 100 FROM season

        INSERT INTO season (id, name, [order], created, modified)
        VALUES (@season_id, @season, @order, GETDATE(), GETDATE())
    END

INSERT INTO compseason (competitionid, seasonid, created, modified)
SELECT id, @season_id, GETDATE(), GETDATE()
FROM competition
WHERE id BETWEEN 1 AND 15

INSERT INTO compseasonevent (competitionid, seasonid, compseasoneventid, sportid, sporteventid, genderid, created, modified)
SELECT id, @season_id, 1, -6, -1, -1, GETDATE(), GETDATE()
FROM competition
WHERE id BETWEEN 1 AND 15

INSERT INTO compseasoneventpart (competitionid, seasonid, compseasoneventid, compseasoneventpartid, sportid, sportdisciplineid, [order], isfinal, created, modified)
SELECT competitionid, seasonid, compseasoneventid, 1, sportid, 1, 1, 1, GETDATE(), GETDATE()
FROM compseasonevent
WHERE competitionid BETWEEN 1 AND 15
AND seasonid = @season_id
