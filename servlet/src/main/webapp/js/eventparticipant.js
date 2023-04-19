const insertEventParticipantsFromCompSeasonLoader = new ElementLoader('resp', function () {
    return '/ProcessInsertEventParticipantsFromCompSeason?cid=' + cid + '&sid=' + sid + '&eid=' + eid;
}, null);

function insertEventParticipantsFromCompSeason() {
    insertEventParticipantsFromCompSeasonLoader.loadElement();
}