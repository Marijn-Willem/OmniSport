const setGeneralClassificationPointsLoader = new ElementLoader('divIns', function () {
    return getUrlWithParameters('ProcessSetGeneralClassificationPoints');
}, null);

const insertEventPartParticipantsLoader = new ElementLoader('divIns', function () {
    const url = isTeam ? 'ProcessInsertEventPartTeams' : 'ProcessInsertEventPartPersonSports';
    return getUrlWithParameters(url);
}, null);

function setGeneralClassificationPoints() {
    setGeneralClassificationPointsLoader.loadElement();
}

function insertEventPartParticipants() {
    insertEventPartParticipantsLoader.loadElement();
}

function scrape() {
    const urlParameters = getUrlParameters();

    if (!isEmptyOrNull(urlParameters))
        goToUrl('Scrape', urlParameters);
}

function getUrlWithParameters(url) {
    const urlParameters = getUrlParameters();

    return !isEmptyOrNull(urlParameters) ? '/' + url + '?' + urlParameters : null;
}

function getUrlParameters() {
    const csepid = document.getElementById('csepid').value;

    return !isEmptyOrNull(csepid) ? 'cid=' + cid + '&sid=' + sid + '&cseid=' + cseid + '&csepid=' + csepid : null;
}
