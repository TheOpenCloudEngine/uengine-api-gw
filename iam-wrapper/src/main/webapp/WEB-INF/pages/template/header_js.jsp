<%@ page import="org.opencloudengine.garuda.model.User" %>
<%@ page import="org.opencloudengine.garuda.util.NetworkUtils" %>
<%@ page import="java.util.Locale" %>
<%@ page import="org.springframework.context.i18n.LocaleContextHolder" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" trimDirectiveWhitespaces="true" %>

<!-- Meta -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>OCE SERVICE WRAPPER | HOME</title>

<!-- 타이틀 -->
<meta name="title" content="uengine Inc"/>
<!-- 주제 -->
<meta name="subject" content="Big Data Platform"/>
<!-- 요약 설명 -->
<meta name="description" content="Hadoop 기반 빅데이터 플랫폼 - Hadoop을 이용한 빅데이터 환경에서 데이터를 처리,분석,시각화할 수 있는 기반환경을 제공합니다.">
<!-- 검색 키워드 -->
<meta name="keywords" content="Hadoop,Platform,하둡,플랫폼,빅데이터,Big Data,ExtJS,분석,Cloud,클라우드"/>
<!-- 웹 페이지(사이트) © Copyright 저작권 -->
<meta name="copyright" content="uengine Inc"/>
<!-- 웹 페이지(사이트) 검색사이트 검색로봇의 접근 허용 -->
<meta name="robots" content="ALL"/>
<!-- 웹 페이지(사이트) 내의 링크를 포함한 검색(수집) 허용 -->
<meta name="robots" content="INDEX,FOLLOW"/>
<!-- 웹 페이지(사이트) 캐쉬(Cache) 미저장 -->
<meta http-equiv="cache-control" content="No-Cache"/>
<meta http-equiv="pragma" content="No-Cache"/>
<meta http-equiv="expires" content="MON, 01 apr 2002 00:00:01 GMT">
<meta name="Location" content="South Korea"/>
<meta name="Distribution" content="Global"/>
<meta name="author" content="클라우다인">

<!-- Favicon -->
<link rel="shortcut icon" href="/service-console/resources/images/favicon.ico">

<!-- CSS Global Compulsory -->
<link rel="stylesheet" href="/service-console/resources/assets/plugins/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="/service-console/resources/assets/css/style.css">

<!-- CSS Header and Footer -->
<link rel="stylesheet" href="/service-console/resources/assets/css/headers/header-default.css">
<link rel="stylesheet" href="/service-console/resources/assets/css/footers/footer-v1.css">

<!-- CSS Implementing Plugins -->
<link rel="stylesheet" href="/service-console/resources/assets/plugins/animate.css">
<link rel="stylesheet" href="/service-console/resources/assets/plugins/line-icons/line-icons.css">
<link rel="stylesheet" href="/service-console/resources/assets/plugins/font-awesome/css/font-awesome.min.css">
<link rel="stylesheet"
      href="/service-console/resources/assets/plugins/cube-portfolio/cubeportfolio/css/cubeportfolio.min.css">
<link rel="stylesheet"
      href="/service-console/resources/assets/plugins/cube-portfolio/cubeportfolio/custom/custom-cubeportfolio.css">
<link rel="stylesheet" href="/service-console/resources/assets/plugins/parallax-slider/css/parallax-slider.css">
<link rel="stylesheet" href="/service-console/resources/assets/plugins/owl-carousel/owl-carousel/owl.carousel.css">
<link rel="stylesheet" href="/service-console/resources/assets/css/pages/pricing/pricing-light.css">

<link rel="stylesheet" href="/service-console/resources/assets/plugins/scrollbar/css/jquery.mCustomScrollbar.css">
<link rel="stylesheet" href="/service-console/resources/assets/plugins/sky-forms-pro/skyforms/css/sky-forms.css">
<link rel="stylesheet"
      href="/service-console/resources/assets/plugins/sky-forms-pro/skyforms/custom/custom-sky-forms.css">

<!-- JS Global Compulsory -->
<script type="text/javascript" src="/service-console/resources/assets/plugins/jquery/jquery.min.js"></script>
<script type="text/javascript" src="/service-console/resources/assets/plugins/jquery/jquery-migrate.min.js"></script>
<script type="text/javascript" src="/service-console/resources/assets/plugins/bootstrap/js/bootstrap.min.js"></script>

<link rel="stylesheet" type="text/css"
      href="/service-console/resources/assets/plugins/jquery-ui-1.11.0.custom/jquery-ui.css"/>
<script type="text/javascript"
        src="/service-console/resources/assets/plugins/jquery-ui-1.11.0.custom/jquery-ui.min.js"></script>

<!-- Spring JS -->
<script type="text/javascript" th:src="@{/service-console/resources/spring/Spring.js}"></script>
<script type="text/javascript" th:src="@{/service-console/resources/spring/Spring-Dojo.js}"></script>


<!-- _csrf -->
<meta name="_csrf" content="${_csrf.token}"/>
<!-- default header name is X-CSRF-TOKEN -->
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<!-- AJAX_csrf 오버라이드 && 더블 submit 방지-->
<script type="text/javascript">
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
    $(function () {
        $('form').each(function () {
            var form = $(this);
            form.append('<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>');
            if (form.find('[type=submit]').length > 0) {
                form.submit(function () {
                    blockSubmitStart();
                });
            }
        });
    })
</script>
<script type="text/javascript">
    $(function () {
        $('form').each(function () {
            var form = $(this);
            form.submit(function () {
                blockSubmitStart();
            });
        });
    })
</script>

<!-- config && message -->
<script type="text/javascript" src="/service-console/config/js.json"></script>
<script type="text/javascript">var lang = "${lang}";
lang = (lang.length > 0) ? lang : config['default.locale'];</script>
<script type="text/javascript" src="/service-console/resources/js/bundle.js"></script>


<!-- session -->
<%
    User user = (User) session.getAttribute("user");
    String ipAddress = NetworkUtils.getIpAddr(request);

    //해당 세션의 첫 접속인경우, ISOCode 와 언어가 일치하지 않을 경우 맞는언어페이지로 리다이렉트.
    if (session.getAttribute("ISOCode") == null) {
        String ISOCode = NetworkUtils.getCountryCode(ipAddress);
        session.setAttribute("ISOCode", ISOCode);
        String localeByIpaddr = NetworkUtils.getLocaleFromISOCode(ISOCode);
        Locale locale = LocaleContextHolder.getLocale();
        if (!localeByIpaddr.equals(locale.toString()))
            response.sendRedirect("/service-console/index?lang=" + localeByIpaddr);
    }

%>
<script type="text/javascript">
    function isEmpty(str) {
        if (typeof str == 'string' && str.length > 0)return false;
        return true;
    }
    var SESSION = {};
    SESSION.ISOCode = !isEmpty('${ISOCode}') ? '${ISOCode}' : undefined;
    SESSION.NAME = !isEmpty('${user.name}') ? '${user.name}' : undefined;
    SESSION.EMAIL = !isEmpty('${user.email}') ? '${user.email}' : undefined;
    SESSION.DESCRIPTION = !isEmpty('${user.description}') ? '${user.description}' : undefined;
    SESSION.LEVEL = !isEmpty('${user.level}') ? '${user.level}' : undefined;

</script>

<!-- CSS Theme -->
<link rel="stylesheet" href="/service-console/resources/assets/css/theme-colors/dark-blue.css">

<!-- uengine CSS -->
<link rel="stylesheet" href="/service-console/resources/assets/css/uengine.css">
