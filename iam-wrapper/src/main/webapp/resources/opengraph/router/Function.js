if(!OG.shape.router){
    OG.shape.router = {};
}
OG.shape.router.Function = function (label) {
    OG.shape.router.Function.superclass.call(this, '/service-console/resources/opengraph/router/Function.svg', label);

    this.SHAPE_ID = 'OG.shape.router.Function';
    this.LABEL_EDITABLE = false;
    this.label = label;
};
OG.shape.router.Function.prototype = new OG.shape.ImageShape();
OG.shape.router.Function.superclass = OG.shape.ImageShape;
OG.shape.router.Function.prototype.constructor = OG.shape.router.Function;
OG.Function = OG.shape.router.Function;