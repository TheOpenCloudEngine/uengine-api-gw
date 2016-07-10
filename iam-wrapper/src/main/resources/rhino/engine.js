function run() {
    inputData = JSON.parse(inputData);
    var normal = inputData.normal;
    var json = inputData.json;
    for (var key in normal) {
        var value = normal[key];
        eval('var ' + key + ' = value;');
    }
    for (var key in json) {
        var value = JSON.parse(json[key]);
        eval('var ' + key + ' = value;');
    }

    var stdout = '';
    var log = function (obj) {
        console.log(obj);
        stdout = stdout + obj + '\n\n';
    };

    var FunctionResponse = function (flow, data) {
        return {
            flow: flow,
            data: data
        }
    };

    var HttpRequest = function (uri, method, headers, body) {
        return {
            uri: uri,
            method: method,
            headers: headers,
            body: body
        }
    };

    var HttpResponse = function (status, headers, entity) {
        return {
            status: status,
            headers: headers,
            entity: entity
        }
    };

    var ProxyRequest = function (host, path) {
        return {
            host: host,
            path: path
        }
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