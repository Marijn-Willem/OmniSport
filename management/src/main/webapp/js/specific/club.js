function getProcessUrl() {
    const clid = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');
    const geid = getValueFromElementByName('geid');

    return '/ProcessManageClub?clid=' + clid + '&nm=' + nm + '&geid=' + geid;
}

function checkInput() {
    return true;
}

function goToManageClub() {
    const nm = document.getElementById('nm').value;
    window.location.href = path + '/ManageClub?nm=' + nm;
}

function goToAddClub() {
    window.location.href = path + '/ManageClub?md=i';
}

function goToEntityInstancePortal() {
    const nm = document.getElementById('nm').value;
    window.location.href = path + '/PrepareEntityInstancePortalClub?nm=' + nm + '&enm=Club';
}