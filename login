<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<link rel="stylesheet" type="text/css" href="../css/login.css" />
</head>
<body style="background-color: #FFFFDE;">
		<div id="logo1">
		<jsp:include page="logo.jsp"/></div>
		<div id="div1">
		<form action="signin.do" method="post">
		<div id="login1">
		<div id="icon1">
		<img src="../image/id.png" width="30px" height="30px" />
		</div>
		<input type="text" name="id" id="idinput" required="required" placeholder="아이디"/>		
		</div>
		<div id="pw1">
		<div id="icon2">
		<img src="../image/password.png" width="30px" height="30px" />
		</div>
		<input type="password" name="pw" id="pwinput" required="required" placeholder="비밀번호"/>		
		</div>
		<div id="autologin">
		<input type="checkbox" name="auto1" value="auto1" />자동로그인
		</div>
		<div id="find">
		<a href="findID.jsp" style="text-decoration:none;">아이디 / 비밀번호 찾기</a>
		</div>
		<div id="button1">
		<input type="submit" value="로그인" id="submitinput" />
		</div>
		<div id="signin">
		<a href="join.jsp" style="text-decoration:none; font-size: 15px;">회원가입</a>
		</div>
		</form>
		</div>
</body>
</html>
