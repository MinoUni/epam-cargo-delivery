<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%--<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>--%>
<%--<fmt:setLocale value="${sessionScope.local}" />--%>
<%--<fmt:setBundle basename="local" var="loc"/>--%>
<%--<fmt:message bundle="${loc}" key="local.message" var="message"/>--%>
<%--<fmt:message bundle="${loc}" key="local.btn.eng" var="eng_btn"/>--%>
<%--<fmt:message bundle="${loc}" key="local.btn.ua" var="ua_btn"/>--%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <!-- === CSS === -->

    <%--    <link href="static/css/style-index.css" rel="stylesheet"/>--%>
    <link
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.2/css/all.min.css"
            rel="stylesheet"
    />
    <link
            href="https://unpkg.com/boxicons@2.1.2/css/boxicons.min.css"
            rel="stylesheet"
    />
    <title>Cargo-Delivery | Index</title>
</head>
<body>

<nav>

    <div class="navbar">

        <div class="logo-toggle">
            <span class="logo"><i class="bx bxs-box box"></i><a href="#">Cargo-Delivery</a></span>
            <i class="bx bx-x sidebar-close"></i>
        </div>

        <div class="menu">
            <ul class="nav-links">
                <li><a href="#">Home</a></li>
                <li><a href="#">Tariffs</a></li>
                <li><a href="#">Contacts</a></li>
                <li><a href="#">About</a></li>
            </ul>
        </div>

        <div class="icon-box">
            <div class="dark-light">
                <i class="bx bx-moon moon"></i>
                <i class="bx bx-sun sun"></i>
            </div>

            <div class="login">
                <a href="login.jsp" class="reg"><i class="bx bxs-user-account log-in"></i></a>
            </div>

<%--            <div class="local-buttons">--%>
<%--                <form action="${pageContext.request.contextPath}/lang_ua" method="post">--%>
<%--                    <input type="hidden" name="local" value="en" />--%>
<%--                    <input type="submit" value="${eng_btn}" class="" />--%>
<%--                </form>--%>
<%--                <form action="${pageContext.request.contextPath}/lang_ua" method="post">--%>
<%--                    <input type="hidden" name="local" value="ua">--%>
<%--                    <input class="" type="submit" name="local" value="${ua_btn}" />--%>
<%--                </form>--%>
<%--            </div>--%>
        </div>
    </div>
</nav>

<footer class="footer">
    <div class="container">
        <div class="row">
            <div class="footer-col">
                <h4>company</h4>
                <ul>
                    <li><a href="#">a</a></li>
                    <li><a href="#">b</a></li>
                    <li><a href="#">c</a></li>
                </ul>
            </div>
            <div class="footer-col">
                <h4>services</h4>
                <ul>
                    <li><a href="#">a</a></li>
                    <li><a href="#">b</a></li>
                    <li><a href="#">c</a></li>
                </ul>
            </div>
            <div class="footer-col">
                <h4>get help</h4>
                <ul>
                    <li><a href="#">a</a></li>
                    <li><a href="#">b</a></li>
                    <li><a href="#">c</a></li>
                </ul>
            </div>
            <div class="footer-col">
                <h4>follow us</h4>
                <div class="social-links">
                    <ul>
                        <a href="https://www.facebook.com/"
                        ><i class="fab fa-facebook-f"></i
                        ></a>
                        <a href="https://www.twitter.com/"
                        ><i class="fab fa-twitter"></i
                        ></a>
                        <a href="https://www.instagram.com/"
                        ><i class="fab fa-instagram"></i
                        ></a>
                        <a href="https://www.linkedin.com/in/maksym-reva-/"
                        ><i class="fab fa-linkedin-in"></i
                        ></a>
                        <a href="https://github.com/MinoUni/Cargo-delivery"
                        ><i class="fab fa-github"></i
                        ></a>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <p class="footer-cr">cargo delivery group © 2022-2022</p>
</footer>

<%--<script src="static/js/script-index.js"></script>--%>
</body>
</html>