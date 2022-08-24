<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tf" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register rent page</title>
    <link rel="stylesheet" href="<c:url value="/static/css/form.css"/>">
    <%@ include file="../partial/head.jspf" %>
</head>
<body>
<header>
    <tf:chooseHeader role="${sessionScope.role}"/>
</header>

<main>
    <input type="hidden" id="status" value="<%= request.getAttribute("status")%>">
    <c:set var="car" value="${sessionScope.car}"/>
    <div class="container">
        <h1>Rent registration</h1>
        <p>Car Info (car: <c:out value="${car.brand} ${car.modelName}"/>, Price:<c:out value="${car.price}"/>)</p>
        <form action="${pageContext.request.contextPath}/Customer/rent" method="post">
            <input name="carPrice" value="${car.price}" type="hidden">
            <div class="form-control">
                <label>Passport data
                    <input type="text" placeholder="Passport code (9 digit)" name="passportData" required="required">
                </label>
            </div>
            <p>Driver options (+10$ per/day)</p>
            <p><input class="filter-checkbox-order" type="checkbox" name="withDriver">With driver</p>
            <div class="form-control">
                <label>Rent from:
                    <input autocomplete="off" id="startDateField" type="text" placeholder="Enter date" name="startDate" required="required" oninput="">
                </label>
            </div>
            <div class="form-control">
                <label>Rent to:
                    <input autocomplete="off" id="endDateField" type="text" placeholder="Enter date" name="endDate" required="required">
                </label>
            </div>
            <div class="form-control">
                <input type="submit" name="signup" value="Request" class="btn">
            </div>
        </form>
    </div>
</main>

<footer>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/js/datePicker.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11.4.19/dist/sweetalert2.all.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/static/js/messages/registerRentMessages.js"></script>
</footer>

</body>
</html>
