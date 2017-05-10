if(!OG.shape.router){
    OG.shape.router = {};
}
OG.shape.router.Oauth = function (label) {
    OG.shape.router.Oauth.superclass.call(this, '/service-console/resources/opengraph/router/Oauth.png', label);

    this.SHAPE_ID = 'OG.shape.router.Oauth';
    this.LABEL_EDITABLE = false;
    this.label = label;
};
OG.shape.router.Oauth.prototype = new OG.shape.ImageShape();
OG.shape.router.Oauth.superclass = OG.shape.ImageShape;
OG.shape.router.Oauth.prototype.constructor = OG.shape.router.Oauth;
OG.Oauth = OG.shape.router.Oauth;