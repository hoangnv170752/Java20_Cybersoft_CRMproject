<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<html lang="en">
<head>
  <title>Bootstrap Example</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>
<body>

<div class="container">
  <div class="row mt-5">
    <div class="col-md-5 m-auto mt-5">
      <h3 class="text-center">ĐĂNG NHẬP HỆ THỐNG</h3>
      <div class="p-4 border mt-4">
        <form>
            <div class="form-group">
              <label>Email</label>
              <input id="input-email" type="email" class="form-control" name="email">
            </div>
            <div class="form-group">
              <label>Mật khẩu</label>
              <input id="input-password" type="password" class="form-control" name="password">
            </div>
        </form>
        <button id="btn-login" type="submit" class="btn btn-primary">Đăng nhập</button>
        </br></br>
        <h3 id="text-announce" style="font-size: 18px; text-align: right">     </h3>

      </div>
    </div>
  </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<script src="js/login.js"></script>
</body>
</html>
