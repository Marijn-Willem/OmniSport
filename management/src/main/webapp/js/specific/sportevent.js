const sportEventListLoader = new ElementLoader('eid', function () {
    return '/SportEventList?spid=' + spid;
}, null);

function getProcessUrl() {
    const eid = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');
    const psa = getValueFromCheckbox('psa');
    const it = getValueFromCheckbox('it');

    return '/ProcessManageSportEvent?spid=' + spid + '&eid=' + eid + '&nm=' + nm +
        '&psa=' + psa + '&it=' + it;
}

function checkInput() {
    return true;
}

function goToManageSportEvent() {
    goToSportEventSpecificLink('ManageSportEvent');
}

function goToSportEventPartPortal() {
    goToSportEventSpecificLink('SportEventPartPortal');
}

function goToSportEventSpecificLink(link) {
    const eid = document.getElementById('eid').value;

    if (!isEmptyOrNull(eid))
        goToUrl(link, 'spid=' + spid + '&eid=' + eid);
}

function handleChangeSportEvent() {
    const option = getSelectedOption('eid');

    if (option !== null) {
        const fp = option.getAttribute('fp');
        document.getElementById('btnEpn').disabled = fp === 'true';
    }
}
