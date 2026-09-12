const importActionElementLoader = new ElementLoader('resp', function () {
    const fl = document.getElementById('fl').value;

    return !isEmptyOrNull(fl) ? '/ProcessTeamMatchActionImport?spid=' + spid + '&fl=' + fl : null;
}, null);

function getProcessUrl() {
    const at = getValueFromElementByName('at');
    const tid = getValueFromElementByName('tid');
    const cnt = getValueFromElementByName('cnt');

    return '/ProcessManageTeamMatchAction?cid=' + cid + '&sid=' + sid + '&mid=' + mid + '&at=' + at +
        '&tid=' + tid + '&cnt=' + cnt;
}

function checkInput() {
    return doCheckAndAlert(md === 'i', 'Only insert allowed!') &&
        doCheckAndAlert(!isEmptyOrNull(getValueFromElementByName('tid')), 'Team is mandatory!') &&
        doCheckAndAlert(isCountValid(), 'Count must at least be 1');
}

function isCountValid() {
    const cnt = getValueFromElementByName('cnt');
    return cnt !== '-' && cnt !== '0';
}

function processImport() {
    importActionElementLoader.loadElement();
}
