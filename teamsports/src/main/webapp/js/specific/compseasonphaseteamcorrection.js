const correctionLoader = new ElementLoader('selCsptcid', function () {
    return '/CompSeasonPhaseTeamCorrectionList?cid=' + cid + '&sid=' + sid + '&pid=' + pid + '&tid=' + tid;
}, function () { setElementValueFromInitStateVar('selCsptcid', csptcid); });

function getProcessUrl() {
    const csptcid = getValueFromElementByName('inpUpd');
    const dt = getValueFromElementByName('dt');
    const pc = getValueFromElementByName('pc');

    return '/ProcessManageCompSeasonPhaseTeamCorrection?cid=' + cid + '&sid=' + sid + '&pid=' + pid + '&tid=' + tid +
        '&csptcid=' + csptcid + '&dt=' + dt + '&pc=' + pc;
}

function checkInput() {
    const dt = getValueFromElementByName('dt');

    return doCheckAndAlert(testDate(dt) || testDateTime(dt), 'Invalid date');
}

function loadCorrections() {
    correctionLoader.loadElement();
}

function goToManageCompSeasonPhaseTeamCorrection() {
    const csptcid = document.getElementById('selCsptcid').value;

    if (!isEmptyOrNull(csptcid))
        goToUrl('ManageCompSeasonPhaseTeamCorrection', 'cid=' + cid + '&sid=' + sid + '&pid=' + pid +
            '&tid=' + tid + '&csptcid=' + csptcid);
}

function goToInsertCompSeasonPhaseTeamCorrection() {
    goToUrl('ManageCompSeasonPhaseTeamCorrection', 'cid=' + cid + '&sid=' + sid + '&pid=' + pid +
        '&tid=' + tid + '&md=i');
}
