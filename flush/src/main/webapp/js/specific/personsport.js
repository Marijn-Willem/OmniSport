const competitionLoader = new ElementLoader('cid', function () {
    return '/CompetitionListNoH2HDouble';
}, loadSeasonList);

const seasonLoader = new ElementLoader('sid', function () {
    const cid = document.getElementById('cid').value;
    return !isEmptyOrNull(cid) ? '/SeasonList?cid=' + cid : null;
}, loadPersonSportList);

const personSportLoader = new ElementLoader('psid', function () {
    const cid = document.getElementById('cid').value;
    const sid = document.getElementById('sid').value;

    return !isEmptyOrNull(cid) && !isEmptyOrNull(sid) ? '/CompSeasonPersonSportList?cid=' + cid + '&sid=' + sid : null;
}, null);

function getFlushUrl() {
    const cid = document.getElementById('cid').value;
    const sid = document.getElementById('sid').value;
    const psid = document.getElementById('psid').value;

    return !isEmptyOrNull(cid) && !isEmptyOrNull(sid) && !isEmptyOrNull(psid) ?
        '/FlushPersonSport?cid=' + cid + '&sid=' + sid + '&psid=' + psid : null;
}

function loadCompetitionList() {
    competitionLoader.loadElement();
}

function loadSeasonList() {
    seasonLoader.loadElement();
}

function loadPersonSportList() {
    personSportLoader.loadElement();
}
