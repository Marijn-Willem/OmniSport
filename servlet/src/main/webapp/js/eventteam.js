const processEventTeamImportLoader = new ElementLoader('resp', function () {
    return '/ProcessEventTeamImport?cid=' + cid + '&sid=' + sid + '&eid=' + eid;
}, null);

function importEventTeams() {
    processEventTeamImportLoader.loadElement();
}