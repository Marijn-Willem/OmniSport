/**
 * Created by marij on 2-12-2016.
 */
const timerFunc = function () {
    document.getElementById('tmr').value = convertMsToTimeString(time);
    time += 100;
};
let time;
let timerId;

const curLaps = [0, 0];

function startTimer() {
    if (timerId !== null)
        clearInterval(timerId);

    time = 0;
    timerId = setInterval(timerFunc, 100);

    document.getElementById('btnLap1').style.display = 'inline';
    document.getElementById('btnLap2').style.display = 'inline';
}

function clearTimer() {
    clearInterval(timerId);
}

function newLap(person) {
    let curInput = document.getElementById('p' + person + '_' + curLaps[person - 1]++);
    curInput.value = convertMsToTimeString(time);

    curInput = document.getElementById('p' + person + '_' + curLaps[person - 1]);

    if (curInput === null) {
        const btn = document.getElementById('btnLap' + person);
        btn.style.display = 'none';

        const otherPerson = (person % 2) + 1;
        const otherBtn = document.getElementById('btnLap' + otherPerson);

        if (otherBtn.style.display === 'none') {
            clearTimer();
            document.getElementById('inp_sbt').style.display = 'inline';
        }
    }
}

function processPersonSelect(person) {
    const otherPerson = (person % 2) + 1;

    const curSelect = document.getElementsByName('p' + person + 'id')[0];
    const otherSelect = document.getElementsByName('p' + otherPerson + 'id')[0];

    let selectedId;

    for (let i = 0; i < curSelect.options.length; i++) {
        const option = curSelect.options[i];

        if (option.selected)
            selectedId = option.value;

        option.style.display = 'block';
    }

    for (let i = 0; i < otherSelect.options.length; i++) {
        const option = otherSelect.options[i];

        if (option.value === selectedId)
            option.style.display = 'none';
        else
            option.style.display = 'block';
    }
}

function submitFinishTimes() {
    const intermediates = document.getElementsByClassName('interm');

    for (let i = 0; i < intermediates.length; i++) {
        const timeStr = convertMsToTimeString(0);

        intermediates[i].childNodes[0].childNodes[0].value = timeStr;
        intermediates[i].childNodes[2].childNodes[0].value = timeStr;
    }

    submit();
}

function submit() {
    if (submitCheck()) {
        const p1id = document.getElementsByName('p1id')[0].value;
        const p2id = document.getElementsByName('p2id')[0].value;

        let params = 'cid=' + cid + '&sid=' + sid + '&cseid=' + cseid + '&csepid=' + csepid +
            '&p1id=' + p1id + '&p2id=' + p2id;

        const timeFields = document.getElementsByClassName('time');

        for (let i = 0; i < timeFields.length; i++) {
            const element = timeFields[i];
            params += '&';
            params += element.name
            params += '=';
            params += element.value;
        }

        goToUrl('AddHeat', params);
    }
}

function submitCheck() {
    const p1id = document.getElementsByName('p1id')[0].value;
    const p2id = document.getElementsByName('p2id')[0].value;

    return doCheckAndAlert(p1id !== '0' && p2id !== '0', 'Choose a person!');
}
