let getSpecificParameters = function () { return null; };

function getProcessUrl() {
    const mid = getValueFromElementByName('inpUpd');
    const p1id = getValueFromElementByName('p1id');
    const p2id = getValueFromElementByName('p2id');
    const p1s = getValueFromElementByName('p1s');
    const p2s = getValueFromElementByName('p2s');
    const dt = getValueFromElementByName('dt');
    const p1st = getValueFromCheckbox('p1st');

    const specificParameters = getSpecificParameters();

    return '/ProcessManageH2HMatch?cid=' + cid + '&sid=' + sid + '&pid=' + pid + '&mid=' + mid +
        '&p1id=' + p1id + '&p2id=' + p2id + '&p1s=' + p1s + '&p2s=' + p2s +
        '&dt=' + dt + '&p1st=' + p1st + (!isEmptyOrNull(specificParameters) ? '&' + specificParameters : '');
}

function checkInput() {
    const dt = getValueFromElementByName('dt');

    const checkDateRange = () => {
        if (dt !== '') {
            const date = convertStringToDate(dt);
            return (!dts || date >= convertStringToDate(dts)) && (!dte || date <= convertStringToDate(dte));
        }

        return true;
    };

    return doCheckAndAlert(validateDatetime('dt')) &&
        doCheckAndAlert(checkDateRange(), 'Date is not within range of phase');
}
