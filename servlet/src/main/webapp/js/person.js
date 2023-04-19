/**
 * Created by marij on 10-1-2017.
 */
const addNamesToSessionUrl = 'AddNamesToSession';

const personSearchListLoader = new ElementLoader('tbl_pls', function () {
    return '/PersonSearchList?nm=' + getName();
}, null);

const dedoublePersonLoader = new ElementLoader('div_mem', function () {
    return '/DedoublePersons?' + getPersonsToDedouble();
}, null);

const splitPersonLoader = new ElementLoader('div_mem', function () {
    return '/SplitPerson?nm=' + getName();
}, null);

function getProcessUrl() {
    const pid = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');
    const geid = getValueFromElementByName('geid');
    const gid = getValueFromElementByName('gid');

    return '/ProcessManagePerson?pid=' + pid + '&nm=' + nm + '&geid=' + geid + '&gid=' + gid;
}

function checkInput() {
    return true;
}

function getName() {
    return document.getElementById('inp_pls').value;
}

function getPersonsToDedouble() {
    const p1 = getValueFromSelectedRow(0);
    const p2 = getValueFromSelectedRow(1);

    return 'p1=' + p1 + '&p2=' + p2;
}

function dedoublePersons() {
    if (doCheckAndAlert(checkNrSelected(2), 'You must select 2 persons to dedouble'))
        dedoublePersonLoader.loadElement();
}

function splitPerson() {
    if (doCheckAndAlert(checkNrSelected(1), 'You must select 1 person to split'))
        splitPersonLoader.loadElement();
}

function goToManagePerson() {
    if (doCheckAndAlert(checkNrSelected(1), 'You must select 1 person to manage'))
        goToUrl('ManagePerson', 'nm=' + getValueFromSelectedRow(0));
}

function goToEntityInstancePortal() {
    if (doCheckAndAlert(checkNrSelected(1), 'You must select exactly 1 person'))
        goToUrl('PrepareEntityInstancePortalPerson', 'enm=Person&nm=' + getValueFromSelectedRow(0));
}

function checkNrSelected(nr) {
    return document.getElementById('tbl_sel').rows.length === nr;
}

function countSelection() {
    alert(document.getElementById('tbl_sel').rows.length);
}

function handleSelectPersonName(tblRow) {
    document.getElementById('inp_pls').value = tblRow.childNodes[0].innerHTML;
}