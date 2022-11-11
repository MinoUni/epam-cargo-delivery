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
    <title>Cargo-Delivery | A-Profile ${sessionScope.user.id}</title>
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
        <a href="profile_admin.jsp">Profile</a>
        <a href="admin?action=getUsers">Load Users</a>
        <a href="admin?action=getOrders&currentPage=1">Load Orders</a>
    </nav>
    <div class="icons">
        <div class="theme">
          <span class="icon">
            <ion-icon name="sunny-outline" class="light"></ion-icon>
            <ion-icon name="moon-outline" class="dark"></ion-icon>
          </span>
        </div>
        <div class="lang">
            <ion-icon name="globe-outline"></ion-icon>
        </div>
        <div class="logout">
            <a href="login">
                <ion-icon name="exit-outline"></ion-icon>
            </a>
        </div>
    </div>
</header>

<section>
    <form action="" method="get">
        <div class="table">
            <div class="table_header">
                <p>Admin com<span class="yellow">mand result</span></p>
            </div>
            <c:choose>

                <c:when test="${sessionScope.adminAction eq AdminAction.ORDERS}">
                    <div class="table_section">
                        <table class="table-sort">
                            <thead>
                            <tr>
                                <th><input type="text" class="search-input" placeholder="Order id"></th>
                                <th><input type="text" class="search-input" placeholder="Cargo id"></th>
                                <th><input type="text" class="search-input" placeholder="Price"></th>
                                <th><input type="text" class="search-input" placeholder="From"></th>
                                <th><input type="text" class="search-input" placeholder="To"></th>
                                <th><input type="text" class="search-input" placeholder="Registration date"></th>
                                <th><input type="text" class="search-input" placeholder="Delivery date"></th>
                                <th><input type="text" class="search-input" placeholder="State"></th>
                                <th>Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="order" items="${sessionScope.adminList}">
                                <tr>
                                    <td>${order.id}</td>
                                    <td>${order.cargoId}</td>
                                    <td>${order.price}</td>
                                    <td>${order.routeStart}</td>
                                    <td>${order.routeEnd}</td>
                                    <td>${order.registrationDate}</td>
                                    <td>${order.deliveryDate}</td>
                                    <td>${order.state}</td>
                                    <td>
                                        <div class="actions">
                                            <c:if test="${order.state eq OrderState.REGISTERED}">
                                                <a href="admin?action=approve&orderId=${order.id}"
                                                   class="info_action">Approve</a>
                                            </c:if>
                                            <c:if test="${order.state eq OrderState.WAITING_FOR_PAYMENT or
                                                          order.state eq OrderState.REGISTERED}">
                                                <a href="admin?action=block&orderId=${order.id}"
                                                   class="del_action">Block</a>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>

                <c:when test="${sessionScope.adminAction eq AdminAction.USERS}">
                    <div class="table_section">
                        <table class="table-sort">
                            <thead>
                            <tr>
                                <th><input type="text" class="search-input" placeholder="User id"></th>
                                <th><input type="text" class="search-input" placeholder="Name"></th>
                                <th><input type="text" class="search-input" placeholder="Surname"></th>
                                <th><input type="text" class="search-input" placeholder="Email"></th>
                                <th><input type="text" class="search-input" placeholder="Role"></th>
                                <th><input type="text" class="search-input" placeholder="Balance"></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="user" items="${sessionScope.adminList}">
                                <tr>
                                    <td>${user.id}</td>
                                    <td>${user.name}</td>
                                    <td>${user.surname}</td>
                                    <td>${user.email}</td>
                                    <td>${user.userRole}</td>
                                    <td>${user.balance}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <p class="empty">No command was used!</p>
                </c:otherwise>
            </c:choose>
        </div>
    </form>
    <div class="pagination">
        <c:if test="${sessionScope.adminAction eq AdminAction.ORDERS and sessionScope.adminList != null}">
            <c:if test="${sessionScope.currentPage != 1}">
                <a href="admin?action=getOrders&currentPage=${sessionScope.currentPage - 1}"><ion-icon name="arrow-back-outline"></ion-icon></a>
            </c:if>
            <a href="admin?action=getOrders&currentPage=${sessionScope.currentPage}">${sessionScope.currentPage}</a>
            <c:if test="${sessionScope.currentPage lt sessionScope.numbOfPages}">
                <a href="admin?action=getOrders&currentPage=${sessionScope.currentPage + 1}"><ion-icon name="arrow-forward-outline"></ion-icon></a>
            </c:if>
        </c:if>
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
