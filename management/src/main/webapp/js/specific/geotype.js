const geoTypeListLoader = new ElementLoader('gtid', function () {
    return '/GeoTypeList';
}, null);

function getProcessUrl() {
    const gtid = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');

    return '/ProcessManageGeoType?gtid=' + gtid + '&nm=' + nm;
}

function checkInput() {
    return true;
}

function initPortal() {
    geoTypeListLoader.loadElement();
}

function goToManageGeoType() {
    const gtid = document.getElementById('gtid').value;

    if (gtid !== null)
        window.location.href = path + '/ManageGeoType?gtid=' + gtid;
}

function goToAddGeoType() {
    window.location.href = path + '/ManageGeoType?md=i';
}