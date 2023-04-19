function getFlushUrl() {
    const cnid = document.getElementById('cnid').value;
    const aeid = document.getElementById('aeid').value;
    const eid = document.getElementById('eid').value;

    if (!isEmptyOrNull(cnid) && !isEmptyOrNull(aeid) && !isEmptyOrNull(eid))
        return '/FlushClientAlias?cnid=' + cnid + '&aeid=' + aeid + '&eid=' + eid;

    return null;
}
