const flushLoader = new ElementLoader('div_res', function() { return getFlushUrl(); }, null);

function flush() {
    flushLoader.loadElement();
}
