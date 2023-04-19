const competitionLoader = new ElementLoader('cid', function () {
    return '/CompetitionListFull';
}, loadSeasonList);

const seasonLoader = new ElementLoader('sid', function () {
    const cid = document.getElementById('cid').value;
    return !isEmptyOrNull(cid) ? '/SeasonList?cid=' + cid : null;
}, null);

function getFlushUrl() {
    const cid = document.getElementById('cid').value;
    const sid = document.getElementById('sid').value;

    return !isEmptyOrNull(cid) && !isEmptyOrNull(sid) ? '/FlushCompSeason?cid=' + cid + '&sid=' + sid : null;
}

function init() {
    loadCompetitionList();
}

function loadCompetitionList() {
    competitionLoader.loadElement();
}

function loadSeasonList() {
    seasonLoader.loadElement();
}
