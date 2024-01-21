const languageLoader = new ElementLoader('selLid', function () {
    return '/LanguageList';
}, function () { setElementValueFromInitStateVar('selLid', lid); });

function goToManageLanguage() {
    const lid = document.getElementById('selLid').value;

    if (lid !== null)
        window.location.href = path + '/ManageLanguage?lid=' + lid;
}

function goToAddLanguage() {
    window.location.href = path + '/ManageLanguage?md=i';
}

function loadLanguageList() {
    languageLoader.loadElement();
}

function getProcessUrl() {
    const lid = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');
    const fbid = getValueFromElementByName('fbid');

    return '/ProcessManageLanguage?lid=' + lid + '&nm=' + nm + '&fbid=' + fbid;
}

function checkInput() {
    return true;
}
