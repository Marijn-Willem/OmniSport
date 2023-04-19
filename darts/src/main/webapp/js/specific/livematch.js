/**
 * Created by marij on 24-12-2016.
 */
let score1 = 501;
let score2 = 501;

function addScore(person) {
    const scr = document.getElementById('scr' + person).value;
    const drt = document.getElementById('drt' + person).value;

    const p_sc = document.getElementById('p' + person + 'sc');

    p_sc.value = concatStringsWithDelimiter(p_sc.value, scr + '_' + drt, '|');

    if (person === 1) {
        score1 -= parseInt(scr);

        if (score1 === 0) {
            document.getElementById('p1w').value = 'true';
            document.forms[0].submit();
        }
        else
            document.getElementById('sc_cur1').innerHTML = score1;
    }
    else {
        score2 -= parseInt(scr);

        if (score2 === 0) {
            document.getElementById('p1w').value = 'false';
            document.forms[0].submit();
        }
        else
            document.getElementById('sc_cur2').innerHTML = score2;
    }
}

function addMisDub(person, up) {
    const md = document.getElementById('p' + person + 'md');

    md.value = parseInt(md.value) + (up ? 1 : -1);
}