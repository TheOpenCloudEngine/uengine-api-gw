# OCE IAM

## CouchDB Schema And View

### iam

Document

```
{
   "_id": "61d499104f264bb78383ec8b5fc8931e",
   "_rev": "6-2c729a9a658d9e3824babeab95e794a8",
   "host": "52.79.164.208",
   "port": 8080,
   "managementKey": "f4562539-2937-4982-912c-f7bd24024510",
   "managementSecret": "df872f4d-6c02-465d-b4ae-6739f0d70ceb",
   "regDate": 1465985340420,
   "updDate": 1466490568613,
   "docType": "iam"
}
```

View

```
{
  "_id": "_design/iam",
  "language": "javascript",
  "views": {
    "select": {
      "map": "function(doc) {if(doc.docType == \"iam\"){emit(null,doc); }}"
    }
  }
}
```

### policy

Document

```

Source
{
   "_id": "6718173f8cc94fb68c4380af3acbaa65",
   "_rev": "37-0ab3cb1d62267e27c2e8beedea106f83",
   "name": "CloudantPolicy",
   "authentication": "Y",
   "tokenLocation": "header",
   "tokenName": "ACCESS-TOKEN",
   "proxyUri": "http://52.79.164.208:5984",
   "prefixUri": "/cloudant,/",
   "beforeUse": "var scopes = scope.split(',');\r\nif(scopes[0]){\r\n   return true;\r\n}else{\r\n   return false;\r\n}",
   "afterUse": "return user;",
   "regDate": 1466486064279,
   "updDate": 1467940969528,
   "docType": "policy"
}
```

View

```
{
  "_id": "_design/policy",
  "language": "javascript",
  "views": {
    "select": {
      "map": "function(doc) { if(doc.docType == \"policy\"){ emit(null, doc); }}"
    },
    "selectById": {
      "map": "function(doc) { if(doc.docType == \"policy\"){ emit([doc._id], doc); }}"
    },
    "selectLikeName": {
      "map": "function(doc) { if(doc.docType == \"policy\"){ var words = {}; doc.name.replace(/\\w+/g, function(word) { words[word.toLowerCase()] = true}); for(w in words) { emit([w], doc); }}}"
    },
    "count": {
      "map": "function(doc) { if(doc.docType == \"policy\"){ emit(null, null); }}",
      "reduce": "_count"
    },
    "countLikeName": {
      "map": "function(doc) { if(doc.docType == \"policy\"){ var words = {}; doc.name.replace(/\\w+/g, function(word) { words[word.toLowerCase()] = true}); for(w in words) { emit([w], doc); }}}",
      "reduce": "_count"
    },
    "selectByName": {
      "map": "function(doc) {if(doc.docType == \"policy\"){emit([doc.name],doc); }}"
    }
  }
}
```

### uris

Document

```
{
   "_id": "4f50de965610417cb0f88cebe2d58b9d",
   "_rev": "10-ffc3b7fcea8da7c867fbb0f1d392b0f8",
   "order": 3,
   "uri": "/cloudant/*",
   "method": "GET,POST,PUT,DELETE,HEAD",
   "runWith": "policy",
   "wid": "",
   "className": "CloudantHandler",
   "policyId": "6718173f8cc94fb68c4380af3acbaa65",
   "regDate": 1466058133567,
   "updDate": 1467187500496,
   "docType": "uris"
}
```

View

```
{
  "_id": "_design/uris",
  "language": "javascript",
  "views": {
    "select": {
      "map": "function(doc) { if(doc.docType == \"uris\"){ emit(null, doc); }}"
    },
    "selectById": {
      "map": "function(doc) { if(doc.docType == \"uris\"){ emit([doc._id], doc); }}"
    },
    "selectByOrder": {
      "map": "function(doc) { if(doc.docType == \"uris\"){ emit([doc.order], doc); }}"
    },
    "selectLikeUri": {
      "map": "function(doc) { if(doc.docType == \"uris\"){ var words = {}; doc.uri.replace(/\\w+/g, function(word) { words[word.toLowerCase()] = true}); for(w in words) { emit([w], doc); }}}"
    },
    "count": {
      "map": "function(doc) { if(doc.docType == \"uris\"){ emit(null, null); }}",
      "reduce": "_count"
    },
    "countLikeUri": {
      "map": "function(doc) { if(doc.docType == \"uris\"){ var words = {}; doc.uri.replace(/\\w+/g, function(word) { words[word.toLowerCase()] = true}); for(w in words) { emit([w], doc); }}}",
      "reduce": "_count"
    },
    "selectByUri": {
      "map": "function(doc) {if(doc.docType == \"uris\"){emit([doc.uri],doc); }}"
    }
  }
}
```

### workflow

Document

```
{
   "_id": "f3be41747fd947b9a14df32efd0bb5b1",
   "_rev": "28-ec3b8c5656572ee4c4f957e5e040945a",
   "name": "BasicApi",
   "designer_xml": "<?xml version=\"1.0\" encoding=\"UTF-8\"?><opengraph....",
   "bpmn_xml": "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<definitions....",
   "vars": "{\"OG_2909_4\":{\"shapeId\":\"OG.shape.router.Authentication....",
   "steps": 0,
   "regDate": 1467700537355,
   "updDate": 1467941505000,
   "docType": "workflow"
}
```

View

```
{
  "_id": "_design/workflow",
  "language": "javascript",
  "views": {
    "select": {
      "map": "function(doc) { if(doc.docType == \"workflow\"){ emit(null, doc); }}"
    },
    "selectById": {
      "map": "function(doc) { if(doc.docType == \"workflow\"){ emit([doc._id], doc); }}"
    },
    "selectLikeName": {
      "map": "function(doc) { if(doc.docType == \"workflow\"){ var words = {}; doc.name.replace(/\\w+/g, function(word) { words[word.toLowerCase()] = true}); for(w in words) { emit([w], doc); }}}"
    },
    "count": {
      "map": "function(doc) { if(doc.docType == \"workflow\"){ emit(null, null); }}",
      "reduce": "_count"
    },
    "countLikeName": {
      "map": "function(doc) { if(doc.docType == \"workflow\"){ var words = {}; doc.name.replace(/\\w+/g, function(word) { words[word.toLowerCase()] = true}); for(w in words) { emit([w], doc); }}}",
      "reduce": "_count"
    },
    "selectByName": {
      "map": "function(doc) {if(doc.docType == \"workflow\"){emit([doc.name],doc); }}"
    }
  }
}
```

### workflow_history

Document

```
{
   "_id": "1128cba8e10740de81d333f1f0d44ff9",
   "_rev": "6-4290a24bb6ef76a492931648c66af545",
   "identifier": "6956273a-bf80-4663-8c74-54bfee93da08",
   "wid": "f3be41747fd947b9a14df32efd0bb5b1",
   "name": "BasicApi",
   "vars": "{\"OG_2909_4\":{\"shapeId\":\"OG.shape.router.Aut....",
   "startDate": 1467909722383,
   "endDate": 1467909762396,
   "duration": 40013,
   "currentTaskId": "OG_2909_474",
   "currentTaskName": "Api",
   "status": "FAILED",
   "docType": "workflow_history"
}
```

View

```
{
  "_id": "_design/workflow_history",
  "language": "javascript",
  "views": {
    "select": {
      "map": "function(doc) { if(doc.docType == \"workflow_history\"){ emit(null, doc); }}"
    },
    "selectByIdentifier": {
      "map": "function(doc) { if(doc.docType == \"workflow_history\"){ emit([doc.identifier], doc); }}"
    },
    "selectById": {
      "map": "function(doc) { if(doc.docType == \"workflow_history\"){ emit([doc._id], doc); }}"
    },
    "selectLikeName": {
      "map": "function(doc) { if(doc.docType == \"workflow_history\"){ var words = {}; doc.name.replace(/\\w+/g, function(word) { words[word.toLowerCase()] = true}); for(w in words) { emit([w], doc); }}}"
    },
    "count": {
      "map": "function(doc) { if(doc.docType == \"workflow_history\"){ emit(null, null); }}",
      "reduce": "_count"
    },
    "countLikeName": {
      "map": "function(doc) { if(doc.docType == \"workflow_history\"){ var words = {}; doc.name.replace(/\\w+/g, function(word) { words[word.toLowerCase()] = true}); for(w in words) { emit([w], doc); }}}",
      "reduce": "_count"
    }
  }
}
```

### task_history

Document

```
{
   "_id": "662c26de92714d12a6ff1702ef946355",
   "_rev": "2-0a86f16b155a771a27b26ee677d65baa",
   "identifier": "b822b5bd-c876-4274-9918-1ca75aae0b90",
   "wid": "f3be41747fd947b9a14df32efd0bb5b1",
   "taskId": "OG_2909_4",
   "taskName": "Authentication",
   "startDate": 1467941581229,
   "endDate": 1467941582587,
   "duration": 1358,
   "status": "FINISHED",
   "input": "{\"tokenLocation\":\"header\",\"tokenName\":\"ACCESS-TOKEN\"}",
   "output": "{\"tokenName\":\"ACCESS-TOKEN\",\"tokenLocation\"....",
   "docType": "task_history"
}
```

View

```
{
  "_id": "_design/task_history",
  "language": "javascript",
  "views": {
    "selectByIdentifier": {
      "map": "function(doc) { if(doc.docType == \"task_history\"){ emit([doc.identifier], doc); }}"
    },
    "selectById": {
      "map": "function(doc) { if(doc.docType == \"task_history\"){ emit([doc._id], doc); }}"
    }
  }
}
```


