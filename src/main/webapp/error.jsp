<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <link href="static/css/style-error.css" rel="stylesheet" />
    <title>Cargo-Delivery | Error</title>
</head>
<body>
<div class="frem">
    <p>404</p>
    <h2>Look like something go wrong!</h2>
    <h5>${sessionScope.errorMessage}</h5>
    <a href="index.jsp">Back to index</a>
</div>
</body>
</html>
