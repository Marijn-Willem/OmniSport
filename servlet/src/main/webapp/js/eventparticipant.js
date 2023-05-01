const insertEventParticipantsFromCompSeasonLoader = new ElementLoader('resp', function () {
    return '/ProcessInsertEventParticipantsFromCompSeason?cid=' + cid + '&sid=' + sid + '&cseid=' + cseid;
}, null);

function insertEventParticipantsFromCompSeason() {
    insertEventParticipantsFromCompSeasonLoader.loadElement();
}
