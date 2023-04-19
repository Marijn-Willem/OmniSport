/**
 * Created by marij on 31-12-2016.
 */
const statLoader = new ElementLoader('tbl_set_stats',
    function () {
        var mpId = document.getElementById('mpid').value;

        return mpId !== '' ? '/SetStats?mpid=' + mpId : null;
    }, null);

function fillSetStats() {
    statLoader.loadElement();
}
