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
    const epid = getValueFromElementByName('epid');
    const did = getValueFromElementByName('did');
    const epnid = getValueFromElementByName('epnid');
    const o = getValueFromElementByName('o');
    const st = getValueFromElementByName('st');

    let params = 'cid=' + cid + '&sid=' + sid + '&cseid=' + cseid + '&csepid=' + csepid +
        '&epid=' + epid + '&did=' + did + '&epnid=' + epnid + '&o=' + o + '&st=' + st;
    params = getUpdateWithNonEmptyParameter(params, 'dt', 'dt');
    params = getUpdateWithNonEmptyParameter(params, 'es', 'es');

    return '/ProcessManageCompSeasonEventPart?' + params;
}

function checkInput() {
    const epid = getValueFromElementByName('epid');
    const did = getValueFromElementByName('did');

    return doCheckAndAlert(fp || (epid === '0' && did !== '0') || (epid !== '0' && did === '0'),
            'Exactly one of event part and discipline must be filled') &&
        validateNumericTextFieldNonNull('o') &&
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
