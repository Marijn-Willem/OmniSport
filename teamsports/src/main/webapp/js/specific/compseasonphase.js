const compSeasonPhaseListLoader = new ElementLoader('selPid', function () {
    return '/CompSeasonPhaseList?cid=' + cid + '&sid=' + sid + '&sd=false';
}, function() {
    setElementValueFromInitStateVar('selPid', pid);
    handleChangePid();
});

function initCompSeasonPhaseList() {
    compSeasonPhaseListLoader.loadElement();
}

function handleChangePid() {
    const selOption = getSelectedOption('selPid');
    const isKo = selOption !== null && selOption.classList.contains('ko');

    document.getElementById('btnPt').disabled = isKo;
    document.getElementById('btnKo').disabled = !isKo;
}
