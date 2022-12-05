<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="com.cargodelivery.dao.entity.User" %>
<%@ page import="com.cargodelivery.dao.entity.enums.UserRole" %>
<%@ page import="com.cargodelivery.dao.entity.enums.OrderState" %>
<%@ page import="com.cargodelivery.dao.entity.Cargo" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link href="static/css/style-profile.css" rel="stylesheet"/>
    <link href="static/css/style-preloader.css" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css" rel="stylesheet"/>
    <link href="https://unpkg.com/boxicons@2.1.2/css/boxicons.min.css" rel="stylesheet"/>

    <title>Cargo-Delivery | U-Profile ${sessionScope.user.login}</title>
    <fmt:setLocale value="${sessionScope.locale}" scope="session" />
    <fmt:setBundle basename="local" />
</head>
<body>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

    if (session.getAttribute("user") == null ||
            !((User) session.getAttribute("user")).getRole().equals(UserRole.USER))
        response.sendRedirect("index.jsp");
%>
<!-- Preloader -->
<div class="preloader">
    <img src="static/img/preloader.gif" alt=""/>
</div>

<header class="header">
      <span>
        <a href="index.jsp" class="logo"><i class="bx bxs-box box"></i>Cargo<span class="yellow">-Delivery</span></a>
      </span>
    <!-- Navbar panel -->
    <nav class="navbar">
        <a href="profile_user.jsp"><fmt:message key="profile.user.navbar.profile"/></a>
        <a href="controller?command=USER_ORDERS"><fmt:message key="profile.user.navbar.orders"/></a>
        <a href=""><fmt:message key="profile.user.navbar.download.orders"/></a>
        <a href=""><fmt:message key="profile.user.navbar.balance"/></a>
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
                    <a href="controller?command=LOCALE&locale=uk&page=userProfile"><ion-icon name="globe-outline"></ion-icon></a>
                </c:when>
                <c:otherwise>
                    <a href="controller?command=LOCALE&locale=en&page=userProfile"><ion-icon name="globe-outline"></ion-icon></a>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="logout">
            <a href="controller?command=LOGOUT"><ion-icon name="exit-outline"></ion-icon></a>
        </div>
    </div>
</header>

<section>
    <form action="" method="get">
        <div class="table">
            <div class="table_header">
                <p><fmt:message key="profile.user.table.header.1"/><span class="yellow"><fmt:message key="profile.user.table.header.2"/></span></p>
                <p><fmt:message key="profile.user.table.header.balance"/> ${sessionScope.user.balance} ₴</p>
            </div>
            <c:choose>
                <c:when test="${sessionScope.userOrders == null or sessionScope.userOrders.size() == 0}">
                    <p class="empty"><fmt:message key="profile.user.table.empty"/></p>
                </c:when>
                <c:otherwise>
                    <div class="table_section">
                        <table class="table-sort">
                            <thead>
                                <tr>
                                    <th>
                                        <input type="text"
                                               class="search-input"
                                               placeholder="<fmt:message key="profile.user.table.thead.th.input.1"/>"
                                        >
                                    </th>
                                    <th>
                                        <input type="text"
                                               class="search-input"
                                               placeholder="<fmt:message key="profile.user.table.thead.th.input.2"/>"
                                        >
                                    </th>
                                    <th>
                                        <input type="text"
                                               class="search-input"
                                               placeholder="<fmt:message key="profile.user.table.thead.th.input.3"/>"
                                        >
                                    </th>
                                    <th>
                                        <input type="text"
                                               class="search-input"
                                               placeholder="<fmt:message key="profile.user.table.thead.th.input.4"/>"
                                        >
                                    </th>
                                    <th>
                                        <input type="text"
                                               class="search-input"
                                               placeholder="<fmt:message key="profile.user.table.thead.th.input.5"/>"
                                        >
                                    </th>
                                    <th>
                                        <input type="text"
                                               class="search-input"
                                               placeholder="<fmt:message key="profile.user.table.thead.th.input.6"/>"
                                        >
                                    </th>
                                    <th><fmt:message key="profile.user.table.thead.th.input.7"/></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="order" items="${sessionScope.userOrders}">
                                    <tr>
                                        <td>${order.id}</td>
                                        <td>${order.userId}</td>
                                        <td>${order.route}</td>
                                        <td>${order.registrationDate}</td>
                                        <td>${order.deliveryDate}</td>
                                        <td>${order.price}</td>
                                        <td>
                                            <div class="actions">
                                                <a href="controller?command=ORDER_INFO&orderId=${order.id}"
                                                   class="info_action"><fmt:message key="profile.user.table.tbody.action.info"/></a>
                                                <c:if test="${order.state != OrderState.PAID}">
                                                    <a href="controller?command=ORDER_DELETE&orderId=${order.id}"
                                                       class="del_action"><fmt:message key="profile.user.table.tbody.action.delete"/></a>
                                                </c:if>
                                                <c:if test="${order.state == OrderState.WAITING_FOR_PAYMENT}">
                                                    <a href="controller?command=ORDER_PAY&orderId=${order.id}"
                                                       class="pay_action"><fmt:message key="profile.user.table.tbody.action.pay"/></a>
                                                </c:if>
                                                <c:if test="${order.state eq OrderState.PAID}">
                                                    <a href="controller?command=ORDER_BILL&orderId=${order.id}"
                                                        class="bill_action"><fmt:message key="profile.user.table.tbody.action.bill"/></a>
                                                </c:if>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </form>
</section>
<section>
    <div class="table">
        <div class="table_header">
            <p><fmt:message key="profile.user.table.cargo.header.1"/><span class="yellow"><fmt:message key="profile.user.table.cargo.header.2"/></span></p>
        </div>
        <c:choose>
            <c:when test="${sessionScope.orderInfo == null}">
                <p class="empty"><fmt:message key="profile.user.table.cargo.empty"/></p>
            </c:when>
            <c:otherwise>
                <div class="table_section">
                    <table class="table-sort">
                        <thead>
                        <tr>
                            <th><fmt:message key="profile.user.table.cargo.thead.th.1"/></th>
                            <th><fmt:message key="profile.user.table.cargo.thead.th.2"/></th>
                            <th><fmt:message key="profile.user.table.cargo.thead.th.3"/></th>
                            <th><fmt:message key="profile.user.table.cargo.thead.th.4"/></th>
                            <th><fmt:message key="profile.user.table.cargo.thead.th.5"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>${sessionScope.orderInfo.cargo.length}</td>
                            <td>${sessionScope.orderInfo.cargo.width}</td>
                            <td>${sessionScope.orderInfo.cargo.height}</td>
                            <td>${sessionScope.orderInfo.cargo.weight}</td>
                            <td>${sessionScope.orderInfo.state}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>
<script
        type="module"
        src="https://unpkg.com/ionicons@5.5.2/dist/ionicons/ionicons.esm.js"
></script>

<script
        nomodule
        src="https://unpkg.com/ionicons@5.5.2/dist/ionicons/ionicons.js"
></script>
<script src="static/js/script-profile.js"></script>

<script src="static/js/script-preloader.js"></script>

<script src="static/js/script-theme.js"></script>

<script src="static/js/script-tableSearch.js"></script>
</body>
</html>

