const correctionLoader = new ElementLoader('csptcid', function () {
    return '/CompSeasonPhaseTeamCorrectionList?cid=' + cid + '&sid=' + sid + '&pid=' + pid + '&tid=' + tid;
}, null);

function getProcessUrl() {
    const csptcid = getValueFromElementByName('inpUpd');
    const dt = getValueFromElementByName('dt');
    const pc = getValueFromElementByName('pc');

    return '/ProcessManageCompSeasonPhaseTeamCorrection?cid=' + cid + '&sid=' + sid + '&pid=' + pid + '&tid=' + tid +
        '&csptcid=' + csptcid + '&dt=' + dt + '&pc=' + pc;
}

function checkInput() {
    const dt = getValueFromElementByName('dt');

    return doCheckAndAlert(testDate(dt), 'Invalid date');
}

function loadCorrections() {
    correctionLoader.loadElement();
}

function goToManageCompSeasonPhaseTeamCorrection() {
    const csptcid = document.getElementById('csptcid').value;

    if (!isEmptyOrNull(csptcid))
        goToUrl('ManageCompSeasonPhaseTeamCorrection', 'cid=' + cid + '&sid=' + sid + '&pid=' + pid +
            '&tid=' + tid + 'csptcid=' + csptcid);
}
