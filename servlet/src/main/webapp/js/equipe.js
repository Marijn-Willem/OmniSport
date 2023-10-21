const equipeListByNameLoader = new ElementLoader('tblNm', function () {
    const nm = encodeURL(document.getElementById('nm').value);

    return !isEmptyOrNull(nm) ? '/EquipeListByName?nm=' + nm : null;
}, null);

function handleClickEquipeName(row) {
    document.getElementById('nm').value = row.children[0].innerText;
}

function loadEquipeList() {
    equipeListByNameLoader.loadElement();
}
