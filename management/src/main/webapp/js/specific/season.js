function loadSeasonList() {
    new ElementLoader('sid', function() { return '/SeasonList'; }, null).loadElement();
}

function getProcessUrl() {
    const sid = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');
    const o = getValueFromElementByName('o');

    return '/ProcessManageSeason?sid=' + sid + '&nm=' + nm + '&o=' + o;
}

function checkInput() {
    return validateNumericTextFieldNonNull('o');
}

function goToManageSeason() {
    const sid = document.getElementById('sid').value;

    if (sid !== null)
        window.location.href = path + '/ManageSeason?sid=' + sid + '&md=u';
}