let detailLoader;
let seasonLoader;
let competitionLoader;

function initCompSeason() {
    detailLoader = new ElementLoader('divPorts', function () {
        const cIdSId = getCIdSId();

        const cId = cIdSId[0];
        const sId = cIdSId[1];

        return !isEmptyOrNull(cId) && !isEmptyOrNull(sId) ?
            '/CompSeasonPorts?cid=' + cId + '&sid=' + sId : null;
    }, null);

    seasonLoader = new ElementLoader('selSid', function () {
        const cId = document.getElementById('selCid').value;

        return !isEmptyOrNull(cId) ? '/SeasonList?cid=' + cId : null;
    }, function() {
        setElementValueFromInitStateVar('selSid', sid);
        detailLoader.loadElement();
    });

    competitionLoader = new ElementLoader('selCid', function () {
        return '/CompetitionList?spid=' + spid;
    }, function() {
        setElementValueFromInitStateVar('selCid', cid);
        seasonLoader.loadElement();
    });

    competitionLoader.loadElement();
}

function getCIdSId() {
    const cId = document.getElementById('selCid') ? document.getElementById('selCid').value : cid;
    const sId = document.getElementById('selSid') ? document.getElementById('selSid').value : sid;

    return [cId, sId];
}
