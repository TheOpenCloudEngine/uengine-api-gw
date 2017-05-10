if(!OG.shape.router){
    OG.shape.router = {};
}
OG.shape.router.Proxy = function (label) {
    OG.shape.router.Proxy.superclass.call(this, '/service-console/resources/opengraph/router/Proxy.png', label);

    this.SHAPE_ID = 'OG.shape.router.Proxy';
    this.LABEL_EDITABLE = false;
    this.label = label;
};
OG.shape.router.Proxy.prototype = new OG.shape.ImageShape();
OG.shape.router.Proxy.superclass = OG.shape.ImageShape;
OG.shape.router.Proxy.prototype.constructor = OG.shape.router.Proxy;
OG.Proxy = OG.shape.router.Proxy;