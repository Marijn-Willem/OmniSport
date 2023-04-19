const insertDisciplinePartParticipantLoader = new ElementLoader('divIns', function () {
    const edpid = document.getElementById('edpid').value;
    const url = isTeam ? 'ProcessInsertDisciplinePartTeams' : 'ProcessInsertDisciplinePartPersonSports';

    return !isEmptyOrNull(edpid) ? '/' + url + '?cid=' + cid + '&sid=' + sid + '&eid=' + eid +
        '&csepid=' + csepid + '&edpid=' + edpid : null;
}, null);

function insertDisciplinePartParticipants() {
    insertDisciplinePartParticipantLoader.loadElement();
}