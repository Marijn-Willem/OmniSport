let getSpecificParameters = function () { return null; };

function getProcessUrl() {
    const mid = getValueFromElementByName('inpUpd');
    const pid = getValueFromElementByName('pid');
    const p1id = getValueFromElementByName('p1id');
    const p2id = getValueFromElementByName('p2id');
    const p1s = getValueFromElementByName('p1s');
    const p2s = getValueFromElementByName('p2s');
    const dt = getValueFromElementByName('dt');
    const p1st = getValueFromCheckbox('p1st');

    const specificParameters = getSpecificParameters();

    return '/ProcessManageH2HMatch?cid=' + cid + '&sid=' + sid + '&mid=' + mid + '&pid=' + pid +
        '&p1id=' + p1id + '&p2id=' + p2id + '&p1s=' + p1s + '&p2s=' + p2s +
        '&dt=' + dt + '&p1st=' + p1st + (!isEmptyOrNull(specificParameters) ? '&' + specificParameters : '');
}

function checkInput() {
    const selectedPhase = getSelectedOption('pid');
    const dt = getValueFromElementByName('dt');

    const checkDateRange = () => {
        if (dt !== '') {
            const date = convertStringToDate(dt);

            const dts = selectedPhase.getAttribute('dts');
            const dte = selectedPhase.getAttribute('dte');

            return (!dts || date >= convertStringToDate(dts)) && (!dte || date <= convertStringToDate(dte));
        }

        return true;
    };

    return doCheckAndAlert(!isEmptyOrNull(selectedPhase?.value), 'No phase selected!') &&
        validateDatetime('dt') &&
        doCheckAndAlert(checkDateRange(), 'Date is not within range of phase');
}

function handleChangePhase() {
    const pid = getValueFromElementByName('pid');

    if (!isEmptyOrNull(pid)) {
        processOptions(document.getElementsByName('p1id')[0].options, pid);
        processOptions(document.getElementsByName('p2id')[0].options, pid);
    }
}

function processOptions(options, pid) {
    for (let i = 1; i < options.length; i++) {
        const option = options[i];
        option.style.display = optionContainsPid(option, pid) ? 'block' : 'none';
    }
}

function optionContainsPid(option, pid) {
    const pids = option.getAttribute('pid').split(',');
    for (let i = 0; i < pids.length; i++)
        if (pids[i] === pid)
            return true;

    return false;
}