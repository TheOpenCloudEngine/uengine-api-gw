function run() {
    var stdout = '';
    var log = function (obj) {
        console.log(obj);
        stdout = stdout + obj + '\n\n';
    };
    var customScript = function () {
        ${script}
    };

    var customResult = customScript();
    var type = typeof customResult;
    var isArray = Array.isArray(customResult);

    if (type == 'boolean') {
        type = 'boolean';
    }
    else if (!customResult) {
        type = 'undefined';
        customResult = null;
    }
    else if (isArray) {
        type = 'array';
        customResult = JSON.stringify(customResult);
    }
    else if (type == 'object') {
        type = 'object';
        customResult = JSON.stringify(customResult);
    }
    else if (type == 'number') {
        type = 'number';
    }
    else if (type == 'string') {
        type = 'string';
    }

    return {
        log: stdout,
        value: customResult,
        type: type
    };
}