/**
 * Created by Marijn-Willem on 6-10-2016.
 */
const phaseLoader = new ElementLoader('pid', function () {
    return '/CompSeasonPhaseList?cid=' + cid + '&sid=' + sid;
}, function() {
    handleSelectPhase();
});

const standingLoader = new ElementLoader('tblStanding', function () {
        const pId_dId = getPIdDId();
        const pId = pId_dId[0];
        const dId = pId_dId[1];

        return pId ? '/Standing?cid=' + cid + '&sid=' + sid + '&pid=' + pId_dId[0] +
            (dId ? '&did=' + dId : '') : null;
    }, null);

const matchMatrixLoader = new ElementLoader('tblMatchMatrix', function () {
    const pId = getPIdDId()[0];
        return pId ? '/PrepareMatchMatrix?cid=' + cid + '&sid=' + sid + '&pid=' + getPIdDId()[0] + '&oc=' + oc : null;
    }, null);

function init() {
    phaseLoader.loadElement();
}

function initMatchMatrixDivision() {
    matchMatrixLoader.loadElement();
}

function getPIdDId() {
    if (document.getElementById('pid'))
        return document.getElementById('pid').value.split('_');
    else
        return [pid];
}

function insertMatches(teamId) {
    const matchesLoader = new ElementLoader('tblMatches', function () {
        const url = teamId !== null ? '/MatchListTeam' : '/MatchListCompSeasonPhase';

        return url + '?cid=' + cid + '&sid=' + sid + '&pid=' + getPIdDId()[0] +
            (teamId !== null ? '&tid=' + teamId : '');
    }, null);

    matchesLoader.loadElement();
}

function goToMatchTimeLine(competitionId, seasonId, phaseId, matchId) {
    goToUrl('MatchTimeLine', 'cid=' + competitionId + '&sid=' + seasonId +
        '&pid=' + phaseId + '&mid=' + matchId);
}

function goToMatchMatrixDivision() {
    const pId_dId = getPIdDId();
    if (pId_dId[0])
        goToUrl('MatchMatrixDivision', 'cid=' + cid + '&sid=' + sid + '&pid=' + pId_dId[0]);
}

function handleSelectPhase() {
    const sel = document.getElementById('pid');

    if (sel.options.length > 0) {
        const cls = sel.options[sel.selectedIndex].className;

        const inlElemIds = ['btnMatchMat'];
        const tblElemIds = ['tblStanding', 'tblMatchMatrix'];

        toggleElements(inlElemIds, cls, 'inline');
        toggleElements(tblElemIds, cls, 'table');
        toggleMatchMatDivButton();

        if (getPIdDId()) {
            const loadFunc = cls === 'stand' ? function () {
                standingLoader.loadElement();
            } : function () {
                insertMatches(null);
            };

            loadFunc();
        }
    }
}

function handleClickDivision(cb) {
    const did = cb.value;

    toggleColumns(did, cb.checked);
    toggleRows(did, cb.checked);
}

function toggleColumns(did, checked) {
    const range = getColIndXRangeForDivision(did);
    const rows = document.getElementById('tblMatchMatrix').rows;

    const teamHeaders = rows[0].children;

    for (let i = 1; i < teamHeaders.length; i++) {
        const cell = teamHeaders[i];

        if (cell.getAttribute('did') === did) {
            cell.style.display = checked ? 'table-cell' : 'none';
            break;
        }
    }

    for (let i = 1; i < rows.length; i++) {
        const cells = rows[i].children;

        for (let j = 0; j < cells.length; j++) {
            const cell = cells[j];
            const colIndX = parseInt(cell.getAttribute('colindx'));

            if (colIndX >= range[0] && colIndX <= range[1])
                cell.style.display = checked ? 'table-cell' : 'none';
            else if (colIndX > range[1])
                break;
        }
    }
}

function toggleRows(did, checked) {
    const range = getRowIndXRangeForDivision(did);
    const rows = document.getElementById('tblMatchMatrix').rows;

    for (let i = range[0]; i <= range[1]; i++) {
        const row = rows[i];
        row.style.display = checked ? 'table-row' : 'none';
    }
}

function getColIndXRangeForDivision(did) {
    const compDivHeaders = document.getElementById('tblMatchMatrix').rows[0].children;

    let colIndXStart = 0;
    let colIndXEnd = colIndXStart - 1;
    let headerIndX = 1;

    while (headerIndX < compDivHeaders.length) {
        const header = compDivHeaders[headerIndX];
        const colSpan = parseInt(header.getAttribute('colspan'));

        colIndXEnd += colSpan;

        if (header.getAttribute('did') !== did) {
            colIndXStart = colIndXEnd + 1;
            headerIndX += 1;
        }
        else
            break;
    }

    return [colIndXStart, colIndXEnd];
}

function getRowIndXRangeForDivision(did) {
    const tbl = document.getElementById('tblMatchMatrix');
    const rows = tbl.rows;

    let rowIndXStart = 2;
    let rowIndXEnd = rowIndXStart - 1;

    while (rowIndXStart < rows.length) {
        const compDivHeader = rows[rowIndXStart].children[0];
        const rowSpan = parseInt(compDivHeader.getAttribute('rowspan'));

        rowIndXEnd += rowSpan;

        if (compDivHeader.getAttribute('did') !== did)
            rowIndXStart = rowIndXEnd + 1;
        else
            break;
    }

    return [rowIndXStart, rowIndXEnd];
}

function toggleElements(idList, className, display) {
    for (let i = 0; i < idList.length; i++)
        document.getElementById(idList[i]).style.display = className === 'stand' ? display : 'none';
}

function toggleMatchMatDivButton() {
    const pId_dId = getPIdDId();
    document.getElementById('btnMatchMatDiv').style.display = pId_dId && pId_dId.length === 2 ? 'inline' : 'none';
}
