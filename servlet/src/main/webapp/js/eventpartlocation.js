const eventPartLocationListLoader = new ElementLoader('eplid', function () {
    return '/EventPartLocationList?' + getCompSeasonEventPartParameters();
}, null);

const geoNameListLoader = new ElementLoader('tblGn', function () {
    const gn = encodeURL(getValueFromElementByName('gn'));

    return !isEmptyOrNull(gn) ? '/GeoNameList?nm=' + gn : null;
}, null);

function getProcessUrl() {
    const eplid = getValueFromElementByName('inpUpd');
    const gn = encodeURL(getValueFromElementByName('gn'));
    const coo = encodeURL(getValueFromPointFields('coo'));
    const lrid = getValueFromElementByName('lrid');

    return '/ProcessManageEventPartLocation?' + getCompSeasonEventPartParameters() + '&eplid=' + eplid +
        '&gn=' + gn + '&coo=' + coo + '&lrid=' + lrid;
}

function checkInput() {
    const lrid = getValueFromElementByName('lrid');

    return validatePoint('coo') && doCheckAndAlert(!isEmptyOrNull(lrid), 'Location role must not be empty');
}

function initPortal() {
    eventPartLocationListLoader.loadElement();
}

function goToManageEventPartLocation() {
    const eplid = document.getElementById('eplid').value;

    if (!isEmptyOrNull(eplid))
        goToUrl('ManageEventPartLocation', getCompSeasonEventPartParameters() + '&eplid=' + eplid);
}

function goToInsertEventPartLocation() {
    goToUrl('ManageEventPartLocation', getCompSeasonEventPartParameters() + '&md=i');
}

function handleChangeGn() {
    geoNameListLoader.loadElement();
}

function handleClickGeoName(row) {
    const name = row.childNodes[0].innerHTML;
    const xy = row.getAttribute('coo').split('\|');

    document.getElementsByName('gn')[0].value = name;
    document.getElementsByName('coo_1')[0].value = xy.length > 0 ? xy[0] : null;
    document.getElementsByName('coo_2')[0].value = xy.length > 0 ? xy[1] : null;
}

function getCompSeasonEventPartParameters() {
    return 'cid=' + cid + '&sid=' + sid + '&cseid=' + cseid + '&csepid=' + csepid;
}
