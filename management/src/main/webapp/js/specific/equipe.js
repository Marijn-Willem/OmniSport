function getProcessUrl() {
    const eqid = getValueFromElementByName('inpUpd');
    const nm = encodeURL(getValueFromElementByName('nm'));

    return '/ProcessManageEquipe?eqid=' + eqid + '&nm=' + nm;
}

function checkInput() {
    return true;
}

function goToManageEquipe() {
    const nm = encodeURL(document.getElementById('nm').value);

    if (!isEmptyOrNull(nm))
        goToUrl('ManageEquipe', 'nm=' + nm);
}

function goToInsertEquipe() {
    goToUrl('ManageEquipe', 'md=i');
}

function goToEntityInstancePortal() {
    const nm = encodeURL(document.getElementById('nm').value);

    if (!isEmptyOrNull(nm))
        goToUrl('PrepareEntityInstancePortalEquipe', 'nm=' + nm + '&enm=Equipe');
}
