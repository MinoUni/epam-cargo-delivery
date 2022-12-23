<%@ page import="com.cargodelivery.dao.entity.enums.UserRole" %>
<%@ page import="com.cargodelivery.dao.entity.enums.AdminAction" %>
<%@ page import="com.cargodelivery.dao.entity.enums.OrderState" %>
<%@ page import="com.cargodelivery.dao.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>

    <link href="static/css/style-profile.css" rel="stylesheet"/>
    <link href="static/css/style-preloader.css" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css" rel="stylesheet"/>
    <link href="https://unpkg.com/boxicons@2.1.2/css/boxicons.min.css" rel="stylesheet"/>

    <title>Cargo-Delivery | A-Profile ${sessionScope.user.login}</title>

    <fmt:setLocale value="${sessionScope.locale}" scope="session" />
    <fmt:setBundle basename="local" />
</head>
<body>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

    if (session.getAttribute("user") == null ||
            !((User) session.getAttribute("user")).getRole().equals(UserRole.MANAGER))
        response.sendRedirect("index.jsp");
%>
<!-- Preloader -->
<div class="preloader">
    <img src="static/img/preloader.gif" alt=""/>
</div>

<header class="header">
      <span>
        <a href="index.jsp" class="logo"
        ><i class="bx bxs-box box"></i>Cargo<span class="yellow"
        >-Delivery</span
        ></a
        >
      </span>
    <!-- Navbar panel -->
    <nav class="navbar">
        <a href="profile_admin.jsp"><fmt:message key="profile.admin.navbar.profile"/></a>
        <a href="controller?command=GET_USERS&currentPage=1"><fmt:message key="profile.admin.navbar.users"/></a>
        <a href="controller?command=GET_ORDERS&currentPage=1"><fmt:message key="profile.admin.navbar.orders"/></a>
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
                    <a href="controller?command=LOCALE&locale=uk&page=adminProfile"><ion-icon name="globe-outline"></ion-icon></a>
                </c:when>
                <c:otherwise>
                    <a href="controller?command=LOCALE&locale=en&page=adminProfile"><ion-icon name="globe-outline"></ion-icon></a>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="logout">
            <a href="controller?command=LOGOUT">
                <ion-icon name="exit-outline"></ion-icon>
            </a>
        </div>
    </div>
</header>

<section>
    <form>
        <div class="table">
            <div class="table_header">
                <p><fmt:message key="profile.admin.table.header.1"/><span class="yellow"><fmt:message key="profile.admin.table.header.2"/></span></p>
            </div>
            <c:choose>

                <c:when test="${sessionScope.adminAction eq AdminAction.ORDERS}">
                    <div class="table_section">
                        <table class="table-sort">
                            <thead>
                            <tr>
                                <th>
                                    <input type="text"
                                           class="search-input"
                                           placeholder="<fmt:message key="profile.admin.table.thead.th.input.1"/>">
                                </th>
                                <th>
                                    <input type="text"
                                           class="search-input"
                                           placeholder="<fmt:message key="profile.admin.table.thead.th.input.2"/>">
                                </th>
                                <th>
                                    <input type="text"
                                           class="search-input"
                                           placeholder="<fmt:message key="profile.admin.table.thead.th.input.3"/>">
                                </th>
                                <th>
                                    <input type="text"
                                           class="search-input"
                                           placeholder="<fmt:message key="profile.admin.table.thead.th.input.4"/>">
                                </th>
                                <th>
                                    <input type="text"
                                           class="search-input"
                                           placeholder="<fmt:message key="profile.admin.table.thead.th.input.5"/>">
                                </th>
                                <th>
                                    <input type="text"
                                           class="search-input"
                                           placeholder="<fmt:message key="profile.admin.table.thead.th.input.6"/>">
                                </th>
                                <th>
                                    <input type="text"
                                           class="search-input"
                                           placeholder="<fmt:message key="profile.admin.table.thead.th.input.7"/>">
                                </th>
                                <th>
                                    <fmt:message key="profile.admin.table.thead.th.input.8"/>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="order" items="${sessionScope.adminList}">
                                <tr>
                                    <td>${order.id}</td>
                                    <td>${order.userId}</td>
                                    <td>${order.route}</td>
                                    <td>${order.registrationDate}</td>
                                    <td>${order.deliveryDate}</td>
                                    <td>${order.price}</td>
                                    <td>${order.state}</td>
                                    <td>
                                        <div class="actions">
                                            <c:if test="${order.state eq OrderState.REGISTERED}">
                                                <a href="controller?command=ORDER_APPROVE&orderId=${order.id}&currentPage=${sessionScope.currentPage}"
                                                   class="info_action"><fmt:message key="profile.admin.table.tbody.action.approve"/></a>
                                            </c:if>
                                            <c:if test="${order.state eq OrderState.WAITING_FOR_PAYMENT or
                                                          order.state eq OrderState.REGISTERED}">
                                                <a href="controller?command=ORDER_DECLINE&orderId=${order.id}&currentPage=${sessionScope.currentPage}"
                                                   class="del_action"><fmt:message key="profile.admin.table.tbody.action.block"/></a>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="pagination">
                        <c:if test="${sessionScope.adminList != null}">
                            <c:if test="${sessionScope.currentPage != 1}">
                                <a href="controller?command=GET_ORDERS&currentPage=${sessionScope.currentPage - 1}"><ion-icon name="arrow-back-outline"></ion-icon></a>
                            </c:if>
                            <a href="controller?command=GET_ORDERS&currentPage=${sessionScope.currentPage}">${sessionScope.currentPage}</a>
                            <c:if test="${sessionScope.currentPage lt sessionScope.numbOfPages}">
                                <a href="controller?command=GET_ORDERS&currentPage=${sessionScope.currentPage + 1}"><ion-icon name="arrow-forward-outline"></ion-icon></a>
                            </c:if>
                        </c:if>
                    </div>
                </c:when>

                <c:when test="${sessionScope.adminAction eq AdminAction.USERS}">
                    <div class="table_section">
                        <table class="table-sort">
                            <thead>
                            <tr>
                                <th><input type="text" class="search-input" placeholder="<fmt:message key="profile.admin.table.users.thead.th.input.1"/>"></th>
                                <th><input type="text" class="search-input" placeholder="<fmt:message key="profile.admin.table.users.thead.th.input.2"/>"></th>
                                <th><input type="text" class="search-input" placeholder="<fmt:message key="profile.admin.table.users.thead.th.input.3"/>"></th>
                                <th><input type="text" class="search-input" placeholder="<fmt:message key="profile.admin.table.users.thead.th.input.4"/>"></th>
                                <th><input type="text" class="search-input" placeholder="<fmt:message key="profile.admin.table.users.thead.th.input.5"/>"></th>
                                <th><input type="text" class="search-input" placeholder="<fmt:message key="profile.admin.table.users.thead.th.input.6"/>"></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="user" items="${sessionScope.adminList}">
                                <tr>
                                    <td>${user.login}</td>
                                    <td>${user.name}</td>
                                    <td>${user.surname}</td>
                                    <td>${user.email}</td>
                                    <td>${user.registrationDate}</td>
                                    <td>${user.role}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <div class="pagination">
                        <c:if test="${sessionScope.adminList != null}">
                            <c:if test="${sessionScope.currentPage != 1}">
                                <a href="controller?command=GET_USERS&currentPage=${sessionScope.currentPage - 1}"><ion-icon name="arrow-back-outline"></ion-icon></a>
                            </c:if>
                            <a href="controller?command=GET_USERS&currentPage=${sessionScope.currentPage}">${sessionScope.currentPage}</a>
                            <c:if test="${sessionScope.currentPage lt sessionScope.numbOfPages}">
                                <a href="controller?command=GET_USERS&currentPage=${sessionScope.currentPage + 1}"><ion-icon name="arrow-forward-outline"></ion-icon></a>
                            </c:if>
                        </c:if>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="empty"><fmt:message key="profile.admin.table.empty"/>"></p>
                </c:otherwise>
            </c:choose>
        </div>
    </form>
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
