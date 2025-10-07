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
    params = getUpdateWithNonEmptyParameter(params, 'es', 'es');

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

function goToCompSeasonEventPartPort(port) {
    const csepid = document.getElementById('csepid').value;
    if (!isEmptyOrNull(csepid))
        goToUrl(port, 'cid=' + cid + '&sid=' + sid + '&cseid=' + cseid + '&csepid=' + csepid);
}
