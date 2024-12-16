function handleClickCbAll() {
    const cb = document.getElementById('cbAll');
    const tids = document.getElementsByName('tid');

    for (let i = 0; i < tids.length; i++)
        tids[i].checked = cb.checked;
}

function handleSend() {
    const message = 'This action cannot be undone, are you sure?';

    if (confirm(message)) {
        new ElementLoader('divUpd', function () {
            return '/ProcessManageCompSeasonPhaseTeams?cid=' + cid + '&sid=' + sid + '&pid=' + pid +
                getParamStringFromNameValues('tid');
        }, null).loadElement();

        document.getElementById('btnSend').style.display = 'none';
    }
}
