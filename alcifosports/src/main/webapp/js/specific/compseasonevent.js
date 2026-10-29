function goToPrepareEventParticipantImport() {
    const cseid = document.getElementById('selCseid').value;
    if (!isEmptyOrNull(cseid))
        goToUrl('PrepareEventParticipantImport', 'cid=' + cid + '&sid=' + sid + '&cseid=' + cseid);
}
