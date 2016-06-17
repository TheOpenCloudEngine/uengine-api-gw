<%@ page contentType="text/html; charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>

<!-- ajax 호출 UI 블락커 && 커스터마이징 -->
<script type="text/javascript" src="/service-console/resources/plugins/blockUI/blockUI.js"></script>
<script type="text/javascript" src="/service-console/resources/js/blockUIcustom.js"></script>


<!-- 메뉴 스트럭쳐 && html 콘텐츠 로딩 && 언어 -->
<script type="text/javascript" src="/service-console/resources/structure/structure.data.js"></script>
<script type="text/javascript" src="/service-console/resources/structure/structure.js"></script>
<script type="text/javascript" src="/service-console/resources/js/language.js"></script>

<!-- 배경 백그라운드 이미지 교체 -->
<script type="text/javascript">
    $('.parallax-counter-v4').css('background' , 'url(/service-console/resources/assets/img/bg/19.jpg) 50% 0 fixed');
</script>

<!-- Jquery Validator -->
<script type="text/javascript" src="/service-console/resources/plugins/validator/jquery.validate.js"></script>
<script type="text/javascript" src="/service-console/resources/plugins/validator/additional-methods.js"></script>

<!-- JS Implementing Plugins -->
<script type="text/javascript" src="/service-console/resources/assets/plugins/back-to-top.js"></script>
<script type="text/javascript" src="/service-console/resources/assets/plugins/smoothScroll.js"></script>
<script type="text/javascript" src="/service-console/resources/assets/plugins/cube-portfolio/cubeportfolio/js/jquery.cubeportfolio.min.js"></script>
<script type="text/javascript" src="/service-console/resources/assets/plugins/parallax-slider/js/modernizr.js"></script>
<script type="text/javascript" src="/service-console/resources/assets/plugins/parallax-slider/js/jquery.cslider.js"></script>
<script type="text/javascript" src="/service-console/resources/assets/plugins/owl-carousel/owl-carousel/owl.carousel.js"></script>
<script type="text/javascript" src="/service-console/resources/assets/plugins/sky-forms-pro/skyforms/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="/service-console/resources/assets/plugins/sky-forms-pro/skyforms/js/jquery.validate.min.js"></script>
<script type="text/javascript" src="/service-console/resources/assets/plugins/sky-forms-pro/skyforms/js/jquery.maskedinput.min.js"></script>
<script type="text/javascript" src="/service-console/resources/assets/plugins/scrollbar/js/jquery.mCustomScrollbar.concat.min.js"></script>

<!-- JS Customization -->
<script type="text/javascript" src="/service-console/resources/assets/js/custom.js"></script>

<!-- JS Page Level -->
<script type="text/javascript" src="/service-console/resources/assets/js/app.js"></script>
<script type="text/javascript" src="/service-console/resources/assets/js/plugins/owl-carousel.js"></script>
<script type="text/javascript" src="/service-console/resources/assets/js/plugins/parallax-slider.js"></script>
<script type="text/javascript" src="/service-console/resources/js/cube.js"></script>
<script type="text/javascript" src="/service-console/resources/assets/js/forms/reg.js"></script>
<script type="text/javascript" src="/service-console/resources/assets/js/forms/checkout.js"></script>
<script type="text/javascript" src="/service-console/resources/assets/js/plugins/datepicker.js"></script>
<script type="text/javascript">
    jQuery(document).ready(function() {
        App.init();
        OwlCarousel.initOwlCarousel();
        ParallaxSlider.initParallaxSlider();
    });
</script>


<!--[if lt IE 9]>
<script src="/service-console/resources/assets/plugins/respond.js"></script>
<script src="/service-console/resources/assets/plugins/html5shiv.js"></script>
<script src="/service-console/resources/assets/plugins/placeholder-IE-fixes.js"></script>
<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<script src="/service-console/resources/assets/plugins/sky-forms-pro/skyforms/js/sky-forms-ie8.js"></script>
<![endif]-->

<!--[if lt IE 10]>
<script src="/service-console/resources/assets/plugins/sky-forms-pro/skyforms/js/jquery.placeholder.min.js"></script>
<![endif]-->