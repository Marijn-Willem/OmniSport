const eventPartNameListLoader = new ElementLoader('epnid', function () {
    return '/EventPartNameList?spid=' + spid + '&eid=' + eid;
}, null);

function getProcessUrl() {
    const epnid = getValueFromElementByName('inpUpd');
    const nm = encodeURL(getValueFromElementByName('nm'));

    return '/ProcessManageEventPartName?spid=' + spid + '&eid=' + eid + '&epnid=' + epnid +
        '&nm=' + nm;
}

function checkInput() {
    return true;
}

function initPortal() {
    eventPartNameListLoader.loadElement();
}

function goToManageEventPartName() {
    const epnid = document.getElementById('epnid').value;

    if (!isEmptyOrNull(epnid))
        goToUrl('ManageEventPartName', 'spid=' + spid + '&eid=' + eid + '&epnid=' + epnid);
}

function goToInsertEventPartName() {
    goToUrl('ManageEventPartName', 'spid=' + spid + '&eid=' + eid + '&md=i');
}