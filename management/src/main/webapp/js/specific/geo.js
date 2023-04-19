function getProcessUrl() {
    const geid = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');
    const gtid = getValueFromElementByName('gtid');
    const pgn = encodeURL(getValueFromElementByName('pgn'));
    const coo = encodeURL(getValueFromPointFields('coo'));

    return '/ProcessManageGeo?geid=' + geid + '&nm=' + nm + '&gtid=' + gtid + "&pgn=" + pgn + '&coo=' + coo;
}

function checkInput() {
    return validatePoint('coo');
}

function goToGeoTypePortal() {
    window.location.href = path + '/GeoTypePortal';
}

function goToManageGeo() {
    const gn = document.getElementById('gn').value;
    if (!isEmptyOrNull(gn))
        window.location.href = path + '/ManageGeo?nm=' + encodeURL(gn);
}

function goToAddGeo() {
    window.location.href = path + '/ManageGeo?md=i';
}

function goToEntityInstancePortal() {
    const gn = document.getElementById('gn').value;
    if (!isEmptyOrNull(gn))
        window.location.href = path + '/PrepareEntityInstancePortalGeo?nm=' + encodeURL(gn) + '&enm=Geo';
}