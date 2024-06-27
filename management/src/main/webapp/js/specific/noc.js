const nocListLoader = new ElementLoader('nid', function () {
    return '/NocList';
}, function () {
    setElementValueFromInitStateVar('nid', nid);
});

function getProcessUrl() {
    const nid = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');
    const geid = getValueFromElementByName('geid');

    return '/ProcessManageNoc?nid=' + nid + '&nm=' + nm + '&geid=' + geid;
}

function checkInput() {
    return true;
}

function initNocList() {
    nocListLoader.loadElement();
}

function goToManageNoc() {
    const nid = document.getElementById('nid').value;

    if (!isEmptyOrNull(nid))
        window.location.href = path + '/ManageNoc?nid=' + nid;
}

function goToAddNoc() {
    window.location.href = path + '/ManageNoc?md=i';
}

function setNocNameFromGeoName() {
    const selOption = getSelectedOption('geid');

    if (selOption !== null && selOption.value !== '0')
        document.getElementsByName('nm')[0].value = selOption.innerHTML;
}

function goToEntityInstancePortal() {
    const nid = document.getElementById('nid').value;

    if (!isEmptyOrNull(nid))
        window.location.href = path + '/EntityInstancePortal?eid=' + nid + '&enm=Noc';
}