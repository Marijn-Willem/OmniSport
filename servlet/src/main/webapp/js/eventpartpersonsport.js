const processManageUrl = 'ProcessManageEventPartPersonSports';

const getSpecificParameters = () => {
    return '';
}

function checkAll() {
    const cbAll = document.getElementById('cb_all');
    const inputs = document.getElementsByName('pid');

    for (let i = 0; i < inputs.length; i++)
        inputs[i].checked = cbAll.checked;
}