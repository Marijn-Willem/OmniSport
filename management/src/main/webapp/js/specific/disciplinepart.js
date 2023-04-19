const disciplinePartLoader = new ElementLoader('dpid', function () {
    return '/DisciplinePartList?spid=' + spid + '&did=' + did;
}, null);

function goToManageDisciplinePart() {
    const dpid = document.getElementById('dpid').value;

    if (dpid !== null)
        window.location.href = path + '/ManageDisciplinePart?spid=' + spid + '&did=' + did + '&dpid=' + dpid;
}

function getProcessUrl() {
    const dpid = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');
    const o = getValueFromElementByName('o');

    return '/ProcessManageDisciplinePart?spid=' + spid + '&did=' + did + '&dpid=' + dpid + '&nm=' + nm + '&o=' + o;
}

function checkInput() {
    return validateNumericTextFieldNonNull('o');
}