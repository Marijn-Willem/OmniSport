/**
 * Created by marij on 27-1-2017.
 */
const matchesCompSeasonPhaseLoader = new ElementLoader('tbl_matches',
    function () {
        return '/MatchesCompSeasonPhase?' + getParameters();
    },
    function () {
        loadStanding()
    });

const standingLoader = new ElementLoader('tbl_standing',
    function () {
        return '/Standing?' + getParameters();
    },
    null);

const matchMatrixLoader = new ElementLoader('tbl_matchmatrix',
    function () {
        return '/MatchMatrix?' + getParameters();
    },
    null);

function init() {
    setElementValueFromInitStateVar('selPid', pid);
    loadMatchesCompSeasonPhase();
}

function loadMatchesCompSeasonPhase() {
    const pid = document.getElementById('selPid').value;

    if (!isEmptyOrNull(pid))
        matchesCompSeasonPhaseLoader.loadElement();
}

function loadStanding() {
    const opt = document.getElementById('selPid').options;
    const indX = opt.selectedIndex;

    if (opt[indX].className === 'standing') {
        standingLoader.loadElement();
        matchMatrixLoader.loadElement();
    }
}

function goToAddMatch() {
    const pid = document.getElementById('selPid').value;

    if (!isEmptyOrNull(pid))
        goToUrl(aml, getParameters() + (amlP !== null ? '&' + amlP : ''));
}

function getParameters() {
    const pid = document.getElementById('selPid').value;

    return 'cid=' + cid + '&sid=' + sid + '&pid=' + pid;
}
