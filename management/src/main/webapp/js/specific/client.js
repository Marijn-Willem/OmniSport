const clientListLoader = new ElementLoader('selCnid', function () {
    return '/ClientList';
}, function () { setElementValueFromInitStateVar('selCnid', cnid); });

function getProcessUrl() {
    const cnid = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');
    const lid = getValueFromElementByName('lid');
    const ia = getValueFromCheckbox('ia');
    const pwd = getValueFromElementByName('pwd');

    return '/ProcessManageClient?cnid=' + cnid + '&nm=' + nm + '&lid=' + lid + '&ia=' + ia + '&pwd=' + pwd;
}

function checkInput() {
    return true;
}

function loadClientList() {
    clientListLoader.loadElement();
}

function goToManageClient() {
    const cnid = document.getElementById('selCnid').value;

    if (!isEmptyOrNull(cnid))
        goToUrl('ManageClient', 'cnid=' + cnid);
}

function goToAddClient() {
    goToUrl('ManageClient', 'md=i');
}
