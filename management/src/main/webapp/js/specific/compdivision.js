const compDivisionListLoader = new ElementLoader('did', function () {
    return '/CompDivisionList?cid=' + cid;
}, null);

function getProcessUrl() {
    const did = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');
    const pdid = getValueFromElementByName('pdid');

    return '/ProcessManageCompDivision?cid=' + cid + '&did=' + did + '&nm=' + nm + '&pdid=' + pdid;
}

function checkInput() {
    return true;
}

function loadCompDivisionList() {
    compDivisionListLoader.loadElement();
}

function goToManageCompDivision() {
    const did = document.getElementById('did').value;

    if (did !== null)
        window.location.href = path + '/ManageCompDivision?cid=' + cid + '&did=' + did;
}

function goToAddCompDivision() {
    window.location.href = path + '/ManageCompDivision?cid=' + cid + '&md=i';
}