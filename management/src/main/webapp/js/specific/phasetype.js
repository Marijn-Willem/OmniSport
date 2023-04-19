const phaseTypeListLoader = new ElementLoader('ptid', function () {
    return '/PhaseTypeList';
}, null);

function getProcessUrl() {
    const ptid = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');
    const ip = getValueFromCheckbox('ip');
    const pptid = getValueFromElementByName('pptid');

    return '/ProcessManagePhaseType?ptid=' + ptid + '&nm=' + nm + '&ip=' + ip + '&pptid=' + pptid;
}

function checkInput() {
    return true;
}

function initPortal() {
    phaseTypeListLoader.loadElement();
}

function handleClickIsParent() {
    const cb = document.getElementsByName('ip')[0];
    const pptid = document.getElementsByName('pptid')[0];

    pptid.value = cb.checked ? '0' : pptid.value;
    pptid.disabled = cb.checked
}

function goToManagePhaseType() {
    const ptid = document.getElementById('ptid').value;

    if (!isEmptyOrNull(ptid))
        goToUrl('ManagePhaseType', 'ptid=' + ptid);
}

function goToInsertPhaseType() {
    goToUrl('ManagePhaseType', 'md=i');
}