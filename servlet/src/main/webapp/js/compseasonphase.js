const compSeasonPhaseListLoader = new ElementLoader('selPid', function () {
    return '/CompSeasonPhaseList?cid=' + cid + '&sid=' + sid + '&sd=false';
}, function() {
    setElementValueFromInitStateVar('selPid', pid);
    if (postProcessPhaseListLoad) postProcessPhaseListLoad();
});

function getProcessUrl() {
    const pid = getValueFromElementByName('inpUpd');
    const ppid = getValueFromElementByName('ppid');
    const rn = getValueFromElementByName('rn');
    const bo1 = getValueFromElementByName('bo1');
    const bo2 = getValueFromElementByName('bo2');
    const bod = getValueFromElementByName('bod');
    const fn = getValueFromCheckbox('fn');
    const sd = getValueFromElementByName('sd');
    const ed = getValueFromElementByName('ed');
    const st = getValueFromCheckbox('st');
    const kop = getValueFromCheckbox('kop');
    const po = getValueFromElementByName('po');
    const ef = getValueFromElementByName('ef');
    const ds = getValueFromCheckbox('ds');
    const ptid = getValueFromElementByName('ptid');

    return '/ProcessManageCompSeasonPhase?cid=' + cid + '&sid=' + sid + '&pid=' + pid +
        '&ppid=' + ppid + '&rn=' + rn + '&bo1=' + bo1 + '&bo2=' + bo2 + '&bod=' + bod +
        '&fn=' + fn + '&sd=' + sd + '&ed=' + ed + '&st=' + st + '&kop=' + kop +
        '&po=' + po + '&ef=' + ef + '&ds=' + ds + '&ptid=' + ptid;
}

function checkInput() {
    return validateDatetime('sd') && validateDatetime('ed');
}

function deletePhase(refreshUrl, phaseId) {
    new ElementLoader(null, function () {
        return '/DeleteCompSeasonPhase?cid=' + cid + '&sid=' + sid + '&pid=' + phaseId;
    }, function () {
        goToUrl(refreshUrl, 'cid=' + cid + '&sid=' + sid);
    }).loadElement();
}

function initCompSeasonPhaseList() {
    compSeasonPhaseListLoader.loadElement();
}

function goToManageCompSeasonPhase() {
    goToCompSeasonPhaseLink('ManageCompSeasonPhase');
}

function goToAddCompSeasonPhase() {
    goToUrl('ManageCompSeasonPhase', 'cid=' + cid + '&sid=' + sid + '&md=i');
}

function goToManageCompSeasonPhaseTeams() {
    goToCompSeasonPhaseLink('PrepareManageCompSeasonPhaseTeams');
}

function goToManageKnockout() {
    goToCompSeasonPhaseLink('KnockoutMain');
}

function goToCompSeasonPhaseLink(url) {
    const pid = document.getElementById('selPid').value;
    if (!isEmptyOrNull(pid))
        goToUrl(url, 'cid=' + cid + '&sid=' + sid + '&pid=' + pid);
}