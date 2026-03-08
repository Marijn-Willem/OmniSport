let rowOnMove = null;

function ElementLoader(elId, getUrl, callBack, body = null) {
    this.loadElement = function () {
        const url = getUrl();

        if (url !== null) {
            const xHttp = new XMLHttpRequest();
            xHttp.onreadystatechange = function () {
                if (this.readyState === 4 && this.status === 200) {
                    if (elId !== null)
                        document.getElementById(elId).innerHTML = this.responseText;

                    if (callBack !== null)
                        callBack();
                }
            };

            if (body !== null) {
                xHttp.open("POST", path + url, true);
                xHttp.send(body);
            }
            else {
                xHttp.open("GET", path + url, true);
                xHttp.send();
            }
        }
    }
}

function setElementValueFromInitStateVar(elemId, initStateVar) {
    const element = document.getElementById(elemId);

    if (initStateVar && !element.getAttribute('initialized')) {
        element.value = initStateVar;
        element.setAttribute('initialized', 'true');
    }
}

function getParamStringFromNameValues(name) {
    const elems = document.getElementsByName(name);
    let parStr = '';

    for (let i = 0; i < elems.length; i++) {
        const elem = elems[i];

        if (elem.checked)
            parStr += '&' + name + '=' + elem.value;
    }

    return parStr;
}

function convertMsToTimeString(ms) {
    const minutes = Math.floor(ms / 60000).toString();
    const seconds = Math.floor((ms % 60000) / 1000).toString();
    const centis = Math.floor((ms % 1000) / 10).toString();

    return padZeroes(minutes, 2) + ':' + padZeroes(seconds, 2) + ':' + padZeroes(centis, 2);
}

function convertHMSStringToSeconds(hmsString) {
    const parts = hmsString.split(":");
    return 3600 * parseInt(parts[0]) + 60 * parseInt(parts[1]) + parseInt(parts[2]);
}

function convertSecondsToHMSString(seconds) {
    const hours = Math.floor(seconds / 3600).toString();
    const minutes = Math.floor(seconds % 3600 / 60).toString();
    const secs = Math.floor(seconds % 60).toString();

    return padZeroes(hours, 2) + ':' + padZeroes(minutes, 2) + ':' + padZeroes(secs, 2);
}

function padZeroes(str, lengthTot) {
    let result = str;

    for (let i = 0; i < lengthTot - str.length; i++)
        result = '0' + result;

    return result;
}

function concatStringsWithDelimiter(str1, str2, delim) {
    if (str1 == null || str1 === '')
        return str2;

    if (str2 == null || str2 === '')
        return str1;

    return str1 + delim + str2;
}

function doCheckAndAlert(check, errorMessage) {
    if (!check)
        alert(errorMessage);

    return check;
}

function getSelectedOption(selectId) {
    const sel = document.getElementById(selectId) || document.getElementsByName(selectId)[0];
    const selIndX = sel.selectedIndex;
    if (selIndX >= 0)
        return sel.options[selIndX];

    return null;
}

function encodeURL(text) {
    return text
        .replace(/\|/g, '%7C')
        .replace(/&/g, '%26');
}

function sortTable(tbl, compareFunc) {
    const rowCount = tbl.rows.length - 1;
    const tableRows = [];

    for (let i = 0; i < rowCount; i++) {
        tableRows.push(tbl.rows[1]);
        tbl.deleteRow(1);
    }

    const rowsSorted = getTableRowsSorted(tableRows, compareFunc);

    for (let i = 0; i < rowCount; i++)
        tbl.getElementsByTagName('tbody')[0].appendChild(rowsSorted[i]);
}

function compareIntegers(x1, x2) {
    if (isNaN(x1) && isNaN(x2))
        return 0;
    else if ((isNaN(x1) && !isNaN(x2)) || x1 > x2)
        return 1;
    else if ((isNaN(x2) && !isNaN(x1)) || x1 < x2)
        return -1;
    else
        return 0;
}

function compareStrings(x1, x2) {
    if (isEmptyOrNull(x1) && isEmptyOrNull(x2))
        return 0;
    else if ((isEmptyOrNull(x1) && !isEmptyOrNull(x2)) || x1 > x2)
        return 1;
    else if ((isEmptyOrNull(x2) && !isEmptyOrNull(x1)) || x1 < x2)
        return -1;
    else
        return 0;
}

function getTableRowsSorted(tableRows, compareFunc) {
    const length = tableRows.length;
    if (length <= 1)
        return tableRows;

    const centerIndX = Math.round(length / 2);
    const centerRow = tableRows[centerIndX];

    const rowsLeft = [];
    const rowsRight = [];

    for (let i = 0; i < length; i++)
        if (i !== centerIndX) {
            const row = tableRows[i];

            if (compareFunc(row, centerRow) === -1)
                rowsLeft.push(row);
            else
                rowsRight.push(row);
        }

    const rowsSorted = [];
    rowsSorted.push(...getTableRowsSorted(rowsLeft, compareFunc));
    rowsSorted.push(centerRow);
    rowsSorted.push(...getTableRowsSorted(rowsRight, compareFunc));

    return rowsSorted;
}

function applyOnClickToContentRows(tblid, onclick) {
    const rows = document.getElementById(tblid).rows;

    for (let i = 0; i < rows.length; i++) {
        const row = rows[i];

        if (isContentRow(row))
            row.onclick = onclick;
    }
}

function applySwitchableRowsToTable(tblid) {
    applyOnClickToContentRows(tblid, function(e) { handleSwitchableOnClick(e, tblid); });
}

function handleSwitchableOnClick(e, tblid) {
    if (!isEmptyOrNull(rowOnMove)) {
        const tbl = document.getElementById(tblid);
        const row = findParentRow(e.target);

        if (!isEmptyOrNull(row) && row !== rowOnMove) {
            tbl.deleteRow(rowOnMove.rowIndex);
            tbl.childNodes[1].insertBefore(rowOnMove, row);
            rowOnMove = null;
        }
    }
    else
        rowOnMove = findParentRow(e.target);
}

function findParentRow(el) {
    let parent = el;

    while (parent.tagName !== 'TR')
        parent = parent.parentNode;

    return parent;
}

function isContentRow(row) {
    return row.childNodes[0].tagName === 'TD';
}
