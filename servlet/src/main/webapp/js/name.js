const addToMemoryLoader = new ElementLoader('div_mem', function () {
    return '/' + addNamesToSessionUrl + '?' + getSelectedNames();
}, null);

function addToSelection() {
    const tblSel = document.getElementById('tbl_sel');

    const row = tblSel.insertRow();

    const cell1 = row.insertCell();
    cell1.innerHTML = "X";
    cell1.onclick = function () { removeFromSelection(row); };

    const cell2 = row.insertCell();
    cell2.innerHTML = getName();
}

function removeFromSelection(row) {
    document.getElementById('tbl_sel').deleteRow(row.rowIndex);
}

function addToMemory() {
    addToMemoryLoader.loadElement();
}

function getSelectedNames() {
    const tblSel = document.getElementById('tbl_sel');
    let selPers = '';

    for (let i = 0; i < tblSel.rows.length; i++)
        selPers += (selPers !== '' ? '&' : '') + 'nm=' + encodeURL(getValueFromSelectedRow(i));

    return selPers;
}

function getValueFromSelectedRow(row) {
    const tblSel = document.getElementById('tbl_sel');
    return tblSel.rows[row].cells[1].innerText;
}