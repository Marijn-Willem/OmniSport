/**
 * Created by marij on 17-1-2017.
 */
function processImport(cid, sid) {
    new ElementLoader('resp', function () {
        return '/ProcessCompSeasonPersonImport?cid=' + cid + '&sid=' + sid;
    }, null).loadElement();
}
