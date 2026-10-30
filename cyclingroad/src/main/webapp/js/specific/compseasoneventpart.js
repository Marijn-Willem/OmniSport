const setGeneralClassificationPointsLoader = new ElementLoader('divIns', function () {
    return getUrlWithParameters('ProcessSetGeneralClassificationPoints');
}, null);

function setGeneralClassificationPoints() {
    setGeneralClassificationPointsLoader.loadElement();
}

function scrape() {
    const urlParameters = getUrlParameters();

    if (!isEmptyOrNull(urlParameters))
        goToUrl('Scrape', urlParameters);
}

function getUrlWithParameters(url) {
    const urlParameters = getUrlParameters();

    return !isEmptyOrNull(urlParameters) ? '/' + url + '?' + urlParameters : null;
}
