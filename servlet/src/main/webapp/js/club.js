const clubNameListLoader = new ElementLoader('tblNm', function () {
    const nm = document.getElementById('nm').value;
    return '/ClubListByName?nm=' + nm;
}, null);

function handleClickClubName(tableRow) {
    document.getElementById('nm').value = tableRow.childNodes[0].innerHTML;
}
