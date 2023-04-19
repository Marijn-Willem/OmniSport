const addPhaseParticipantsLoader = new ElementLoader('div_pp', function () {
    return '/AddParticipantsToCompSeasonPhase?cid=' + cid + '&sid=' + sid + '&pid=' + pid +
        getParamStringFromNameValues('ptid');
}, null);