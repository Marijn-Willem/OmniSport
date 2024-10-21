let inpGn;

function loadGeoNameList(gtid) {
    const geoName = inpGn.value;

    if (!isEmptyOrNull(geoName))
        new ElementLoader('tblGn', function () {
            return '/GeoNameList?nm=' + encodeURL(geoName) + (gtid !== null ? '&gtid=' + gtid : '');
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

function handleChangeCign() {
    inpGn = document.getElementsByName('cign')[0];
    loadGeoNameList(gtid);
}

function handleClickGeoName(tblRow) {
    const name = tblRow.childNodes[0].innerHTML;

    if (inpGn !== null)
        inpGn.value = name;
}
