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
	<div style="display: flex;justify-content: center;width: 100%;height: 100%;background-image: url('../../static/images/cc.png')">
		<form action="http://192.168.10.223:8080/WorkProject/ylx/preview/${questionnaire.id }" method="post">
			<input type="submit" value="开启问卷">
		</form>
	</div>
</body>
</html>