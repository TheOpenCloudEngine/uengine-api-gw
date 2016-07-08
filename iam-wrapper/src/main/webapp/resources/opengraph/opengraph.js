var canvas = null, opengraphJSON, ele1;
var canvasCtl = {
    canvas: null,
    init: function () {
        var ctl = this;
        var canvasEle = $('#canvas');
        var iconShape = $(".icon_shape");

        //프라퍼티 윈도우 폼 밸리데이션(visible 상태가 아닌것도 체크한다.)
        $('[name=propertyWindow]').each(function () {
            ctl.initPropertyWindow($(this));
        });

        iconShape.css({
            width: '30px',
            height: '30px',
            cursor: 'pointer'
        });
        canvasEle.css({
            padding: 0,
            overflow: 'scroll'
        });

        // Canvas
        canvas = new OG.Canvas('canvas', [1000, 500], 'white', 'url(/service-console/resources/images/symbol/grid.gif)');
        canvas._CONFIG.DEFAULT_STYLE.EDGE["edge-type"] = "plain";
        //plain , bezier , straight

        canvas.initConfig({
            selectable: true,
            dragSelectable: true,
            movable: true,
            resizable: true,
            connectable: true,
            selfConnectable: true,
            connectCloneable: true,
            connectRequired: true,
            labelEditable: true,
            groupDropable: true,
            collapsible: true,
            enableHotKey: true,
            enableContextMenu: true
        });

        iconShape.draggable({
            start: function () {
                canvasEle.data('DRAG_SHAPE', {
                    '_shape_type': $(this).attr('_shape_type'),
                    '_shape_id': $(this).attr('_shape_id'),
                    '_width': $(this).attr('_width'),
                    '_height': $(this).attr('_height'),
                    '_label': $(this).attr('_label'),
                    '_textLine': $(this).attr('_textLine')
                });
            },
            helper: 'clone',
            appendTo: "#canvas"
        });
        canvasEle.droppable({
            drop: function (event, ui) {
                var shapeInfo = canvasEle.data('DRAG_SHAPE'), shape, element;
                if (shapeInfo) {
                    if (shapeInfo._label) {
                        var label = shapeInfo._label;
                        shape = eval('new ' + shapeInfo._shape_id + '(label)');
                    } else {
                        shape = eval('new ' + shapeInfo._shape_id + '()');
                    }
                    element = canvas.drawShape([
                            event.pageX - canvasEle.offset().left + canvasEle[0].scrollLeft,
                            event.pageY - canvasEle.offset().top + canvasEle[0].scrollTop],
                        shape, [parseInt(shapeInfo._width, 10), parseInt(shapeInfo._height, 10)]);
                    canvasEle.removeData('DRAG_SHAPE');

                    var properties = {
                        label: shapeInfo._label,
                        shapeId: shapeInfo._shape_id
                    };
                    var isValidated = false;
                    var nodeMeta = {
                        title: shapeInfo._label,
                        type: shapeInfo._label.toLowerCase()
                    };

                    canvas.setCustomData(element, {
                        metadata: nodeMeta,
                        properties: properties,
                        isValidated: isValidated
                    });

                    var textLine = shapeInfo._textLine;
                    if (textLine) {
                        var split = textLine.split(',');
                        canvas.setTextListInController(element, split);
                    }
                }
            }
        });

        canvas.onDivideLane(function (event, divideLane) {

        });

        // Shape 이 처음 Draw 되었을 때의 이벤트 리스너
        canvas.onDrawShape(function (event, shapeElement) {
            $(shapeElement).dblclick(function () {
                ctl.showPropertyWindow(shapeElement);
            });
        });

        // Undo 되었을 때의 이벤트 리스너
        canvas.onUndo(function (event) {
//            console.log('undo');
        });

        // Redo 되었을 때의 이벤트 리스너
        canvas.onRedo(function (event) {
//            console.log('redo');
        });

        // Shape 이 Redraw 되었을 때의 이벤트 리스너
        canvas.onRedrawShape(function (event, shapeElement) {
//            console.log('onRedrawShape', shapeElement);
        });

        // Shape 이 Remove 될 때의 이벤트 리스너
        canvas.onRemoveShape(function (event, shapeElement) {
//            console.log('onRemoveShape', shapeElement);
        });

        // Shape 이 Move 되었을 때의 이벤트 리스너
        canvas.onMoveShape(function (event, shapeElement, offset) {
//            console.log('onMoveShape', shapeElement, offset);
        });

        // Shape 이 Resize 되었을 때의 이벤트 리스너
        canvas.onResizeShape(function (event, shapeElement, offset) {
//            console.log('onResizeShape', shapeElement, offset);
        });

        // Shape 이 Connect 되었을 때의 이벤트 리스너
        canvas.onConnectShape(function (event, edgeElement, fromElement, toElement) {
//            console.log('onConnectShape', edgeElement, fromElement, toElement);
        });

        // Shape 이 Disconnect 되었을 때의 이벤트 리스너
        canvas.onDisconnectShape(function (event, edgeElement, fromElement, toElement) {
//            console.log('onDisconnectShape', edgeElement, fromElement, toElement);
        });

        ctl.canvas = canvas;
    },
    refreshEditor: function (propertyWindow) {
        propertyWindow.find('[name=propertyForm]').each(function () {
            $(this).find('[name=script]').each(function (index, script) {
                var editor = $(script).data('CodeMirrorInstance');
                if (editor) {
                    editor.toTextArea();
                }
                editor = new CodeMirror.fromTextArea(script, {
                    mode: "javascript",
                    styleActiveLine: true,
                    lineNumbers: true
                });
                $(script).data('CodeMirrorInstance', editor);
            })
        });
    },
    initPropertyWindow: function (propertyWindow) {
        var me = this;
        propertyWindow.find('[name=propertyForm]').each(function () {
            $(this).validate({
                ignore: ""
            });
        });

        //탭 오픈이벤트시에 에디터를 리프레쉬한다.
        propertyWindow.find('[name=scriptTab]').each(function () {
            $(this).bind('shown.bs.tab', function () {
                me.refreshEditor(propertyWindow);
            });
        });
        me.refreshEditor(propertyWindow);
    },
    save: function () {
        opengraphJSON = canvas.toJSON();
    },
    getPropertyWindow: function (graphElement) {
        var ctl = this;
        var canvas = ctl.canvas;
        var nodeData = canvas.getCustomData(graphElement),
            nodeMeta = nodeData ? nodeData.metadata : null,
            popWindow;

        if (nodeMeta) {
            var nodeType = nodeMeta.type;
            popWindow = $('#' + nodeType);
        }
        return popWindow;
    },
    showPropertyWindow: function (graphElement) {
        var ctl = this;
        var canvas = ctl.canvas;
        var nodeData = canvas.getCustomData(graphElement),
            nodeMeta = nodeData ? nodeData.metadata : null,
            nodeProperty = nodeData ? nodeData.properties : null,
            popWindow;

        if (nodeMeta && nodeProperty) {
            var nodeType = nodeMeta.type;
            var nodeTitle = nodeMeta.title;

            popWindow = ctl.getPropertyWindow(graphElement);
            popWindow.find('[name=title]').text(nodeTitle);
            ctl.setNodeProperties(graphElement, nodeProperty);

            //모달 오픈이벤트시에 에디터를 리프레쉬한다.
            popWindow.unbind('shown.bs.modal');
            popWindow.bind('shown.bs.modal', function () {
                ctl.refreshEditor(popWindow);
            });
            popWindow.modal('show');

            var saveBtn = popWindow.find('[name=save]');
            saveBtn.unbind('click');
            saveBtn.bind('click', function () {
                //밸리데이션, 그리고 프라퍼티 세이브
                if (ctl.isFormValid(graphElement)) {

                    nodeData.properties = ctl.getNodeProperties(graphElement);
                    nodeData.metadata = ctl.getMetadata(graphElement);
                    nodeData.isValidated = true;
                    canvas.setCustomData(graphElement, nodeData);

                    /*
                     라벨이 체인지 되었으면 라벨을 바꿔준다.
                     */
                    canvas.getRenderer().drawLabel(graphElement, nodeData.properties.label);
                    popWindow.find('.close').click();
                }
            });
        }
    },
    /**
     * 노드 프라퍼티의 form 유효성을 체크한다.
     *
     * @return {Boolean}
     */
    isFormValid: function (graphElement) {
        var isValid = true;
        var inValidCount = 0;
        var forms = this.getPropertyForms(graphElement);
        forms.each(function () {
            if (!$(this).valid()) {
                inValidCount++;
            }
        });
        if (inValidCount > 0) {
            isValid = false;
        }
        return isValid;
    },
    /**
     * 노드의 프라퍼티정보를 반환한다.
     *
     * @return {Object} Property(name:value) Object
     */
    getNodeProperties: function (graphElement) {
        var me = this;
        var properties = {}, value, form, editor, fields;
        var forms = this.getPropertyForms(graphElement);
        forms.each(function () {
            fields = $(this).find(":input").serializeArray();
            form = $(this);
            if (form.attr('id') === 'flowProperty') {
                me.getFlowProperties(graphElement, form, properties);
            } else {
                $.each(fields, function (i, field) {
                    value = field.value;
                    var filedEle = form.find('[name=' + field.name + ']');
                    if (filedEle.data('CodeMirrorInstance')) {
                        editor = filedEle.data('CodeMirrorInstance');
                        value = editor.getDoc().getValue();
                    }
                    if (properties[field.name]) {
                        properties[field.name] += ',' + value
                    } else {
                        properties[field.name] = value;
                    }
                });
            }
        });
        return properties;
    },
    getFlowProperties: function (element, form, properties) {
        var flows = [];
        var flowTemplates = $("[name=flow_template]");
        flowTemplates.each(function () {
            var value = $(this).find("[name=flow_value]").val();
            if (value.length > 0) {
                flows.push(value);
            }
        });
        properties['flow'] = flows.join();
        canvas.setTextListInController(element, flows);
    },
    /**
     * 노드의 메타데이터정보를 반환한다.
     */
    getMetadata: function (graphElement) {
        var nodeData = this.canvas.getCustomData(graphElement);
        if (nodeData) {
            return nodeData.metadata;
        } else {
            return null;
        }
    },
    /**
     * 노드의 폼들을 반환한다.
     *
     * @return {Array} forms
     */
    getPropertyForms: function (graphElement) {
        var popWindow = this.getPropertyWindow(graphElement);
        return popWindow.find('[name=propertyForm]')
    },

    /**
     * 주어진 프라퍼티정보로 노드의 프라퍼티를 설정한다.
     *
     * @param {Object} nodeProperties Property(name:value) Object
     */
    setNodeProperties: function (graphElement, nodeProperties) {
        var me = this;
        if (nodeProperties) {
            var forms = this.getPropertyForms(graphElement);
            var field, value, editor;
            forms.each(function () {
                var form = $(this);
                if (form.attr('id') === 'flowProperty') {
                    me.setFlowProperties(form, nodeProperties);
                    return;
                }

                var fields = form.find(":input").serializeArray();
                $.each(fields, function (i, serializeFiled) {
                    field = form.find('[name=' + serializeFiled.name + ']');
                    value = nodeProperties[serializeFiled.name];
                    var tagName = field.get(0).tagName;

                    switch (tagName) {
                        case 'TEXTAREA':
                            if (field.data('CodeMirrorInstance')) {
                                editor = field.data('CodeMirrorInstance');
                                if (value) {
                                    editor.getDoc().setValue(value);
                                } else {
                                    editor.getDoc().setValue('');
                                }
                            } else {
                                if (value) {
                                    field.val(value);
                                } else {
                                    field.val('');
                                }
                            }
                            break;
                        case 'INPUT':
                            if (value) {
                                field.val(value);
                            } else {
                                field.val('');
                            }
                            break;
                        case 'SELECT':
                            if (value) {
                                var split = value.split(',');
                                $.each(split, function (i, val) {
                                    field.find('option[value=' + val + ']').attr('selected', true);
                                });
                            } else {
                                field.find('option').attr('selected', false);
                            }
                            break;
                        default:
                            break;
                    }
                });
            });
        }
    },
    setFlowProperties: function (form, nodeProperties) {
        form.find('[name=flow_template]').each(function () {
            if ($(this).attr('id') !== 'flow_template') {
                $(this).remove();
            }
        });
        var flows = nodeProperties['flow'];
        if (!flows) {
            flows = ''
        }
        flows = flows.split(',');
        $.each(flows, function (i, flow) {
            var template = $('#flow_template');
            var clone = template.clone();
            clone.removeAttr("id");
            template.after(clone);
            clone.find('[name=flow_value]').val(flow);
            clone.show();

            clone.find("[name=flow_del]").click(function () {
                clone.remove();
            });
        });
    },
    onSaveClick: function(form){
        var toXML = canvas.toXML();
        $(form).find('[name=designer_xml]').val(toXML);
        form.submit();
    },
    onWorkflowLoad: function(xml){
        canvas.loadXML(xml);
    }
};