/**
 * Created by marij on 27-1-2017.
 */
const matchesCompSeasonPhaseLoader = new ElementLoader('tbl_matches',
    function () {
        const pid = document.getElementById('selPid').value;

        return '/MatchesCompSeasonPhase?cid=' + cid + '&sid=' + sid + '&pid=' + pid;
    },
    function () {
        loadStanding()
    });

const standingLoader = new ElementLoader('tbl_standing',
    function () {
        const pid = document.getElementById('selPid').value;

        return '/Standing?cid=' + cid + '&sid=' + sid + '&pid=' + pid;
    },
    null);

const matchMatrixLoader = new ElementLoader('tbl_matchmatrix',
    function () {
        const pid = document.getElementById('selPid').value;

        return '/MatchMatrix?cid=' + cid + '&sid=' + sid + '&pid=' + pid;
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