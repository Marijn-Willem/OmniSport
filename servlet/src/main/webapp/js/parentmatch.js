const childMatchLoader = new ElementLoader('tblChildMatch', function () {
    return '/MatchesParentMatch?cid=' + cid + '&sid=' + sid  + '&pmid=' + pmid;
}, null);

function loadChildMatches() {
    childMatchLoader.loadElement();
}

function goToAddChildMatch() {
    goToUrl('ManageH2HMatch', 'cid=' + cid + '&sid=' + sid + '&pid=' + pid + '&pmid=' + pmid + '&md=i');
}
