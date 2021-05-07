<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>开启问卷之旅</title>
</head>
<body>
	<div style="margin:0;padding:0;width:100%;height:100vh;background-size:100% 100%;background-image: url('../../static/images/${questionnaire.appearance}');" >
		<div style="width: 100%;text-align:center;vertical-align:middle; position:fixed;top:100px;z-index: 9999;">
			<font color="${questionnaire.imgColor}" style="font-size:30px;">${questionnaire.title}</font>
		</div>
		<div style="width: 100%;text-align:center;vertical-align:middle; position:fixed;bottom:20px;z-index: 9999;">
			<form style="width: auto;" action="http://192.168.137.1:8080/WorkProject/ylx/preview/${questionnaire.id }" method="get">
				<input align="center" style="width:60% ;height:80px; background-color:${questionnaire.imgColor};border:none;color: #FFF;font-size:30px;text-align: center;" type="submit" value="开启问卷">
			</form>
		</div>
	</div>
</body>
</html>