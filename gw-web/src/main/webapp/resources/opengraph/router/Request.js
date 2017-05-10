if(!OG.shape.router){
    OG.shape.router = {};
}
OG.shape.router.Request = function (label) {
    OG.shape.router.Request.superclass.call(this, '/service-console/resources/opengraph/router/Request.png', label);

    this.SHAPE_ID = 'OG.shape.router.Request';
    this.LABEL_EDITABLE = false;
    this.label = label;
};
OG.shape.router.Request.prototype = new OG.shape.ImageShape();
OG.shape.router.Request.superclass = OG.shape.ImageShape;
OG.shape.router.Request.prototype.constructor = OG.shape.router.Request;
OG.Request = OG.shape.router.Request;