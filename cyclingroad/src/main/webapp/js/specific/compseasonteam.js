const compSeasonTeamListLoader = new ElementLoader('tid', function () {
    return '/CompSeasonTeamList?cid=' + cid + '&sid=' + sid;
}, null);

function loadCompSeasonTeams() {
    compSeasonTeamListLoader.loadElement();
}

function goToManageCompSeasonTeamPersonSports() {
    const tid = document.getElementById('tid').value;

    if (!isEmptyOrNull(tid))
        goToUrl('ManageCompSeasonTeamPersonSports', 'cid=' + cid + '&sid=' + sid + '&tid=' + tid);
}