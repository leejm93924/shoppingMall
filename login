<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<style>
@media all and (max-width: 360px) { /* 320px 이상은 모바일 */
body{ background:#FFFFDE; }
#logo1 {
	position: absolute;
	top: 10%;
	left: 23%;
}
#div1 {
	position: absolute;
	top: 20%;
	left: 0%;
	width: 500px;
	height: 300px;
}
#login1 {
	position: absolute;
	top: 10%;
	left: 0%;
	width: 50px;
	height: 50px;
	border: 1px solid #BDBDBD;
}
#icon1 {
	position: absolute;
	top: 20%;
	left: 20%;
}
#idinput {
	position: absolute;
	top: -1%;
	left: 100%;
	width: 300px;
	height: 46px;
	font-size: 25px;
}
#pw1 {
	position: absolute;
	top: 40%;
	left: 0%;
	width: 50px;
	height: 50px;
	border: 1px solid #BDBDBD;
}
#icon2 {
	position: absolute;
	top: 20%;
	left: 20%;
}
#pwinput {
	position: absolute;
	top: -1%;
	left: 100%;
	width: 300px;
	height: 46px;
	font-size: 25px;
}
#autologin {
	position: absolute;
	top: 60%;
	left: 0%;
}
#find {
	position: absolute;
	top: 60%;
	left: 35%;
}
#button1 {
	position: absolute;
	top: 75%;
	left: 0%;
}
#submitinput {
	width: 360px; 
	height: 50px; 
	background-color: #368AFF;
	font-size: 20px;
	font-family: "굴림"; 
}
#signin {
	position: absolute;
	top: 95%;
	left: 30%;
}
#msg1 {
	position: absolute;
	top: 110%;
	left: 13%;
	color: #DB0000;
	font-size: 20px;
	
}
}
@media all and (min-width: 400px) { /* 960px 이상은 PC */
body{ background:#FFFFDE; }
#logo1 {
	position: absolute;
	top: 10%;
	left: 38%;
}
#div1 {
	position: absolute;
	top: 20%;
	left: 31%;
	width: 500px;
	height: 300px;
}
#login1 {
	position: absolute;
	top: 10%;
	left: 9%;
	width: 50px;
	height: 50px;
	border: 1px solid #BDBDBD;
}
#icon1 {
	position: absolute;
	top: 20%;
	left: 20%;
}
#idinput {
	position: absolute;
	top: -1%;
	left: 100%;
	width: 300px;
	height: 46px;
	font-size: 25px;
}
#pw1 {
	position: absolute;
	top: 40%;
	left: 9%;
	width: 50px;
	height: 50px;
	border: 1px solid #BDBDBD;
}
#icon2 {
	position: absolute;
	top: 20%;
	left: 20%;
}
#pwinput {
	position: absolute;
	top: -1%;
	left: 100%;
	width: 300px;
	height: 46px;
	font-size: 25px;
}
#autologin {
	position: absolute;
	top: 60%;
	left: 7%;
}
#find {
	position: absolute;
	top: 60%;
	left: 50%;
}
#button1 {
	position: absolute;
	top: 75%;
	left: 9%;
}
#submitinput {
	width: 360px; 
	height: 50px; 
	background-color: #368AFF;
	font-size: 20px;
	font-family: "굴림"; 
}
#signin {
	position: absolute;
	top: 95%;
	left: 39%;
}
#msg1 {
	position: absolute;
	top: 110%;
	left: 20%;
	color: #DB0000;
	font-size: 20px;	
}
} 
</style>
<!-- <link rel="stylesheet" type="text/css" href="../css/login.css" />
<link rel="stylesheet" type="text/css" href="../css/login_mobile.css" /> -->
</head>
<body style="background-color: #FFFFDE;">
	<div id="logo1">
		<jsp:include page="logo.jsp" /></div>
	<div id="div1">
		<form action="signin.do" method="post">
			<div id="login1">
				<div id="icon1">
					<img src="../image/id.png" width="30px" height="30px" />
				</div>
				<input type="text" name="id" id="idinput" required="required"
					placeholder="아이디" />
			</div>
			<div id="pw1">
				<div id="icon2">
					<img src="../image/password.png" width="30px" height="30px" />
				</div>
				<input type="password" name="pw" id="pwinput" required="required"
					placeholder="비밀번호" />
			</div>
			<div id="autologin">
				<input type="checkbox" name="auto1" value="auto1" />자동로그인
			</div>
			<div id="find">
				<a href="findID.jsp" style="text-decoration: none;">아이디 / 비밀번호찾기</a>
			</div>
			<div id="button1">
				<input type="submit" value="로그인" id="submitinput" />
			</div>
			<div id="signin">
				<a href="join.jsp" style="text-decoration: none; font-size: 15px;">회원가입</a> <br />
			</div>
			<div id="msg1">
				<p align="center">${msg1 }</p>
				<p align="center">${msg2 }</p>
			</div>
		</form>
	</div>
</body>

</html>
