let sportEventListLoader;
let addCompSeasonEventLoader;

function getProcessUrl() {
    const eid = getValueFromElementByName('inpUpd');

    let params = 'cid=' + cid + '&sid=' + sid + '&spid=' + spid + '&eid=' + eid;
    params = getUpdateWithNonEmptyParameter(params, 'es', 'es');

    return '/ProcessManageCompSeasonEvent?' + params;
}

function checkInput() {
    return true;
}

function initSportEventList(url) {
    sportEventListLoader = new ElementLoader('selEid', function () {
        return '/' + url + '?cid=' + cid + '&sid=' + sid;
    }, function () {
        setElementValueFromInitStateVar('selEid', eid);
    });

    addCompSeasonEventLoader = new ElementLoader('divAdd', function () {
        const eid = document.getElementById('selEid').value;
        return !isEmptyOrNull(eid) ? '/ProcessAddCompSeasonEvent?cid=' + cid + '&sid=' + sid + '&eid=' + eid : null;
    }, sportEventListLoader.loadElement);

    sportEventListLoader.loadElement();
}

function handleAddCompSeasonEvent() {
    addCompSeasonEventLoader.loadElement();
}

function goToManageCompSeasonEvent() {
    const eid = document.getElementById('selEid').value;

    if (!isEmptyOrNull(eid))
        goToUrl('ManageCompSeasonEvent', 'cid=' + cid + '&sid=' + sid + '&eid=' + eid);
}

function goToInsertCompSeasonEventPart() {
    goToCompSeasonEventPartUrl('ManageCompSeasonEventPart', 'md=i');
}

function goToCompSeasonEventPartPortal() {
    goToCompSeasonEventPartUrl('CompSeasonEventPartPortal');
}

function goToCompSeasonEventPartUrl(url, additionalParams) {
    const eid = document.getElementById('selEid').value;
    if (!isEmptyOrNull(eid))
        goToUrl(url, 'cid=' + cid + '&sid=' + sid + '&eid=' + eid +
            (additionalParams ? '&' + additionalParams : ''));
}
