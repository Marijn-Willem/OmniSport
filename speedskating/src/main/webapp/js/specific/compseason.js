function calculateEventPersonSportRanks(cseid) {
    new ElementLoader('divCalc', function () {
        const cid = document.getElementById('selCid').value;
        const sid = document.getElementById('selSid').value;

        return !isEmptyOrNull(cid) && !isEmptyOrNull(sid) ?
            '/ProcessCalculateEventPersonSportRanks?cid=' + cid + '&sid=' + sid + '&cseid=' + cseid
            : null;
    }, null).loadElement();
}
