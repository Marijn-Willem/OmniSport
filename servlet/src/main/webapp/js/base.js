function isEmptyOrNull(el) {
    return el === undefined || el == null || el === '';
}

function goToUrl(url, parameters) {
    window.location.href = path + '/' + url + (!isEmptyOrNull(parameters) ? '?' + parameters : '');
}
