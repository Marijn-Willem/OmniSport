/**
 * Created by marij on 5-1-2017.
 */
function insertRanking(url, csepid) {
    const paramStr = 'cid=' + cid + '&sid=' + sid + '&cseid=' + cseid + '&csepid=' + csepid;
    const tableId = 'tbl_rnk_' + csepid;

    new ElementLoader(tableId, function() { return url + '?' + paramStr; }, null).loadElement();
}
