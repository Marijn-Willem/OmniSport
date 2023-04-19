getSpecificParameters = function () {
    const p1ss = getValueFromElementByName('p1ss');
    const p2ss = getValueFromElementByName('p2ss');

    return 'p1ss=' + p1ss + '&p2ss=' + p2ss;
}