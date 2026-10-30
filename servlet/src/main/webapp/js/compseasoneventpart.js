const compSeasonEventPartListLoader = new ElementLoader('csepid', function () {
    return '/CompSeasonEventPartList?cid=' + cid + '&sid=' + sid + '&cseid=' + cseid;
}, function () {
    setElementValueFromInitStateVar('csepid', csepid);
    loadPorts();
});

const compSeasonEventPartPortsLoader = new ElementLoader('divPorts', function () {
    const csepid = document.getElementById('csepid').value;
    return !isEmptyOrNull(csepid) ?
        '/CompSeasonEventPartPorts?cid=' + cid + '&sid=' + sid + '&cseid=' + cseid + '&csepid=' + csepid : null;
}, null);

const insertEventPartParticipantsLoader = new ElementLoader('divIns', function () {
    const url = isTeam ? 'ProcessInsertEventPartTeams' : 'ProcessInsertEventPartPersonSports';
    const urlParameters = getUrlParameters();

    return !isEmptyOrNull(urlParameters) ? '/' + url + '?' + urlParameters : null;
}, null);

function getProcessUrl() {
    const csepid = getValueFromElementByName('inpUpd');
    const did = getValueFromElementByName('did');
    const epnid = getValueFromElementByName('epnid');
    const o = getValueFromElementByName('o');
    const st = getValueFromElementByName('st');
    const fn = getValueFromCheckbox('fn');

    let params = 'cid=' + cid + '&sid=' + sid + '&cseid=' + cseid + '&csepid=' + csepid +
        '&did=' + did + '&epnid=' + epnid + '&o=' + o + '&st=' + st + '&fn=' + fn;
    params = getUpdateWithNonEmptyParameter(params, 'dt', 'dt');

    return '/ProcessManageCompSeasonEventPart?' + params;
}

function checkInput() {
    return validateNumericTextFieldNonNull('o') &&
        validateDatetime('dt');
}

function initPortal() {
    compSeasonEventPartListLoader.loadElement();
}

function loadPorts() {
    compSeasonEventPartPortsLoader.loadElement();
}

function goToUpdateCompSeasonEventPart() {
    goToCompSeasonEventPartPort('ManageCompSeasonEventPart');
}

function goToEventDisciplinePartPortal() {
    goToCompSeasonEventPartPort('EventDisciplinePartPortal');
}

function goToEventPartLocationPortal() {
    goToCompSeasonEventPartPort('EventPartLocationPortal');
}

function insertEventPartParticipants() {
    insertEventPartParticipantsLoader.loadElement();
}

function goToCompSeasonEventPartPort(port) {
    const urlParameters = getUrlParameters();
    if (!isEmptyOrNull(urlParameters))
        goToUrl(port, urlParameters);
}

function getUrlParameters() {
    const csepid = document.getElementById('csepid').value;

    return !isEmptyOrNull(csepid) ? 'cid=' + cid + '&sid=' + sid + '&cseid=' + cseid + '&csepid=' + csepid : null;
}
