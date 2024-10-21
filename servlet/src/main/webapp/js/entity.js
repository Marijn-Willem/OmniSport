const dateRegEx = /\b\d{8}\b/;
const dateTimeRegex = /\b\d{8}\s\d{2}:\d{2}:\d{2}\b/;
const pointRegex = /\d+\.\d+/;
const googleMapsUrl = 'https://www.google.com/maps/';

function update(funcValidate, getProcessUrl) {
    if (funcValidate())
        getUpdateLoader(function () {
            return getProcessUrl() + '&md=' + md;
        }).loadElement();
}

function changeNumericText(name, forward) {
    if (md !== 'r') {
        const tb = document.getElementsByName(name)[0];
        let curVal = convertStringToNumericVal(tb.value);

        let newVal;

        if (forward)
            newVal = convertNumericValToString(++curVal);
        else
            newVal = convertNumericValToString(curVal !== -1 ? --curVal : curVal);

        tb.value = newVal;
    }
}

function validateDatetime(name) {
    const tb = document.getElementsByName(name)[0];
    const curVal = tb.value;

    return doCheckAndAlert(curVal === '-' || testDate(curVal) || testDateTime(curVal), 'Invalid date');
}

function validateNumericTextFieldNonNull(name) {
    const curVal = document.getElementsByName(name)[0].value;

    return doCheckAndAlert(convertStringToNumericVal(curVal) !== 0, 'Input must be numeric');
}

function validatePoint(name) {
    const x = document.getElementsByName(name + '_1')[0].value;
    const y = document.getElementsByName(name + '_2')[0].value;

    return (isEmptyOrNull(x) && isEmptyOrNull(y)) || (doCheckAndAlert(!isEmptyOrNull(x) && !isEmptyOrNull(y),
            'Both point coordinates must be filled') &&
        doCheckAndAlert(pointRegex.test(x) && pointRegex.test(y),
            'Point coordinates must be valid doubles'));
}

function getValueFromElementByName(name) {
    return document.getElementsByName(name)[0].value;
}

function getValueFromElementByNameOrNull(name) {
    return document.getElementsByName(name).length > 0 ? getValueFromElementByName(name) : null;
}

function getValueFromCheckbox(name) {
    return document.getElementsByName(name)[0].checked;
}

function getUpdateWithNonEmptyParameter(params, elemName, paramName) {
    const elem = getValueFromElementByName(elemName);
    return params + (testNonEmptyParameter(elem) ? '&' + paramName + '=' + elem : '');
}

function getValueFromPointFields(name) {
    return document.getElementsByName(name + '_1')[0].value + '|' +
        document.getElementsByName(name + '_2')[0].value;
}

function getUpdateLoader(getProcessUrl) {
    return new ElementLoader('inpUpd', getProcessUrl, function () {
        const inpUpd = document.getElementById('inpUpd');
        const text = (md === 'u' ? 'Update' : 'Insert') + ' successful';

        if (md === 'i' && entIdNm && returnPath) {
            const delimiter = returnPath.includes('?') ? '&' : '?';
            returnPath = returnPath + delimiter + entIdNm + '=' + inpUpd.innerText;
        }

        md = 'u';
        document.getElementById('divUpd').innerHTML = text;
        document.getElementById('btnUpd').value = 'Update';
        inpUpd.value = inpUpd.innerText;
    });
}

function showCoordinatesInGoogleMaps(name) {
    const x = document.getElementsByName(name + '_1')[0].value;
    const y = document.getElementsByName(name + '_2')[0].value;

    if ((!isEmptyOrNull(x) || !isEmptyOrNull(y)) && validatePoint(name))
        window.open(googleMapsUrl + '@' + x + ',' + y + ',17z');
}

function convertNumericValToString(nVal) {
    return nVal === -1 ? '-' : nVal.toString();
}

function convertStringToNumericVal(sVal) {
    return sVal === '-' ? -1 : parseInt(sVal);
}

function testNonEmptyParameter(val) {
    return !isEmptyOrNull(val) && val !== '-';
}

function testDate(val) {
    return val.length === 8 && dateRegEx.test(val);
}

function testDateTime(val) {
    return val.length === 17 && dateTimeRegex.test(val);
}

function convertStringToDate(str) {
    let inputStr = str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8) + "T";

    if (testDateTime(str))
        inputStr += str.substring(9) + "Z";
    else
        inputStr += "00:00:00Z";

    return new Date(inputStr);
}
