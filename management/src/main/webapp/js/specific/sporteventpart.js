const sportEventPartLoader = new ElementLoader('epid', function () {
    return '/SportEventPartList?spid=' + spid + '&eid=' + eid;
}, null);

function goToManageSportEventPart() {
    const epid = document.getElementById('epid').value;

    if (epid !== null)
        window.location.href = path + '/ManageSportEventPart?spid=' + spid + '&eid=' + eid + '&epid=' + epid;
}

function checkInput() {
    return validateNumericTextFieldNonNull('o');
}

function getProcessUrl() {
    const epid = getValueFromElementByName('inpUpd');
    const did = getValueFromElementByName('did');
    const nm = getValueFromElementByName('nm');
    const o = getValueFromElementByName('o');
    const w = getValueFromElementByName('w');
    const fn = getValueFromCheckbox('fn');

    return '/ProcessManageSportEventPart?spid=' + spid + '&eid=' + eid + '&epid=' + epid + '&did=' + did +
        '&nm=' + nm + '&o=' + o + '&w=' + w + '&fn=' + fn;
}
