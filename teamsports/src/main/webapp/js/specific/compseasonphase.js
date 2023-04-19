const postProcessPhaseListLoad = function () { handleChangePid(); }

function handleChangePid() {
    const selOption = getSelectedOption('selPid');

    document.getElementById('btnPt').disabled = selOption !== null && selOption.classList.contains('ko');
}
