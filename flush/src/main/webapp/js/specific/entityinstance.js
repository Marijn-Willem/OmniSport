const competitionLoader = new ElementLoader('cid', function () {
    return '/CompetitionListFull';
}, loadSeasonList);

const seasonLoader = new ElementLoader('sid', function () {
    const cid = document.getElementById('cid').value;

    return !isEmptyOrNull(cid) ? '/SeasonList?cid=' + cid : null;
}, null);

const nocLoader = new ElementLoader('nid', function () {
    return '/NocList';
}, null);

function getFlushUrl() {
    const en = document.getElementById('en').value;
    const eas = getEntityAsString();

    const cidSel = document.getElementById('cid');
    const sidSel = document.getElementById('sid');

    if (cidSel !== null && sidSel !== null) {
        const cid = cidSel.value;
        const sid = sidSel.value;

        return !isEmptyOrNull(cid) && !isEmptyOrNull(sid) ?
            '/FlushEntityInstanceCompSeason?cid=' + cid + '&sid=' + sid + '&en=' + en + '&eas=' + eas : null;
    } else
        return '/FlushEntityInstanceNonCompSeason?en=' + en + '&eas=' + eas;
}

function init() {
    loadNocList();
    if (document.getElementById('cid') !== null)
        loadCompetitionList();
    setVisibility();
}

function loadCompetitionList() {
    competitionLoader.loadElement();
}

function loadSeasonList() {
    seasonLoader.loadElement();
}

function loadNocList() {
    nocLoader.loadElement();
}

function setVisibility() {
    const en = document.getElementById('en').value;

    document.getElementById('nm').style.display = en === 'Club' ? 'inline' : 'none';
    document.getElementById('tblNm').style.display = en === 'Club' ? 'table' : 'none';

    document.getElementById('gn').style.display = en === 'Geo' ? 'inline' : 'none';
    document.getElementById('tblGn').style.display = en === 'Geo' ? 'table' : 'none';

    document.getElementById('nid').style.display = en === 'Noc' ? 'inline' : 'none';

    document.getElementById('inp_pls').style.display = en === 'Person' ? 'inline' : 'none';
    document.getElementById('tbl_pls').style.display = en === 'Person' ? 'table' : 'none';
}

function getEntityAsString() {
    const en = document.getElementById('en').value;

    let elementId;

    if (en === 'Club')
        elementId = 'nm';
    else if (en === 'Geo')
        elementId = 'gn';
    else if (en === 'Noc')
        elementId = 'nid';
    else
        elementId = 'inp_pls';

    return encodeURL(document.getElementById(elementId).value);
}
