<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.cargodelivery.dao.entity.UserRole" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link href="static/css/style-index.css" rel="stylesheet"/>

    <link href="static/css/style-preloader.css" rel="stylesheet"/>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css" rel="stylesheet"/>

    <link href="https://unpkg.com/boxicons@2.1.2/css/boxicons.min.css" rel="stylesheet"/>

    <title>Cargo-Delivery | Index</title>
    <fmt:setLocale value="${sessionScope.locale}" scope="session" />
    <fmt:setBundle basename="local" />
</head>
<body>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
%>
<!-- Preloader -->
<div class="preloader">
    <img src="static/img/preloader.gif" alt=""/>
</div>

<!-- Header -->

<header class="header">
      <span>
        <a href="" class="logo"><i class="bx bxs-box box"></i>Cargo<span class="yellow">-Delivery</span></a>
      </span>
    <!-- Navbar panel -->
    <nav class="navbar">
        <a href="#home"><fmt:message key="index.home"/></a>
        <a href="#calc"><fmt:message key="index.calc"/></a>
        <a href="#services"><fmt:message key="index.services"/></a>
        <a href="#tariffs"><fmt:message key="index.tariffs"/></a>
        <a href="#directions"><fmt:message key="index.directions"/></a>
        <a href="#contacts"><fmt:message key="index.contact.us"/></a>
        <c:choose>
            <c:when test="${sessionScope.user.userRole eq UserRole.MANAGER}">
                <a href="profile_admin.jsp"><fmt:message key="index.profile"/></a>
            </c:when>
            <c:when test="${sessionScope.user.userRole eq UserRole.USER}">
                <a href="profileOrders"><fmt:message key="index.profile"/></a>
            </c:when>
            <c:otherwise>
                <a href="login.jsp"><fmt:message key="index.profile"/></a>
            </c:otherwise>
        </c:choose>
        <a href="login.jsp"><fmt:message key="index.login"/></a>
        <a href="signup.jsp"><fmt:message key="index.signup"/></a>
        <a href="error.jsp"><fmt:message key="index.error"/></a>
    </nav>
    <div class="icons">
        <div class="theme">
          <span class="icon">
            <ion-icon name="sunny-outline" class="light"></ion-icon>
            <ion-icon name="moon-outline" class="dark"></ion-icon>
          </span>
        </div>
        <div class="lang">
            <c:choose>
                <c:when test="${sessionScope.locale eq null or sessionScope.locale == 'en'}">
                    <a href="locale?p=index&locale=uk"><ion-icon name="globe-outline"></ion-icon></a>
                </c:when>
                <c:otherwise>
                    <a href="locale?p=index&locale=en"><ion-icon name="globe-outline"></ion-icon></a>
                </c:otherwise>
            </c:choose>
        </div>
        <c:if test="${sessionScope.user != null}">
            <div class="logout">
                <a href="login"><ion-icon name="exit-outline"></ion-icon></a>
            </div>
        </c:if>
    </div>
</header>

<!-- Home -->

<section class="home" id="home">
    <div class="content">
        <h1><fmt:message key="index.section.home.h1.1"/> <span class="yellow"><fmt:message key="index.section.home.h1.2"/></span></h1>
        <p><fmt:message key="index.section.home.p"/></p>
        <a href="#calc" class="home-btn"><fmt:message key="index.section.home.btn"/></a>
    </div>
    <div class="image">
        <img src="static/img/Delivery address.svg" alt=""/>
    </div>
</section>

<!-- Cargo calculator section -->

<section class="calc" id="calc">
    <form action="orderForm" method="post" id="form">
        <fieldset>
            <legend><fmt:message key="index.section.calc.legend"/></legend>

            <div class="field">
                <label for="routeStart"><fmt:message key="index.section.calc.label.route_start"/></label>
                <input type="text" name="routeStart" id="routeStart" required/>
            </div>

            <div class="field">
                <label for="routeEnd"><fmt:message key="index.section.calc.label.route_end"/></label>
                <input type="text" name="routeEnd" id="routeEnd" required/>
            </div>

            <div class="field">
                <label for="deliveryDate"><fmt:message key="index.section.calc.label.delivery_date"/></label>
                <input type="date" name="deliveryDate" id="deliveryDate" required/>
            </div>

            <div class="cargo_det">
                <p><fmt:message key="index.section.calc.p.cargo_details"/></p>
                <div class="field">
                    <label for="ilength"><fmt:message key="index.section.calc.label.length"/></label>
                    <input type="number" min="1" name="length" id="ilength" required/>
                </div>
                <div class="field">
                    <label for="width"><fmt:message key="index.section.calc.label.width"/></label>
                    <input type="number" min="1" name="width" id="width" required/>
                </div>
                <div class="field">
                    <label for="height"><fmt:message key="index.section.calc.label.height"/></label>
                    <input type="number" min="1" name="height" id="height" required/>
                </div>
                <div class="field">
                    <label for="weight"><fmt:message key="index.section.calc.label.weight"/></label>
                    <input type="number" min="1" name="weight" id="weight" required/>
                </div>
            </div>
            <div class="field">
                <label><fmt:message key="index.section.calc.label.price"/></label>
                <input name="price" id="pr" type="number" min="0" hidden/>
                <span><span id="price">0</span> ₴</span>
            </div>
            <c:if test="${sessionScope.user != null}">
                <div class="btn">
                    <button type="submit"><fmt:message key="index.section.calc.btn"/></button>
                </div>
            </c:if>
        </fieldset>
    </form>

    <div class="content">
        <h1><fmt:message key="index.section.calc.h1.1"/> <span class="yellow"><fmt:message key="index.section.calc.h1.2"/></span></h1>
        <p><fmt:message key="index.section.content.p"/></p>
        <span class="yellow order-msg">${requestScope.orderMessage}</span>
    </div>
</section>

<!-- Services section -->

<section class="services" id="services">
    <div class="top">
        <h2><span class="yellow"><fmt:message key="index.section.services.h1.1"/> </span><fmt:message key="index.section.services.h1.2"/></h2>
        <p><fmt:message key="index.section.services.p"/></p>
    </div>

    <div class="bottom">
        <div class="box">
            <h4><fmt:message key="index.section.services.h4.1"/></h4>
            <p><fmt:message key="index.section.services.p.delivery"/></p>
            <a href="#"><fmt:message key="index.section.services.a.view_more"/></a>
        </div>
        <div class="box">
            <h4><fmt:message key="index.section.services.h4.2"/></h4>
            <p><fmt:message key="index.section.services.p.food"/></p>
            <a href="#"><fmt:message key="index.section.services.a.view_more"/></a>
        </div>
        <div class="box">
            <h4><fmt:message key="index.section.services.h4.3"/></h4>
            <p><fmt:message key="index.section.services.p.online_order"/></p>
            <a href="#"><fmt:message key="index.section.services.a.view_more"/></a>
        </div>
    </div>
</section>

<!-- Cargo tariffs section -->

<section id="tariffs">
    <div class="table">
        <div class="table_header">
            <p><fmt:message key="index.section.tariffs.header.1"/> <span class="yellow"><fmt:message key="index.section.tariffs.header.2"/></span></p>
        </div>
        <div class="table_section">
            <table>
                <thead>
                <tr>
                    <th rowspan="2"><fmt:message key="index.section.tariffs.table.thead.th.1"/></th>
                    <th colspan="4"><fmt:message key="index.section.tariffs.table.thead.th.2"/></th>
                </tr>
                <tr>
                    <th>1</th>
                    <th>2</th>
                    <th>3</th>
                    <th>4</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td><fmt:message key="index.section.tariffs.table.tbody.td.1"/></td>
                    <td>3.50</td>
                    <td>4.20</td>
                    <td>5.00</td>
                    <td>5.60</td>
                </tr>
                <tr>
                    <td>1 м³</td>
                    <td>3.00</td>
                    <td>8.75</td>
                    <td>10.50</td>
                    <td>12.50</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</section>

<!-- Destination tariffs section -->

<section id="directions">
    <div class="table">
        <div class="table_header">
            <p><fmt:message key="index.section.directions.header.1"/> <span class="yellow"><fmt:message key="index.section.directions.header.2"/></span></p>
        </div>
    </div>
    <table class="table-sort">
        <thead>
        <tr>
            <th>
                <input type="text" class="search-input" placeholder="<fmt:message key="index.section.directions.thead.input.1"/>" />
            </th>
            <th>
                <input type="text" class="search-input" placeholder="<fmt:message key="index.section.directions.thead.input.2"/>"/>
            </th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td><fmt:message key="index.section.directions.tbody.td.1"/></td>
            <td>750</td>
        </tr>
        <tr>
            <td><fmt:message key="index.section.directions.tbody.td.2"/></td>
            <td>450</td>
        </tr>
        <tr>
            <td><fmt:message key="index.section.directions.tbody.td.3"/></td>
            <td>750</td>
        </tr>
        <tr>
            <td><fmt:message key="index.section.directions.tbody.td.4"/></td>
            <td>2100</td>
        </tr>
        <tr>
            <td><fmt:message key="index.section.directions.tbody.td.5"/></td>
            <td>350</td>
        </tr>
        <tr>
            <td><fmt:message key="index.section.directions.tbody.td.6"/></td>
            <td>2100</td>
        </tr>
        <tr>
            <td><fmt:message key="index.section.directions.tbody.td.7"/></td>
            <td>450</td>
        </tr>
        <tr>
            <td><fmt:message key="index.section.directions.tbody.td.8"/></td>
            <td>1050</td>
        </tr>
        <tr>
            <td><fmt:message key="index.section.directions.tbody.td.9"/></td>
            <td>1100</td>
        </tr>
        <tr>
            <td><fmt:message key="index.section.directions.tbody.td.10"/></td>
            <td>900</td>
        </tr>
        <tr>
            <td><fmt:message key="index.section.directions.tbody.td.11"/></td>
            <td>1500</td>
        </tr>
        <tr>
            <td><fmt:message key="index.section.directions.tbody.td.12"/></td>
            <td>900</td>
        </tr>
        <tr>
            <td><fmt:message key="index.section.directions.tbody.td.13"/></td>
            <td>750</td>
        </tr>
        <tr>
            <td><fmt:message key="index.section.directions.tbody.td.14"/></td>
            <td>1100</td>
        </tr>
        </tbody>
    </table>
</section>

<!-- Footer -->

<footer class="footer" id="contacts">
    <div class="top">
        <div class="content">
          <span>
            <a href="" class="logo"><i class="bx bxs-box box"></i>Cargo<span class="yellow">-Delivery</span></a>
          </span>
            <p><fmt:message key="index.section.footer.p"/></p>
        </div>

        <div class="links">
            <div class="link">
                <h4><fmt:message key="index.section.footer.links.1"/></h4>
                <div>
                    <ion-icon class="icon" name="location-outline"></ion-icon>
                    <span>122,Cargo St, Port Delivery,Vic 3207.</span>
                </div>
                <div>
                    <ion-icon class="icon" name="mail-open-outline"></ion-icon>
                    <span>cargo.delivery@gmail.com</span>
                </div>
            </div>

            <div class="link">
                <h4><fmt:message key="index.section.footer.links.2"/></h4>
                <ul>
                    <a href="https://www.linkedin.com/in/maksym-reva-/"><i class="fab fa-linkedin-in"></i></a>
                    <a href="https://github.com/MinoUni/epam-cargo-delivery"><i class="fab fa-github"></i></a>
                </ul>
            </div>
        </div>
    </div>
    <div class="bottom">
        <p>Copyright © 2022-2022 Cargo-Delivery. All rights reserved.</p>
    </div>
</footer>

<!-- Scripts -->

<script type="module" src="https://unpkg.com/ionicons@5.5.2/dist/ionicons/ionicons.esm.js"></script>

<script nomodule src="https://unpkg.com/ionicons@5.5.2/dist/ionicons/ionicons.js"></script>

<script src="static/js/script-index.js"></script>

<script src="static/js/script-preloader.js"></script>

<script src="static/js/script-theme.js"></script>

<script src="static/js/script-tableSearch.js"></script>
</body>
</html>

