const sportLoader = new ElementLoader('spid', function () {
    return '/SportList';
}, null);

function getFlushUrl() {
    const spid = document.getElementById('spid').value;

    return !isEmptyOrNull(spid) ? '/FlushSport?spid=' + spid : null;
}

function loadSports() {
    sportLoader.loadElement();
}
