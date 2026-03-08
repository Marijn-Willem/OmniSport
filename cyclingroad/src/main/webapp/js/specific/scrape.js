function processScrape() {
    new ElementLoader('divRes', function () {
        return '/ProcessScrape?cid=' + cid + '&sid=' + sid + '&cseid=' + cseid + '&csepid=' + csepid;
    }, null, document.getElementById('divInp').value).loadElement();
}
