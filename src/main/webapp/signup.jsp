<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <link href="static/css/style-signup.css" rel="stylesheet"/>
    <link href="static/css/style-preloader.css" rel="stylesheet"/>
    <link href="https://unpkg.com/boxicons@2.1.2/css/boxicons.min.css" rel="stylesheet"/>
    <title>Cargo-Delivery | Signup</title>
    <fmt:setLocale value="${sessionScope.locale}" scope="session" />
    <fmt:setBundle basename="local" />
</head>
<body>

<!-- Preloader -->
<div class="preloader">
    <img src="static/img/preloader.gif" alt=""/>
</div>

<section class="container">
    <div class="signup">
        <header><fmt:message key="signup.header"/></header>
        <form method="post" action="signup">
            <div class="field login-field">
                <div class="input-field">
                    <input type="text" name="login" id="login"
                           placeholder="<fmt:message key="signup.input.login"/>"
                           class="login"
                           title="<fmt:message key="signup.input.login.title"/>"
                           required/>
                </div>
            </div>

            <div class="field name-field">
                <div class="input-field">
                    <input type="text" name="name" id="name"
                           placeholder="<fmt:message key="signup.input.name"/>"
                           class="name"
                           title="<fmt:message key="signup.input.name.title"/>"
                           required/>
                </div>
            </div>

            <div class="field surname-field">
                <div class="input-field">
                    <input type="text" name="surname" id="surname"
                           placeholder="<fmt:message key="signup.input.surname"/>"
                           class="surname"
                           title="<fmt:message key="signup.input.surname.title"/>"
                           required/>
                </div>
            </div>

            <div class="field email-field">
                <div class="input-field">
                    <input type="email" name="email" id="email"
                           placeholder="<fmt:message key="signup.input.email"/>"
                           class="email"
                           title="<fmt:message key="signup.input.email.title"/>"
                           pattern="^[^ ]+@[^ ]+\.[a-z]{2,3}$"
                           required/>
                </div>
            </div>

            <div class="field password-field">
                <div class="input-field">
                    <input type="password" name="password" id="password"
                           placeholder="<fmt:message key="signup.input.password"/>"
                           class="password"
                           title="<fmt:message key="signup.input.password.title"/>"
                           required/>
                    <i class="bx bx-hide show-pass"></i>
                </div>
            </div>

            <div class="field confirm-password-field">
                <div class="input-field">
                    <input type="password" name="confirm-password" id="confirm-password"
                           placeholder="<fmt:message key="signup.input.confirm.password"/>"
                           class="confirm-password"
                           title="<fmt:message key="signup.input.confirm.password.title"/>"
                           required/>
                </div>
            </div>

            <div class="input-field btn-field">
                <button type="submit" name="signup_btn" id="signup_btn"><fmt:message key="signup.button.submit"/></button>
            </div>
        </form>

        <div class="link-field">
            <span><fmt:message key="signup.link.to.login"/>
                <a href="login.jsp" class="link login-link"><fmt:message key="signup.link.to.login.a"/></a>
            </span>
        </div>

        <div class="line"></div>

        <div class="back-home">
            <a href="index.jsp" class="input-field home-link">
                <i class="bx bxs-box box icon"></i>
                <span><fmt:message key="signup.button.go.index"/></span>
            </a>
        </div>
    </div>
</section>

<!-- JavaScript -->
<script src="static/js/script-signup.js"></script>

<script src="static/js/script-preloader.js"></script>

<script src="static/js/script-showPassword.js"></script>
</body>
</html>
