const locationRoleListLoader = new ElementLoader('lrid', function () {
    return '/LocationRoleList';
}, null);

function getProcessUrl() {
    const lrid = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');

    return '/ProcessManageLocationRole?lrid=' + lrid + '&nm=' + nm;
}

function checkInput() {
    return true;
}

function initPortal() {
    locationRoleListLoader.loadElement();
}

function goToManageLocationRole() {
    const lrid = document.getElementById('lrid').value;

    if (!isEmptyOrNull(lrid))
        goToUrl('ManageLocationRole', 'lrid=' + lrid);
}

function goToInsertLocationRole() {
    goToUrl('ManageLocationRole', 'md=i');
}