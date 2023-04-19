/**
 * Created by marij on 11-6-2017.
 */
function processImport(cid, sid) {
    new ElementLoader('resp', function () {
        return '/ProcessCompSeasonDoublesImport?cid=' + cid + '&sid=' + sid;
    }, null).loadElement();
}