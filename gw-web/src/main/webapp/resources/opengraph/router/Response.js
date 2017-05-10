if(!OG.shape.router){
    OG.shape.router = {};
}
OG.shape.router.Response = function (label) {
    OG.shape.router.Response.superclass.call(this, '/service-console/resources/opengraph/router/Response.png', label);

    this.SHAPE_ID = 'OG.shape.router.Response';
    this.LABEL_EDITABLE = false;
    this.label = label;
};
OG.shape.router.Response.prototype = new OG.shape.ImageShape();
OG.shape.router.Response.superclass = OG.shape.ImageShape;
OG.shape.router.Response.prototype.constructor = OG.shape.router.Response;
OG.Response = OG.shape.router.Response;