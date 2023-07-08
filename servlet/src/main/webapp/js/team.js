const addNamesToSessionUrl = 'AddTeamNamesToSession';

const teamListLoader = new ElementLoader('tid', function () {
    const spid = document.getElementById('spid').value;
    const gid = document.getElementById('gid').value;

    return '/TeamListBySportGender?spid=' + spid + '&gid=' + gid;
}, function() { setElementValueFromInitStateVar('tid', tid); });

const sportListLoader = new ElementLoader('spid', function () {
    return '/SportList';
}, function() {
    setElementValueFromInitStateVar('spid', spid);
    setElementValueFromInitStateVar('gid', gid);

    teamListLoader.loadElement();
});

function getName() {
    const options = document.getElementById('tid').options;

    for (let i = 0; i < options.length; i++) {
        const option = options[i];
        if (option.selected)
            return option.innerHTML;
    }

    return '';
}
