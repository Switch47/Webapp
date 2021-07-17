
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Login Webapp</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
            crossorigin="anonymous"></script>
</head>
<body>
<div class="container">
    <nav class="navbar navbar-light bg-light">
        <div class="container-fluid">
            <a class="navbar-brand" href="/">SSC - Login Webapp</a>
            <a class="btn btn-light pull-right" type="button" href="/logout">
                <i class="fa fa-sign-out"></i>Logout</a>
        </div>
    </nav>
    <c:if test="${not empty message}">
        <c:choose>
            <c:when test="${hasError}">
                <div class="alert alert-danger" role="alert">
                        ${message}
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-success" role="alert">
                        ${message}
                </div>
            </c:otherwise>
        </c:choose>
    </c:if>
    <div class="row justify-content-md-center">
        <div class="col-sm-12 col-md-6 col-lg-4 mt-5">
                <h1 class="text-center">Sign Up</h1>
                <p>${error}</p>
                <form action="/user/create" method="post" autocomplete="off">
                    <div class="input-group mb-4 input-group-md">
                        <span class="input-group-text" id="username" style="width: 40px">
                            <i class="fa fa-user"></i>
                        </span>
                        <input type="text" class="form-control" name="username" placeholder="Username" aria-label="Username"
                               aria-describedby="username" autocomplete="off" value="${username}">
                    </div>
                    <div class="input-group mb-4 input-group-md">
                        <span class="input-group-text" id="display_name" style="width: 40px">
                            <i class="fa fa-user"></i></span>
                        <input type="text" class="form-control" name="display_name" placeholder="Display Name" aria-label="display_name"
                               aria-describedby="display_name" autocomplete="off" value="${display_name}">
                    </div>
                    <div class="input-group mb-4 input-group-md">
                        <span class="input-group-text" id="password" style="width: 40px"><i class="fa fa-key"></i></span>
                        <input type="password" class="form-control" name="password" placeholder="Password"
                               aria-label="Password" aria-describedby="password" autocomplete="false" autocomplete="off" value="${password}">
                    </div>
                    <div class="input-group mb-4 input-group-md">
                        <span class="input-group-text" id="cpassword" style="width: 40px">
                            <i class="fa fa-key"></i>
                        </span>
                        <input type="password" class="form-control" name="cpassword" placeholder="Confirm Password"
                               aria-label="Password" aria-describedby="cpassword" autocomplete="off" value="${cpassword}">
                    </div>
                    <div class="d-grid gap-2">
                    <button class="btn btn-success" type="submit"><i class="fa fa-sign-in"></i> &nbsp; Sign up</button>
                    </div>
                </form>
        </div>
    </div>
</div>
</body>
</html>