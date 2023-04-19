const languageLoader = new ElementLoader('lid', function () {
    return '/LanguageList';
}, null);

function goToManageLanguage() {
    const lid = document.getElementById('lid').value;

    if (lid !== null)
        window.location.href = path + '/ManageLanguage?id=' + lid;
}

function goToAddLanguage() {
    window.location.href = path + '/ManageLanguage?md=i';
}

function loadLanguageList() {
    languageLoader.loadElement();
}

function getProcessUrl() {
    const id = getValueFromElementByName('inpUpd');
    const nm = getValueFromElementByName('nm');
    const fbid = getValueFromElementByName('fbid');

    return '/ProcessManageLanguage?id=' + id + '&nm=' + nm + '&fbid=' + fbid;
}

function checkInput() {
    return true;
}