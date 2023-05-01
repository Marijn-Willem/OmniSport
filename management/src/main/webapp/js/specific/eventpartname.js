const eventPartNameListLoader = new ElementLoader('epnid', function () {
    return '/EventPartNameList';
}, null);

function getProcessUrl() {
    const epnid = getValueFromElementByName('inpUpd');
    const nm = encodeURL(getValueFromElementByName('nm'));

    return '/ProcessManageEventPartName?epnid=' + epnid + '&nm=' + nm;
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
        goToUrl('ManageEventPartName', 'epnid=' + epnid);
}

function goToInsertEventPartName() {
    goToUrl('ManageEventPartName', 'md=i');
}
