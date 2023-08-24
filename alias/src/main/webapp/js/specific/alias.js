const sportListLoaderAlias = new ElementLoader('spid', function () {
    return '/SportList';
}, null);

const alcifoSportListLoader = new ElementLoader('spid', function () {
    return '/SportList';
}, function () {
   removeNonAlcifoSports();
   loadSportDisciplineList();
   loadSportEventList();
});

const sportDisciplineListLoader = new ElementLoader('did', function () {
    const spid = document.getElementById('spid').value;
    return '/SportDisciplineList?spid=' + spid;
}, loadDisciplinePartList);

const sportEventListLoader = new ElementLoader('eid', function () {
    const spid = document.getElementById('spid').value;
    return '/SportEventList?spid=' + spid;
}, loadEventPartList);

const disciplinePartListLoader = new ElementLoader('dpid', function () {
    const spid = document.getElementById('spid').value;
    const did = document.getElementById('did').value;

    return '/DisciplinePartList?spid=' + spid + '&did=' + did;
}, null);

const eventPartListLoader = new ElementLoader('epid', function () {
    const spid = document.getElementById('spid').value;
    const eid = document.getElementById('eid').value;

    return '/SportEventPartList?spid=' + spid + '&eid=' + eid;
}, null);

const competitionListLoader = new ElementLoader('cid', function () {
    return '/CompetitionList';
}, null);

const aliasListLoader = new ElementLoader('aid', function () {
    return '/AliasList?aeid=' + aeid + '&eid=' + eid;
}, null);

const aliasDeleteLoader = new ElementLoader('divDel', function () {
    const aid = document.getElementById('aid').value;
    return '/DeleteAlias?aeid=' + aeid + '&aid=' + aid;
}, loadAliasList);

const personSearchListLoader = new ElementLoader('tbl_ps', function () {
    const nm = document.getElementById('inp_ps').value;
    return '/PersonSearchList?nm=' + nm;
}, null);

const geoNameListLoader = new ElementLoader('tbl_gn', function () {
    const nm = document.getElementById('gn').value;
    return '/GeoNameList?nm=' + nm;
}, null);

const nocListLoader = new ElementLoader('nid', function () {
    return '/NocList';
}, null);

const clubIdByNameLoader = new ElementLoader('clid', function () {
    const nm = document.getElementById('nm').value;
    return '/GetClubIdByName?nm=' + nm;
}, function() { goToEntityInstanceAliasPortal(getClubId); });

const entityInstanceListLoader = new ElementLoader('eiid', function () {
    return '/EntityInstanceList?eid=' + eid + '&enm=' + enm;
}, null);

const phaseTypeListLoader = new ElementLoader('ptid', function () {
    return '/PhaseTypeList';
}, null);

const locationRoleListLoader = new ElementLoader('lrid', function () {
    return '/LocationRoleList';
}, null);

const eventPartNameListLoader = new ElementLoader('epnid', function () {
    return '/EventPartNameList';
}, null);

const equipeListByNameLoader = new ElementLoader('tbl_eqn', function () {
    const nm = document.getElementById('eqn').value;
    return '/EquipeListByName?nm=' + nm;
}, null);

function loadSportList() {
    sportListLoaderAlias.loadElement();
}

function loadAlcifoSportList() {
    alcifoSportListLoader.loadElement();
}

function loadSportDisciplineList() {
    const spid = document.getElementById('spid').value;

    if (!isEmptyOrNull(spid) && document.getElementById('did') !== null)
        sportDisciplineListLoader.loadElement();
}

function loadSportEventList() {
    const spid = document.getElementById('spid').value;

    if (!isEmptyOrNull(spid) && document.getElementById('eid') !== null)
        sportEventListLoader.loadElement();
}

function loadDisciplinePartList() {
    const spid = document.getElementById('spid').value;
    const did = document.getElementById('did').value;

    if (!isEmptyOrNull(spid) && !isEmptyOrNull(did) && document.getElementById('dpid') !== null)
        disciplinePartListLoader.loadElement();
}

function loadEventPartList() {
    const spid = document.getElementById('spid').value;
    const eid = document.getElementById('eid').value;

    if (!isEmptyOrNull(spid) && !isEmptyOrNull('eid') && document.getElementById('epid') !== null)
        eventPartListLoader.loadElement();
}

function loadCompetitionList() {
    competitionListLoader.loadElement();
}

function loadAliasList() {
    aliasListLoader.loadElement();
}

function loadPersonSearchList() {
    personSearchListLoader.loadElement();
}

function loadGeoNameList() {
    geoNameListLoader.loadElement();
}

function loadNocList() {
    nocListLoader.loadElement();
}

function loadClubIdByName() {
    clubIdByNameLoader.loadElement();
}

function loadEntityInstanceList() {
    entityInstanceListLoader.loadElement();
}

function loadPhaseTypeList() {
    phaseTypeListLoader.loadElement();
}

function loadLocationRoleList() {
    locationRoleListLoader.loadElement();
}

function loadEventPartNameList() {
    eventPartNameListLoader.loadElement();
}

function loadEquipeNameList() {
    equipeListByNameLoader.loadElement();
}

function removeNonAlcifoSports() {
    const options = document.getElementById('spid').options;
    const indicesToDelete = [];

    for (let i = 0; i < options.length; i++)
        if (options[i].className !== 'alcifo')
            indicesToDelete.push(i);

    for (let i = indicesToDelete.length - 1; i >= 0; i--)
        options.remove(indicesToDelete[i]);
}

function goToEntityInstanceAliasPortal(eidFunc) {
    const eid = eidFunc();

    if (!isEmptyOrNull(eid))
        goToUrl('EntityInstancePortal', 'aeid=' + aeid + '&eid=' + eid);
}

function goToAliasPortal(eidFunc) {
    const eid = eidFunc();

    if (!isEmptyOrNull(eid))
        goToUrl('AliasPortal', 'aeid=' + aeid + '&eid=' + eid);
}

function goToManageAlias() {
    const aid = document.getElementById('aid').value;

    if (!isEmptyOrNull(aid))
        goToUrl('ManageAlias', 'aeid=' + aeid + '&aid=' + aid + '&eid=' + eid);
}

function goToPreparePerson() {
    const nm = document.getElementById('inp_ps').value;

    if (!isEmptyOrNull(nm))
        goToUrl('PreparePersonForEntityInstancePortal', 'aeid=' + aeid + '&nm=' + encodeURL(nm));
}

function goToPrepareGeo() {
    const nm = document.getElementById('gn').value;

    if (!isEmptyOrNull(nm))
        goToUrl('PrepareGeoForEntityInstancePortal', 'aeid=' + aeid + '&nm=' + encodeURL(nm));
}

function goToPrepareEquipe() {
    const nm = document.getElementById('eqn').value;

    if (!isEmptyOrNull(nm))
        goToUrl('PrepareEquipeForEntityInstancePortal', 'aeid=' + aeid + '&nm=' + encodeURL(nm));
}

function goToAddAlias() {
    goToUrl('ManageAlias', 'aeid=' + aeid + '&eid=' + eid + '&md=i');
}

function deleteAlias() {
    const aid = document.getElementById('aid').value;

    if (!isEmptyOrNull(aid))
        aliasDeleteLoader.loadElement();
}

function getSportId() {
    return document.getElementById('spid').value;
}

function getCompetitionId() {
    return document.getElementById('cid').value;
}

function getActionTypeId() {
    return document.getElementById('atid').value;
}

function getStatTypeId() {
    return document.getElementById('stid').value;
}

function getSportDisciplineKey() {
    const spid = document.getElementById('spid').value;
    const did = document.getElementById('did').value;

    return !isEmptyOrNull(spid) && !isEmptyOrNull(did) ? spid + '_' + did : null;
}

function getSportEventKey() {
    const spid = document.getElementById('spid').value;
    const eid = document.getElementById('eid').value;

    return !isEmptyOrNull(spid) && !isEmptyOrNull(eid) ? spid + '_' + eid : null;
}

function getDisciplinePartKey() {
    const spid = document.getElementById('spid').value;
    const did = document.getElementById('did').value;
    const dpid = document.getElementById('dpid').value;

    return !isEmptyOrNull(spid) && !isEmptyOrNull(did) && !isEmptyOrNull(dpid) ? spid + '_' + did + '_' + dpid : null;
}

function getEventPartKey() {
    const spid = document.getElementById('spid').value;
    const eid = document.getElementById('eid').value;
    const epid = document.getElementById('epid').value;

    return !isEmptyOrNull(spid) && !isEmptyOrNull(eid) && !isEmptyOrNull(epid) ? spid + '_' + eid + '_' + epid : null;
}

function getEntityInstanceKey() {
    const eiid = document.getElementById('eiid').value;

    return !isEmptyOrNull(eiid) ? eid + '_' + eiid : null;
}

function getNocId() {
    const nid = document.getElementById('nid').value;

    return !isEmptyOrNull(nid) ? nid : null;
}

function getClubId() {
    return document.getElementById('clid').innerText;
}

function getTeamId() {
    return document.getElementById('tid').value;
}

function getDoubleId() {
    return document.getElementById('dbid').value;
}

function getPhaseTypeId() {
    return document.getElementById('ptid').value;
}

function getLocationRoleId() {
    return document.getElementById('lrid').value;
}

function getEventPartNameId() {
    return document.getElementById('epnid').value;
}

function getResultTypeId() {
    return document.getElementById('rtid').value;
}

function handleSelectPersonName(tblRow) {
    document.getElementById('inp_ps').value = tblRow.childNodes[0].innerHTML;
}

function handleClickGeoName(tblRow) {
    document.getElementById('gn').value = tblRow.childNodes[0].innerHTML;
}

function handleClickEquipeName(tblRow) {
    document.getElementById('eqn').value = tblRow.childNodes[0].innerHTML;
}

function getProcessUrl() {
    const aid = getValueFromElementByName('inpUpd');
    const lid = getValueFromElementByName('lid');
    const cnid = getValueFromElementByName('cnid');
    const al = getValueFromElementByName('al');

    return '/ProcessManageAlias?aeid=' + aeid + '&aid=' + aid + '&eid=' + eid + '&lid=' + lid +
        '&cnid=' + cnid + '&al=' + al;
}

function checkInput() {
    const lid = getValueFromElementByName('lid');
    const cnid = getValueFromElementByName('cnid');

    if ((lid === '0' && cnid === '0') || (lid !== '0' && cnid !== '0')) {
        alert('Exactly one of language and client must be selected');
        return false;
    }

    return true;
}
