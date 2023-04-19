const processLoader = new ElementLoader('divUpd', function () {
    return '/ProcessManageCompSeasonDivisions?cid=' + cid + '&sid=' + sid + getParamStringFromNameValues('did');
}, null);

function handleSend() {
    const confirmMessage = 'This cannot be undone, are you sure?';

    if (confirm(confirmMessage))
        processLoader.loadElement();

    document.getElementById('btnSnd').style.display = 'none';
}