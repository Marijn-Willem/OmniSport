const entityInstanceListLoader = new ElementLoader('eiid', function () {
    return '/EntityInstanceList?eid=' + eid + '&enm=' + enm;
}, function () {
    setElementValueFromInitStateVar('eiid', eiid);
});

const processInsertEntityInstanceLoader = new ElementLoader('divIns', function () {
    const dt = document.getElementById('dt').value;
    return '/ProcessInsertEntityInstance?eid=' + eid + '&enm=' + enm + '&dt=' + dt;
}, loadEntityInstanceList);

function loadEntityInstanceList() {
    entityInstanceListLoader.loadElement();
}

function insertEntityInstance() {
    const dt = document.getElementById('dt').value;

    if (doCheckAndAlert(testDate(dt), 'Invalid date'))
        processInsertEntityInstanceLoader.loadElement();
}

function goToManageEntityInstance() {
    const eiid = document.getElementById('eiid').value;

    if (!isEmptyOrNull(eiid))
        window.location.href = path + '/ManageEntityInstance?eid=' + eid + '&eiid=' + eiid + '&enm=' + enm;
}

function getProcessUrl() {
    const eiid = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');

    return '/ProcessManageEntityInstance?eid=' + eid + '&eiid=' + eiid + '&nm=' + nm + '&enm=' + enm;
}

function checkInput() {
    return true;
}
