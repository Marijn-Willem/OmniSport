let inpGn;

function loadGeoNameList() {
    const geoName = inpGn.value;

    if (!isEmptyOrNull(geoName))
        new ElementLoader('tblGn', function () {
            return '/GeoNameList?nm=' + encodeURL(geoName);
        }, null).loadElement();
}

function handleChangePgn() {
    inpGn = document.getElementsByName('pgn')[0];
    loadGeoNameList();
}

function handleChangeGn() {
    inpGn = document.getElementById('gn');
    loadGeoNameList();
}

function handleChangeCgn() {
    inpGn = document.getElementsByName('cgn')[0];
    loadGeoNameList();
}

function handleClickGeoName(tblRow) {
    const name = tblRow.childNodes[0].innerHTML;

    if (inpGn !== null)
        inpGn.value = name;
}
