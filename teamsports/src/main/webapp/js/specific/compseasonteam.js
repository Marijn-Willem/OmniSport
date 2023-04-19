const compSeasonTeamLoader = new ElementLoader('selTid', function () {
    return '/CompSeasonTeamList?cid=' + cid + '&sid=' + sid;
}, function () {
    setElementValueFromInitStateVar('selTid', tid);
});

const processImportLoader = new ElementLoader('resp', function () {
    return '/ProcessCompSeasonTeamImport?cid=' + cid + '&sid=' + sid;
}, null);

function handleSend() {
    const confirmMessage = 'This action cannot be undone, are you sure?';

    if (confirm(confirmMessage)) {
        new ElementLoader('divUpd', function () {
            return '/ProcessManageCompSeasonTeams?cid=' + cid + '&sid=' + sid + getParamStringFromNameValues('tid');
        }, null).loadElement();

        document.getElementById('btnSnd').style.display = 'none';
    }
}

function loadCompSeasonTeams() {
    compSeasonTeamLoader.loadElement();
}

function goToManageCompSeasonTeam() {
    const tid = document.getElementById('selTid').value;

    if (!isEmptyOrNull(tid))
        window.location.href = path + '/ManageCompSeasonTeam?cid=' + cid + '&sid=' + sid + '&tid=' + tid;
}

function processCompSeasonTeamImport() {
    processImportLoader.loadElement();
}

function getProcessUrl() {
    const did = getValueFromElementByName('did');

    return '/ProcessManageCompSeasonTeam?cid=' + cid + '&sid=' + sid + '&tid=' + tid + '&did=' + did;
}

function checkInput() {
    return true;
}
