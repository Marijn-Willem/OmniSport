const sportLoader = new ElementLoader('selSpid', function () {
    return '/SportList';
}, function () {
    setElementValueFromInitStateVar('selSpid', spid);
    handleSelectSport();
});

function getProcessUrl() {
    const spid = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');
    const it = getValueFromCheckbox('it');
    const h2h = getValueFromCheckbox('h2h');
    const hmp = getValueFromCheckbox('hmp');

    return '/ProcessManageSport?spid=' + spid + '&nm=' + nm + '&it=' + it +
        '&h2h=' + h2h + '&hmp=' + hmp;
}

function checkInput() {
    return true;
}

function goToManageSport() {
    goToSportSpecificLink('ManageSport');
}

function goToSportEventPortal() {
    goToSportSpecificLink('SportEventPortal');
}

function goToSportDisciplinePortal() {
    goToSportSpecificLink('SportDisciplinePortal');
}

function goToSportSpecificLink(url) {
    const spid = document.getElementById('selSpid').value;

    if (spid !== null)
        window.location.href = path + '/'+ url + '?spid=' + spid;
}

function handleSelectSport() {
    const btnSe = document.getElementById('btnSe');
    const btnSd = document.getElementById('btnSd');

    const selectedOption = getSelectedSportOption();
    if (selectedOption !== null && selectedOption.className === 'alcifo')
        btnSe.disabled = btnSd.disabled = false;
    else
        btnSe.disabled = btnSd.disabled = true;
}

function getSelectedSportOption() {
    const sportOptions = document.getElementById('selSpid').options;

    for (let i = 0; i < sportOptions.length; i++) {
        const sportOption = sportOptions[i];
        if (sportOption.selected)
            return sportOption;
    }

    return null;
}
