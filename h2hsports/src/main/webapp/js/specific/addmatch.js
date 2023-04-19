/**
 * Created by marij on 23-1-2017.
 */
function getSetScores() {
    let setScores = [];

    const scoreRows = getScoreRows();
    let rowNr = 2, score1, score2;

    while (rowNr <= scoreRows.length &&
            !(isEmptyOrNull(score1 = getValueFromElementByName('scr_s' + (rowNr - 1) + '_1')) &
            isEmptyOrNull(score2 = getValueFromElementByName('scr_s' + (rowNr - 1) + '_2')))) {
        if (!isNaN(parseInt(score1)) && !isNaN(parseInt(score2))) {
            setScores.push(score1);
            setScores.push(score2);
        } else
            return [];

        rowNr += 1;
    }

    return setScores;
}

function toggleMatchRow() {
    const checked = getValueFromCheckbox('cb_match');
    setRowVisibility(getScoreRows()[0], checked);
    setRowVisibility(getNcrRow(), !checked);

    if (checked) {
        document.getElementsByName('ncr_1')[0].value = '';
        document.getElementsByName('ncr_2')[0].value = '';
    }
    else {
        document.getElementsByName('scr_m_1')[0].value = '';
        document.getElementsByName('scr_m_2')[0].value = '';
    }
}

function toggleSetRows() {
    const checked = getValueFromCheckbox('cb_sets');
    const scoreRows = getScoreRows();

    for (let i = 1; i < scoreRows.length; i++)
        setRowVisibility(scoreRows[i], checked);
}

function getScoreRows() {
    const scoreRows = [];
    const allRows = document.getElementById('tbl_scr').rows;

    for (let i = 1; i < allRows.length - 1; i++)
        scoreRows.push(allRows[i]);

    return scoreRows;
}

function getNcrRow() {
    const allRows = document.getElementById('tbl_scr').rows;

    return allRows[allRows.length - 1];
}

function setRowVisibility(row, checked) {
    row.style.display = checked ? 'table-row' : 'none';
}

function handleSubmit() {
    const p1id = getValueFromElementByName('p1id');
    const p2id = getValueFromElementByName('p2id');

    const checkParticipants = function () {
        return doCheckAndAlert(!isEmptyOrNull(p1id) && !isEmptyOrNull(p2id),
            'Participant must be defined');
    }

    let parameters = 'cid=' + cid + '&sid=' + sid + '&mid=' + mid + '&p1id=' + p1id + '&p2id=' + p2id;

    const processMatch = function () {
        if (getValueFromCheckbox('cb_match')) {
            const scr_m_1 = getValueFromElementByName('scr_m_1');
            const scr_m_2 = getValueFromElementByName('scr_m_2');

            const scr_m_1_is_int = !isNaN(parseInt(scr_m_1));
            const scr_m_2_is_int = !isNaN(parseInt(scr_m_2));

            if (scr_m_1_is_int)
                parameters += '&scr_m_1=' + scr_m_1;

            if (scr_m_2_is_int)
                parameters += '&scr_m_2=' + scr_m_2;

            return doCheckAndAlert(scr_m_1_is_int && scr_m_2_is_int, 'Match score must be a valid number');
        }
        else
            return true;
    }

    const processSets = function () {
        if (getValueFromCheckbox('cb_sets')) {
            const setScores = getSetScores();

            for (let i = 0; i < setScores.length; i += 2) {
                const set = (i / 2) + 1;
                parameters += '&scr_s' + set + '_1=' + setScores[i];
                parameters += '&scr_s' + set + '_2=' + setScores[i + 1];
            }

            return doCheckAndAlert(setScores.length > 0, 'Set score must be a valid number');
        }
        else
            return true;
    }

    const processNcr = function () {
        if (!getValueFromCheckbox('cb_match')) {
            const ncr_1 = getValueFromElementByName('ncr_1');
            const ncr_2 = getValueFromElementByName('ncr_2');

            if (!isEmptyOrNull(ncr_1))
                parameters += '&ncr_1=' + ncr_1;

            if (!isEmptyOrNull(ncr_2))
                parameters += '&ncr_2=' + ncr_2;

            return doCheckAndAlert(
                (!isEmptyOrNull(ncr_1) || !isEmptyOrNull(ncr_2)) &&
                (isEmptyOrNull(ncr_1) || ncrList.includes(ncr_1)) &&
                (isEmptyOrNull(ncr_2) || ncrList.includes(ncr_2)),
                'NCR must be valid');
        }
        else
            return true;
    }

    if (checkParticipants() && processMatch() && processSets() && processNcr())
        goToUrl('ProcessAddMatchScore', parameters);
}