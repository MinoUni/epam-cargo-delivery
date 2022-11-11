<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/style-login.css" rel="stylesheet"/>
    <link href="static/css/style-preloader.css" rel="stylesheet"/>
    <link href="https://unpkg.com/boxicons@2.1.2/css/boxicons.min.css" rel="stylesheet"/>
    <title>Cargo-Delivery | Login</title>
    <fmt:setLocale value="${sessionScope.locale}" scope="session" />
    <fmt:setBundle basename="local" />
</head>
<body>
<!-- Preloader -->
<div class="preloader">
    <img src="static/img/preloader.gif" alt=""/>
</div>
<section class="container">
    <div class="login">
        <header><fmt:message key="login.header"/></header>
        <form method="post" action="controller?command=LOGIN">
            <div class="field login-field">
                <div class="input-field">
                    <input type="text" name="login" id="login"
                           placeholder="<fmt:message key="login.input.login"/>"
                           class="login"
                           title="<fmt:message key="login.input.login.title"/>"
                           required/>
                </div>
            </div>

            <div class="field password-field">
                <div class="input-field">
                    <input type="password"
                           name="password"
                           id="password"
                           placeholder="<fmt:message key="login.input.password"/>"
                           class="password"
                           title="<fmt:message key="login.input.password.title"/>"
                           required/>
                    <i class="bx bx-hide show-pass"></i>
                </div>
            </div>

            <div class="input-field btn-field">
                <button type="submit" name="login_btn" id="login_btn"><fmt:message key="login.button.submit"/></button>
            </div>
        </form>

        <div class="link-field">
            <span><fmt:message key="login.link.to.signup"/> <a href="signup.jsp" class="link signup-link"><fmt:message key="login.link.to.signup.a"/></a></span>
        </div>

        <div class="line"></div>

        <div class="back-home">
            <a href="index.jsp" class="input-field home-link">
                <i class="bx bxs-box box icon"></i>
                <span><fmt:message key="login.button.go.index"/></span>
            </a>
        </div>
    </div>
</section>

<!-- JavaScript -->
<script src="static/js/script-preloader.js"></script>

<script src="static/js/script-showPassword.js"></script>
</body>
</html>
