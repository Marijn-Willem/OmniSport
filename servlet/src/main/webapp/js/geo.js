function loadGeoNameList(tblName, geoName) {
    if (!isEmptyOrNull(geoName))
        new ElementLoader(tblName, function () {
            return '/GeoNameList?nm=' + encodeURL(geoName);
        }, null).loadElement();
}

function handleChangePgn() {
    const pgn = getValueFromElementByName('pgn');
    loadGeoNameList('tblPgn', pgn);
}

function handleChangeGn() {
    const gn = document.getElementById('gn').value;
    loadGeoNameList('tblGn', gn);
}

function handleClickGeoName(tblRow) {
    const name = tblRow.childNodes[0].innerHTML;
    const tableId = tblRow.parentElement.parentElement.id;
    const input = getInputFromTableId(tableId);

    if (input !== null)
        input.value = name;
}

function getInputFromTableId(tableId) {
    if (tableId === 'tblGn')
        return document.getElementById('gn');
    else if (tableId === 'tblPgn')
        return document.getElementsByName('pgn')[0];

    return null;
}