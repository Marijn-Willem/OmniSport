/**
 * Created by marij on 16-1-2017.
 */
const createKnockoutMatchesLoader = new ElementLoader('div_res', function () {
    const pid = document.getElementById('pid').value;

    if (pid !== null)
        return '/CreateKnockoutMatches?cid=' + cid + '&sid=' + sid + '&pid=' + pid;

    return null;
}, null);

function goToManagePhaseParticipants() {
    goToURLWithPhaseId('ManageParticipantsCompSeasonPhase');
}

function goToURLWithPhaseId(url) {
    const pid = document.getElementById('pid').value;

    if (pid !== null)
        window.location.href = path + '/' + url + '?cid=' + cid + '&sid=' + sid + '&pid=' + pid;
}