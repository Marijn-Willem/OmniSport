/**
 * Created by marij on 5-1-2017.
 */
function insertRanking(url, epid) {
    const paramStr = 'cid=' + cid + '&sid=' + sid + '&eid=' + eid + '&epid=' + epid;
    const tableId = 'tbl_rnk_' + epid;

    new ElementLoader(tableId, function() { return url + '?' + paramStr; }, null).loadElement();
}