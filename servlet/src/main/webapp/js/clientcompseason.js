const processClientCompSeasonLoader = new ElementLoader('divRes', function () {
    return '/ProcessManageClientCompSeasons?cid=' + cid + '&sid=' + sid + getParamStringFromNameValues('cnid');
}, null);

function processClientCompSeasons() {
    processClientCompSeasonLoader.loadElement();
}