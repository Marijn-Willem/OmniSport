const setGeneralClassificationPointsLoader = new ElementLoader('divIns', function () {
    return getUrlWithParameters('ProcessSetGeneralClassificationPoints');
}, null);

const insertEventPartParticipantsLoader = new ElementLoader('divIns', function () {
    const url = isTeam ? 'ProcessInsertEventPartTeams' : 'ProcessInsertEventPartPersonSports';
    return getUrlWithParameters(url);
}, null);

const scrapeLoader = new ElementLoader('divIns', function () {
    return getUrlWithParameters('Scrape');
}, null);

function setGeneralClassificationPoints() {
    setGeneralClassificationPointsLoader.loadElement();
}

function insertEventPartParticipants() {
    insertEventPartParticipantsLoader.loadElement();
}

function scrape() {
    scrapeLoader.loadElement();
}

function getUrlWithParameters(url) {
    const csepid = document.getElementById('csepid').value;

    return !isEmptyOrNull(csepid) ? '/' + url + '?cid=' + cid + '&sid=' + sid + '&cseid=' + cseid +
        '&csepid=' + csepid : null;
}
