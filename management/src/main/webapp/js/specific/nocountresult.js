const noCountResultListLoader = new ElementLoader('ncrid', function () {
    return '/NoCountResultList';
}, null);

function getProcessUrl() {
    const ncrid = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');

    return '/ProcessManageNoCountResult?ncrid=' + ncrid + '&nm=' + nm;
}

function checkInput() {
    return true;
}

function initPortal() {
    noCountResultListLoader.loadElement();
}

function goToManageNoCountResult() {
    const ncrid = document.getElementById('ncrid').value;

    if (!isEmptyOrNull(ncrid))
        goToUrl('ManageNoCountResult', 'ncrid=' + ncrid);
}

function goToInsertNoCountResult() {
    goToUrl('ManageNoCountResult', 'md=i');
}