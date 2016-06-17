if(!OG.shape.router){
    OG.shape.router = {};
}
OG.shape.router.Api = function (label) {
    OG.shape.router.Api.superclass.call(this, '/service-console/resources/opengraph/router/Api.png', label);

    this.SHAPE_ID = 'OG.shape.router.Api';
    this.LABEL_EDITABLE = false;
    this.label = label;
};
OG.shape.router.Api.prototype = new OG.shape.ImageShape();
OG.shape.router.Api.superclass = OG.shape.ImageShape;
OG.shape.router.Api.prototype.constructor = OG.shape.router.Api;
OG.Authentication = OG.shape.router.Api;