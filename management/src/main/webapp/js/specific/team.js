const clubListLoader = new ElementLoader('tblCn', function () {
    const nm = encodeURL(getValueFromElementByName('cn'));

    return !isEmptyOrNull(nm) ? '/ClubListByName?nm=' + nm : null;
}, null);

const equipeListLoader = new ElementLoader('tblEn', function () {
    const nm = encodeURL(getValueFromElementByName('en'));

    return !isEmptyOrNull(nm) ? '/EquipeListByName?nm=' + nm : null;
}, null);

function getProcessUrl() {
    const tid = getValueFromElementByName('inpUpd');
    const ds = encodeURL(getValueFromElementByName('ds'));
    const cn = encodeURL(getValueFromElementByName('cn'));
    const en = encodeURL(getValueFromElementByName('en'));
    const nid = getValueFromElementByName('nid');
    const spid = getValueFromElementByName('spid');
    const gid = getValueFromElementByName('gid');

    return '/ProcessManageTeam?tid=' + tid + '&ds=' + ds + '&cn=' + cn + '&en=' + en +
        '&nid=' + nid + '&spid=' + spid + '&gid=' + gid;
}

function checkInput() {
    const cn = getValueFromElementByName('cn');
    const en = getValueFromElementByName('en');
    const nid = getValueFromElementByName('nid');

    let fillCount = 0;

    if (!isEmptyOrNull(cn))
        fillCount += 1;

    if (!isEmptyOrNull(en))
        fillCount += 1;

    if (nid !== '0')
        fillCount += 1;

    return doCheckAndAlert(fillCount === 1,
        'Exactly one of Club, Equipe or Noc must be filled');
}

function loadClubList() {
    clubListLoader.loadElement();
}

function loadEquipeList() {
    equipeListLoader.loadElement();
}

function goToManageTeam() {
    const tid = document.getElementById('tid').value;

    window.location.href = path + '/ManageTeam?tid=' + tid;
}

function goToAddTeam() {
    const spid = document.getElementById('spid').value;
    const gid = document.getElementById('gid').value;

    window.location.href = path + '/ManageTeam?spid=' + spid + '&gid=' + gid + '&md=i';
}

function handleClickClubName(tableRow) {
    document.getElementsByName('cn')[0].value = tableRow.childNodes[0].innerText;
}

function handleClickEquipeName(tableRow) {
    document.getElementsByName('en')[0].value = tableRow.childNodes[0].innerText;
}

function setTeamDescription() {
    let teamName;
    const cn = getValueFromElementByName('cn');
    const en = getValueFromElementByName('en');

    if (!isEmptyOrNull(cn))
        teamName = cn;
    else if (!isEmptyOrNull(en))
        teamName = en;
    else
        teamName = getSelectedOption('nid').innerHTML;

    document.getElementsByName('ds')[0].value = teamName;
}