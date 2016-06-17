if (!OG.shape.router) {
    OG.shape.router = {};
}
OG.shape.router.Authentication = function (label) {
    OG.shape.router.Authentication.superclass.call(this, '/service-console/resources/opengraph/router/Authentication.png', label);

    this.SHAPE_ID = 'OG.shape.router.Authentication';
    this.LABEL_EDITABLE = false;
    this.label = label;
};
OG.shape.router.Authentication.prototype = new OG.shape.ImageShape();
OG.shape.router.Authentication.superclass = OG.shape.ImageShape;
OG.shape.router.Authentication.prototype.constructor = OG.shape.router.Authentication;
OG.Authentication = OG.shape.router.Authentication;