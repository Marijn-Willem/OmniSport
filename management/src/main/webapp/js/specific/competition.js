const genderIdOpen = -4;

function loadCompetitionList() {
    new ElementLoader('selCid', function() { return '/CompetitionList'; }, function () {
        setElementValueFromInitStateVar('selCid', cid);
    }).loadElement();
}

function getProcessUrl() {
    const cid = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');
    const spid = getValueFromElementByName('spid');
    const gid = getValueFromElementByName('gid');
    const dbl = getValueFromCheckbox('dbl');
    const cti = getValueFromElementByName('cti');
    const cdi = getValueFromElementByName('cdi');
    const dm = getValueFromCheckbox('dm');
    const geid = getValueFromElementByName('geid');

    return '/ProcessManageCompetition?cid=' + cid + '&nm=' + nm + '&spid=' + spid + '&gid=' + gid +
        '&dbl=' + dbl + '&cti=' + cti + '&cdi=' + cdi + '&dm=' + dm + '&geid=' + geid;
}

function checkInput() {
    if (getValueFromCheckbox('dm') && getValueFromElementByName('geid') === '0') {
        alert('Geo is mandatory in domestic competition');
        return false;
    }

    return validateDatetime('cdi');
}

function initManageCompetition() {
    handleSelectSportId();
}

function goToManageCompetition() {
    const cid = document.getElementById('selCid').value;

    if (cid !== null)
        window.location.href = path + '/ManageCompetition?cid=' + cid + '&md=u';
}

function goToCompSeasonManagementPortal() {
    goToCompetitionLink('CompSeasonManagementPortal');
}

function goToCompDivisionPortal() {
    goToCompetitionLink('CompDivisionPortal');
}

function handleSelectSportId() {
    const spidSel = getSelectedOption('spid');
    const gidSelect = document.getElementsByName('gid')[0];

    if (spidSel.className === 'alcifo') {
        gidSelect.value = genderIdOpen;
        gidSelect.disabled = true;
    }
    else
        gidSelect.disabled = false;
}

function handleCheckDomestic() {
    const dm = document.getElementsByName('dm')[0];
    const geid = document.getElementsByName('geid')[0];

    if (dm.checked)
        geid.disabled = false;
    else {
        geid.value = '0';
        geid.disabled = true;
    }
}

function goToCompetitionLink(link) {
    const cid = document.getElementById('selCid').value;

    if (cid !== null)
        window.location.href = path + '/' + link + '?cid=' + cid;
}