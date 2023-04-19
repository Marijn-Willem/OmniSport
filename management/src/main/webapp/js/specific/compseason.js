const addCompSeasonLoader = new ElementLoader('divResp', function () {
    const sid = document.getElementById('selSid').value;

    return '/ProcessAddCompSeason?cid=' + cid + '&sid=' + sid;
}, null);
const compSeasonManagementPortalLoader = new ElementLoader('selSid', function () {
    return '/SeasonList?cid=' + cid;
}, function () { setElementValueFromInitStateVar('selSid', sid) });

function getProcessUrl() {
    const tab = getValueFromElementByName('tab');
    const trb = getValueFromElementByName('trb');
    const ldb = getValueFromElementByName('ldb');
    const sd = getValueFromElementByName('sd');
    const ed = getValueFromElementByName('ed');

    return '/ProcessManageCompSeason?cid=' + cid + '&sid=' + sid + '&tab=' + tab +
        '&trb=' + trb + '&ldb=' + ldb + '&sd=' + sd + '&ed=' + ed;
}

function checkInput() {
    return validateDatetime('sd') && validateDatetime('ed');
}

function initCompSeasonManagementPortal() {
    compSeasonManagementPortalLoader.loadElement();
}

function goToManageCompSeason() {
    goToCompSeasonManagementUrl('ManageCompSeason');
}

function goToManageClientCompSeasons() {
    goToCompSeasonManagementUrl('ManageClientCompSeasons');
}

function goToManageAddCompSeason() {
    window.location.href = path + '/ManageAddCompSeason?cid=' + cid;
}

function goToCompSeasonManagementUrl(url) {
    const sid = document.getElementById('selSid').value;

    if (!isEmptyOrNull(sid))
        goToUrl(url, 'cid=' + cid + '&sid=' + sid);
}