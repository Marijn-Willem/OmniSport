const importActionElementLoader = new ElementLoader('resp', function () {
    const fl = document.getElementById('fl').value;

    return !isEmptyOrNull(fl) ? '/ProcessTeamMatchActionImport?spid=' + spid + '&fl=' + fl : null;
}, null);

function getProcessUrl() {
    const at = getValueFromElementByName('at');
    const tid = getValueFromElementByName('tid');
    const m = getValueFromElementByName('m');
    const mpid = getValueFromElementByName('mpid');

    return '/ProcessManageTeamMatchAction?cid=' + cid + '&sid=' + sid + '&mid=' + mid + '&at=' + at +
        '&tid=' + tid + '&m=' + m + '&mpid=' + mpid;
}

function checkInput() {
    if (md !== 'i') {
        alert('Only insert allowed!');
        return false;
    }

    if (isEmptyOrNull(getValueFromElementByName('tid'))) {
        alert('Team is mandatory!');
        return false;
    }

    return true;
}

function processImport() {
    importActionElementLoader.loadElement();
}