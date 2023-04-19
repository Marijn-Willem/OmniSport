/**
 * Created by marij on 17-1-2017.
 */
function processImport(cid, sid, pid) {
    new ElementLoader('resp', function () {
        var fl = document.getElementById('fl').value;
        if (fl !== null)
            return '/ProcessSeedingImport?cid=' + cid + '&sid=' + sid + '&pid=' + pid + '&fl=' + fl;

        return null;
    }, null).loadElement();
}
