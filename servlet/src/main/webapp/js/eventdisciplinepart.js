const eventDisciplinePartLoader = new ElementLoader('edpid', function () {
    return '/EventDisciplinePartList?cid=' + cid + '&sid=' + sid + '&eid=' + eid + '&csepid=' + csepid;
}, loadEventDisciplinePartPorts);

const eventDisciplinePartPortsLoader = new ElementLoader('divPorts', function () {
    const edpid = document.getElementById('edpid').value;
    return '/EventDisciplinePartPorts?cid=' + cid + '&sid=' + sid + '&eid=' + eid + '&csepid=' + csepid + '&edpid=' + edpid;
}, null);

function getProcessUrl() {
    const edpid = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');

    return '/ProcessManageEventDisciplinePart?cid=' + cid + '&sid=' + sid + '&eid=' + eid +
        '&csepid=' + csepid + '&edpid=' + edpid + '&nm=' + nm;
}

function checkInput() {
    return true;
}

function init() {
    loadEventDisciplinePartList();
}

function loadEventDisciplinePartList() {
    eventDisciplinePartLoader.loadElement();
}

function loadEventDisciplinePartPorts() {
    const edpid = document.getElementById('edpid').value;

    if (!isEmptyOrNull(edpid))
        eventDisciplinePartPortsLoader.loadElement();
}