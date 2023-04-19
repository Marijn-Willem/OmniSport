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
        return pId ? '/MatchMatrix?cid=' + cid + '&sid=' + sid + '&pid=' + getPIdDId()[0] + '&oc=' + oc : null;
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

function goToMatchTimeLine(competitionId, seasonId, matchId) {
    window.location.href = path + '/MatchTimeLine?cid=' + competitionId +
            '&sid=' + seasonId + '&mid=' + matchId;
}

function goToMatchMatrixDivision() {
    const pId_dId = getPIdDId();
    if (pId_dId[0])
        window.location.href = path + '/MatchMatrixDivision?cid=' + cid + '&sid=' + sid + '&pid=' + pId_dId[0];
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
    const tbl = document.getElementById('tblMatchMatrix');
    const headers = tbl.rows[0].children;
    const indices = [];

    for (let i = 0; i < headers.length; i++) {
        const header = headers[i];
        if (header.getAttribute('did') && header.getAttribute('did') === did)
            indices.push(i);
    }

    for (let i = 0; i < indices.length; i++) {
        const colIndX = indices[i];
        const startRowIndX = getFirstRowIndXForColIndX(tbl.rows, colIndX);
        const lastRowIndX = startRowIndX + parseInt(tbl.rows[startRowIndX].children[0].getAttribute('rowspan'));

        for (let j = startRowIndX; j < lastRowIndX; j++)
            tbl.rows[j].style.display = cb.checked ? 'table-row' : 'none';

        for (let j = 0; j < tbl.rows.length; j++) {
            const row = tbl.rows[j];
            for (let k = 0; k < row.children.length; k++) {
                const cell = row.children[k];
                if (cell.getAttribute('colindx') && cell.getAttribute('colindx') === colIndX.toString())
                    cell.style.display = cb.checked ? 'table-cell' : 'none';
            }
        }
    }
}

function getFirstRowIndXForColIndX(rows, colIndX) {
    let count = 0;

    for (let i = 1; i < rows.length; i++) {
        const row = rows[i];
        if (row.getElementsByTagName('th').length === 1)
            count++;

        if (count === colIndX)
            return i;
    }
}

function toggleElements(idList, className, display) {
    for (let i = 0; i < idList.length; i++)
        document.getElementById(idList[i]).style.display = className === 'stand' ? display : 'none';
}

function toggleMatchMatDivButton() {
    const pId_dId = getPIdDId();
    document.getElementById('btnMatchMatDiv').style.display = pId_dId && pId_dId.length === 2 ? 'inline' : 'none';
}