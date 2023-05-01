const processEventTeamImportLoader = new ElementLoader('resp', function () {
    return '/ProcessEventTeamImport?cid=' + cid + '&sid=' + sid + '&cseid=' + cseid;
}, null);

function importEventTeams() {
    processEventTeamImportLoader.loadElement();
}