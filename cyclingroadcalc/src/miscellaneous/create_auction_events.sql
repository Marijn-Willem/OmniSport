DECLARE @season NVARCHAR(4)
DECLARE @season_id INT

SET @season = '2023'

SELECT @season_id = id FROM season WHERE name = @season

IF @season_id IS NULL
    BEGIN
        DECLARE @order INT

        SELECT @season_id = COALESCE(MAX(id), 0) + 1, @order = COALESCE(MAX([order]), 0) + 100 FROM season

        INSERT INTO season (id, name, [order], created, modified)
        VALUES (@season_id, @season, @order, GETDATE(), GETDATE())
    END

INSERT INTO competition (id, name, sportid, genderid, h2hdouble, isdomestic, created, modified)
SELECT 1, 'Omloop Het Nieuwsblad', 9, 1, 0, 0, GETDATE(), GETDATE()
UNION
SELECT 2, 'Kuurne-Brussel-Kuurne', 9, 1, 0, 0, GETDATE(), GETDATE()
UNION
SELECT 3, 'Strade Bianche', 9, 1, 0, 0, GETDATE(), GETDATE()
UNION
SELECT 4, 'Milano-San Remo', 9, 1, 0, 0, GETDATE(), GETDATE()
UNION
SELECT 5, 'Brugge-De Panne', 9, 1, 0, 0, GETDATE(), GETDATE()
UNION
SELECT 6, 'E3 Classic', 9, 1, 0, 0, GETDATE(), GETDATE()
UNION
SELECT 7, 'Gent-Wevelgem', 9, 1, 0, 0, GETDATE(), GETDATE()
UNION
SELECT 8, 'A Travers la Flandre', 9, 1, 0, 0, GETDATE(), GETDATE()
UNION
SELECT 9, 'Tour of Flanders', 9, 1, 0, 0, GETDATE(), GETDATE()
UNION
SELECT 10, 'Scheldeprijs', 9, 1, 0, 0, GETDATE(), GETDATE()
UNION
SELECT 11, 'Paris-Roubaix', 9, 1, 0, 0, GETDATE(), GETDATE()
UNION
SELECT 12, N'Flèche Brabançonne', 9, 1, 0, 0, GETDATE(), GETDATE()
UNION
SELECT 13, 'Amstel Gold Race', 9, 1, 0, 0, GETDATE(), GETDATE()
UNION
SELECT 14, N'Flèche Wallonne', 9, 1, 0, 0, GETDATE(), GETDATE()
UNION
SELECT 15, N'Liège-Bastogne-Liège', 9, 1, 0, 0, GETDATE(), GETDATE()

INSERT INTO compseason (competitionid, seasonid, created, modified)
SELECT id, @season_id, GETDATE(), GETDATE()
FROM competition
WHERE id <= 15

INSERT INTO compseasonevent (competitionid, seasonid, compseasoneventid, sportid, sporteventid, genderid, created, modified)
SELECT id, @season_id, 1, 9, 1, 1, GETDATE(), GETDATE()
FROM competition
WHERE id <= 15

UPDATE compseasonevent
SET externalsource = CASE competitionid
    WHEN 1 THEN 'omloop-het-nieuwsblad'
    WHEN 2 THEN 'kuurne-brussel-kuurne'
    WHEN 3 THEN 'strade-bianche'
    WHEN 4 THEN 'milano-sanremo'
    WHEN 5 THEN 'oxyclean-classic-brugge-de-panne'
    WHEN 6 THEN 'e3-harelbeke'
    WHEN 7 THEN 'gent-wevelgem'
    WHEN 8 THEN 'dwars-door-vlaanderen'
    WHEN 9 THEN 'ronde-van-vlaanderen'
    WHEN 10 THEN 'scheldeprijs'
    WHEN 11 THEN 'paris-roubaix'
    WHEN 12 THEN 'brabantse-pijl'
    WHEN 13 THEN 'amstel-gold-race'
    WHEN 14 THEN 'la-fleche-wallone'
    WHEN 15 THEN 'liege-bastogne-liege'
    END,
    modified = GETDATE()
WHERE competitionid <= 15
AND seasonid = @season_id

UPDATE compseasonevent
SET externalsource = CONCAT('https://www.procyclingstats.com/race/', externalsource, '/', @season, '/result'),
    modified = GETDATE()
WHERE competitionid <= 15
AND seasonid = @season_id

INSERT INTO compseasoneventpart (competitionid, seasonid, compseasoneventid, compseasoneventpartid, sportid, sporteventid, sporteventpartid, [order], created, modified)
SELECT cse.competitionid, cse.seasonid, cse.compseasoneventid, sep.sporteventpartid, cse.sportid, cse.sporteventid, sep.sporteventpartid, sep.[order], GETDATE(), GETDATE()
FROM compseasonevent cse
JOIN sporteventpart sep ON cse.sportid = sep.sportid AND cse.sporteventid = sep.sporteventid
WHERE cse.competitionid <= 15
AND cse.seasonid = @season_id
