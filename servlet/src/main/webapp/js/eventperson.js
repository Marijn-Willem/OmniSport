/**
 * Created by marij on 13-12-2016.
 */
const processEventPersonImportLoader = new ElementLoader('resp', function () {
    return '/ProcessEventPersonImport?cid=' + cid + '&sid=' + sid + '&eid=' + eid;
}, null);