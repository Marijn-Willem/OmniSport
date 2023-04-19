const sportDisciplineLoader = new ElementLoader('did', function () {
    return '/SportDisciplineList?spid=' + spid;
}, function () {
    setElementValueFromInitStateVar('did', did);
});

const resultTypePrecisionLoader = new ElementLoader('rtpid', function () {
    const rtid = getValueFromElementByName('rtid');
    return !isEmptyOrNull(rtid) ? '/ResultTypePrecisionList?rtid=' + rtid : null;
}, null);

function goToManageSportDiscipline() {
    goToDisciplineSpecificLink('ManageSportDiscipline');
}

function goToDisciplinePartPortal() {
    goToDisciplineSpecificLink('DisciplinePartPortal');
}

function goToDisciplineSpecificLink(link) {
    const did = document.getElementById('did').value;

    if (did !== null)
        window.location.href = path + '/' + link + '?spid=' + spid + '&did=' + did;
}

function getProcessUrl() {
    const did = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');
    const rtid = getValueFromElementByName('rtid');
    const rtpid = getValueFromElementByName('rtpid');

    return '/ProcessManageSportDiscipline?spid=' + spid + '&did=' + did + '&nm=' + nm + '&rtid=' + rtid + '&rtpid=' + rtpid;
}

function checkInput() {
    return true;
}

function handleSelectResultType() {
    resultTypePrecisionLoader.loadElement();
}
