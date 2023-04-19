const copyCompSeasonLoader = new ElementLoader('divResp', function () {
    const sidt = document.getElementById('selSid').value;

    if (sidt !== null)
        return '/ProcessCopyCompSeason?cid=' + cid + '&sid=' + sid + '&sidt=' + sidt;

    return null;
}, null);
