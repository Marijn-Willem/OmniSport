const timeRegex = /\b\d{1,3}:\d{2}:\d{2}\b/
const resultTypeIdPoints = 1;
const resultTypeIdTime = 2;

let arrangeMode = false;
let arrangeIndX = 0;

let rowSwitchMode = false;

const saveElementLoader = new ElementLoader('divPt', function () {
    const specificParameters = getSpecificParameters();

    return '/' + processManageUrl + '?cid=' + cid + '&sid=' + sid + '&cseid=' + cseid +
        '&csepid=' + csepid + (!isEmptyOrNull(specificParameters) ? '&' + specificParameters : '') +
        getParticipantDataParameterString();
}, restoreModeRelatedFields);

const compareFuncRank = (row1, row2) => {
    const result = compareIntegers(parseInt(getRank(row1)), parseInt(getRank(row2)));
    return result === 0 ? compareStrings(getName(row1), getName(row2)) : result;
}

const compareFuncPoints = (row1, row2) => {
    let result = 0;

    if (rtid === resultTypeIdPoints)
        result = compareFuncPointsPoints(row1, row2);
    else if (rtid === resultTypeIdTime)
        result = compareFuncTimePoints(row1, row2);

    return result === 0 ? compareStrings(getName(row1), getName(row2)) : result;
}

const compareFuncPointsPoints = (row1, row2) => {
    return compareIntegers(parseInt(-getPoints(row1)), parseInt(-getPoints(row2)));
}

const compareFuncTimePoints = (row1, row2) => {
    const points1 = getPoints(row1);
    const points2 = getPoints(row2);

    if (!timeRegex.test(points1) && !timeRegex.test(points2))
        return 0;
    else if ((!timeRegex.test(points1) && timeRegex.test(points2)) || getTimeStringDiff(points1, points2) > 0)
        return 1;
    else if ((!timeRegex.test(points2) && timeRegex.test(points1)) || getTimeStringDiff(points1, points2) < 0)
        return -1;
    else
        return 0;
}

function sortRank() {
    sortTable(document.getElementById('tblPt'), compareFuncRank);
}

function sortPoints() {
    sortTable(document.getElementById('tblPt'), compareFuncPoints);
}

function save() {
    document.getElementById('btnMd').disabled = true;

    if (md === 'r')
        switchModeToAbsolute();

    if (validate())
        saveElementLoader.loadElement();
    else
        restoreModeRelatedFields();
}

function restoreModeRelatedFields() {
    if (md === 'r')
        switchModeToRelative();

    document.getElementById('btnMd').disabled = false;
}

function setRanks() {
    const tbl = document.getElementById('tblPt');

    let i = 1;

    while (i < tbl.rows.length && getNoCountResult(tbl.rows[i]) === '')
        setRank(tbl.rows[i], i++);
}

function clearRanks() {
    const tbl = document.getElementById('tblPt');

    for (let i = 1; i < tbl.rows.length; i++)
        setRank(tbl.rows[i], '');
}

function fillPoints() {
    const rows = document.getElementById('tblPt').rows;

    let curPoints = '', i = 1;

    while (i < rows.length && !isEmptyOrNull(getRank(rows[i])) && isEmptyOrNull(getNoCountResult(rows[i]))) {
        const points = getPoints(rows[i]);
        if (isEmptyOrNull(points))
            setPoints(rows[i], curPoints);
        else
            curPoints = points;

        i++;
    }
}

function fillNoCountResults() {
    const rows = document.getElementById('tblPt').rows;

    let curNcr = '';

    for (let i = 1; i < rows.length; i++) {
        const row = rows[i];
        const ncr = getNoCountResult(row);
        if (isEmptyOrNull(ncr) && !isEmptyOrNull(curNcr))
            setNoCountResult(row, curNcr);
        else
            curNcr = ncr;

        if (!isEmptyOrNull(curNcr))
            setRank(row, '');
    }
}

function switchMode() {
    let btnVal;

    if (md === 'a') {
        switchModeToRelative();
        md = 'r';
        btnVal = 'Set absolute';
    }
    else if (md === 'r') {
        switchModeToAbsolute();
        md = 'a';
        btnVal = 'Set relative';
    }

    document.getElementById('btnMd').value = btnVal;
}

function switchModeToRelative() {
    doSwitchMode((secsFirst, secs) => secs - secsFirst);
}

function switchModeToAbsolute() {
    doSwitchMode((secsFirst, secs) => secsFirst + secs);
}

function doSwitchMode(calcFunc) {
    const rows = document.getElementById('tblPt').rows;

    if (rows.length > 2) {
        const pointsFirst = getPoints(rows[1]);

        if (timeRegex.test(pointsFirst)) {
            const secsFirst = convertHMSStringToSeconds(pointsFirst);

            for (let i = 2; i < rows.length; i++) {
                const points = getPoints(rows[i]);

                if (timeRegex.test(points)) {
                    const secs = convertHMSStringToSeconds(points);
                    setPoints(rows[i], convertSecondsToHMSString(calcFunc(secsFirst, secs)));
                }
            }
        }
    }
}

function switchRowSwitchMode() {
    let buttonText;

    if (rowSwitchMode) {
        buttonText = 'Switchable rows';
        applyOnClickToContentRows('tblPt', function () { });
    }
    else {
        buttonText = 'No switchable rows';
        applySwitchableRowsToTable('tblPt');
    }

    document.getElementById('btnSw').value = buttonText;
    rowSwitchMode = !rowSwitchMode
}

function switchArrange() {
    if (arrangeMode)
        switchArrangeOff();
    else
        switchArrangeOn();
}

function switchArrangeOn() {
    document.getElementById('btnArr').value = 'Stop arrange participants';

    switchNonArrangeButtons(true);
    if (rowSwitchMode)
        switchRowSwitchMode();
    applyOnClickToContentRows('tblPt', handleArrangeOnClick);

    rowOnMove = null;
    arrangeMode = true;
}

function switchArrangeOff() {
    document.getElementById('btnArr').value = 'Arrange participants';

    applyOnClickToContentRows('tblPt', function () {});
    switchNonArrangeButtons(false);

    arrangeIndX = 0;
    arrangeMode = false;
}

function switchNonArrangeButtons(disabled) {
    const buttonContainer = document.getElementsByClassName('button_container')[0];
    const inputs = buttonContainer.getElementsByTagName('INPUT');

    for (let i = 0; i < inputs.length; i++) {
        const input = inputs[i];

        if (input.type === 'button' && input.id !== 'btnArr')
            input.disabled = disabled;
    }
}

function handleArrangeOnClick(e) {
    const tbl = document.getElementById('tblPt');

    if (arrangeIndX < tbl.rows.length - 1) {
        const row = findParentRow(e.target);
        if (!isEmptyOrNull(row) && row.rowIndex >= arrangeIndX && row.rowIndex > arrangeIndX++) {
            tbl.deleteRow(row.rowIndex);
            tbl.childNodes[1].insertBefore(row, tbl.rows[arrangeIndX]);
        }

        if (arrangeIndX === tbl.rows.length - 1)
            switchArrangeOff();
    }
}

function getParticipantDataParameterString() {
    let paramStr = '';

    const rows = document.getElementById('tblPt').rows;
    for (let i = 1; i < rows.length; i++) {
        const row = rows[i];

        const ptid = row.getAttribute('ptid');
        const rank = getRank(row);
        const points = getPoints(row);
        const noCountResult = getNoCountResult(row);

        paramStr += '&' + encodeURL('pt=' + ptid + '|' + rank + '|' + points + '|' + noCountResult);
    }

    return paramStr;
}

function validate() {
    const rows = document.getElementById('tblPt').rows;
    let previousRank = null;
    let previousPoints = null;
    let i = 1;

    let result = true;

    while (result && i < rows.length) {
        const row = rows[i];
        result = validateRow(rows[i], previousRank, previousPoints);
        previousRank = getRank(row);
        previousPoints = getPoints(row);
        i++;
    }

    return result;
}

function validateRow(row, previousRank, previousPoints) {
    const rank = getRank(row);
    const points = getPoints(row);
    const noCountResult = getNoCountResult(row);
    const emPrefix = 'Error in row ' + getName(row) + ': ';

    return (isEmptyOrNull(rank) && doCheckAndAlert(isEmptyOrNull(points), 'Points assigned to empty rank')) ||
        (!isEmptyOrNull(rank) && validateForNonEmptyRank(rank, points, noCountResult, previousRank, previousPoints, emPrefix));
}

function validateForNonEmptyRank(rank, points, noCountResult, previousRank, previousPoints, emPrefix) {
    return doCheckAndAlert(!isNaN(parseInt(rank)), emPrefix + 'Rank must be an integer') &&
        (previousRank === null || doCheckAndAlert(previousRank !== '' && parseInt(previousRank) - parseInt(rank) <= 0,
            emPrefix + 'Rank must be in ascending order')) &&
        ((isEmptyOrNull(noCountResult) && validatePoints(points, previousPoints, emPrefix)) ||
            (!isEmptyOrNull(noCountResult) && validateNoCountResult(noCountResult, points, emPrefix)))
}

function validatePoints(points, previousPoints, emPrefix) {
    if (rtid === resultTypeIdPoints)
        return validatePointsPoints(points, previousPoints, emPrefix);
    else if (rtid === resultTypeIdTime)
        return validateTimePoints(points, previousPoints, emPrefix);

    return true;
}

function validatePointsPoints(points, previousPoints, emPrefix) {
    return doCheckAndAlert(!isNaN(parseInt(points)), emPrefix + 'Points must be an integer') &&
        (previousPoints === null || doCheckAndAlert(previousPoints !== '' && previousPoints - parseInt(points) >= 0,
            emPrefix + 'Points must be in descending order'));
}

function validateTimePoints(points, previousPoints, emPrefix) {
    return doCheckAndAlert(timeRegex.test(points), emPrefix + 'Time in invalid format') &&
        (previousPoints === null || doCheckAndAlert(previousPoints !== '' && getTimeStringDiff(previousPoints, points) <= 0,
            emPrefix + 'Time must be in ascending order'));
}

function validateNoCountResult(noCountResult, points, emPrefix) {
    return doCheckAndAlert(isEmptyOrNull(points), emPrefix + 'Points and NoCountResult cannot be awarded simultaneously') &&
            doCheckAndAlert(ncrList.includes(noCountResult), emPrefix + 'Invalid NoCountResult');
}

function getRank(row) {
    return row.cells[0].children[0].value;
}

function setRank(row, rank) {
    row.cells[0].children[0].value = rank;
}

function getPoints(row) {
    return row.cells[2].children[0].value;
}

function setPoints(row, points) {
    row.cells[2].children[0].value = points;
}

function getNoCountResult(row) {
    return row.cells[3].children[0].value;
}

function setNoCountResult(row, noCountResult) {
    row.cells[3].children[0].value = noCountResult;
}

function getName(row) {
    return row.cells[1].innerHTML;
}

function getTimeStringDiff(timeStr1, timeStr2) {
    return convertHMSStringToSeconds(timeStr1) - convertHMSStringToSeconds(timeStr2);
}