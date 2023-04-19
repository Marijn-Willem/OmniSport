const addCompSeasonTeamPersonSportLoader = new ElementLoader('divRes', function () {
    return '/ProcessManageCompSeasonTeamPersonSports?cid=' + cid + '&sid=' + sid + '&tid=' + tid +
        getParamStringFromNameValues('psid');
}, disableCheckboxes);

function addCompSeasonTeamPersonSports() {
    addCompSeasonTeamPersonSportLoader.loadElement();
}

function disableCheckboxes() {
    const checkboxes = document.getElementsByName('psid');

    for (let i = 0; i < checkboxes.length; i++)
        checkboxes[i].disabled = true;
}