let sportEventListLoader;

function getProcessUrl() {
    const cseid = getValueFromElementByName('inpUpd');

    let params = 'cid=' + cid + '&sid=' + sid + '&cseid=' + cseid;
    params = getUpdateWithNonEmptyParameter(params, 'seid', 'seid');
    params = getUpdateWithNonEmptyParameter(params, 'gid', 'gid');
    params = getUpdateWithNonEmptyParameter(params, 'es', 'es');

    return '/ProcessManageCompSeasonEvent?' + params;
}

function checkInput() {
    return doCheckAndAlert(!isEmptyOrNull(getValueFromElementByName('seid')),
        'Sport event is mandatory');
}

function initSportEventList() {
    sportEventListLoader = new ElementLoader('selCseid', function () {
        return '/SportEventListByCompSeason?cid=' + cid + '&sid=' + sid;
    }, function () {
        setElementValueFromInitStateVar('selCseid', cseid);
    });

    sportEventListLoader.loadElement();
}

function goToManageCompSeasonEvent() {
    const cseid = document.getElementById('selCseid').value;

    if (!isEmptyOrNull(cseid))
        goToUrl('ManageCompSeasonEvent', 'cid=' + cid + '&sid=' + sid + '&cseid=' + cseid);
}

function goToInsertCompSeasonEventPart() {
    goToCompSeasonEventPartUrl('ManageCompSeasonEventPart', 'md=i');
}

function goToCompSeasonEventPartPortal() {
    goToCompSeasonEventPartUrl('CompSeasonEventPartPortal');
}

function goToCompSeasonEventPartUrl(url, additionalParams) {
    const cseid = document.getElementById('selCseid').value;
    if (!isEmptyOrNull(cseid))
        goToUrl(url, 'cid=' + cid + '&sid=' + sid + '&cseid=' + cseid +
            (additionalParams ? '&' + additionalParams : ''));
}
