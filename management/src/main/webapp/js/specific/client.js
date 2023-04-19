const clientListLoader = new ElementLoader('cnid', function () {
    return '/ClientList';
}, null);

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
    const cnid = document.getElementById('cnid').value;

    if (cnid !== null)
        window.location.href = path + '/ManageClient?cnid=' + cnid;
}

function goToAddClient() {
    window.location.href = path + '/ManageClient?md=i';
}