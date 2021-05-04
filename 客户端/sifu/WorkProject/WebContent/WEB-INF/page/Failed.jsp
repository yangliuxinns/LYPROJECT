<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>成功失败</title>
<style>
        * {
            margin: 0px;
            padding: 0px;
        }
        
        .box {
            width: 500px;
            margin: 45% auto;
        }
        
        .box-bg {
            width:100%;
            height: 500px;
        }
     
        .box-text {
            width: 100%;
        }
        
        .box-text h3 {
            text-align: center;
            padding: 50px 0;
            font-size: 24px;
        }
</style>
</head>
<body>
	<div class="box">
        <div class="box-bg" align="center">
            <img alt="" src="${ctx }/static/images/Fail.png" style="width:500px;height: 500px"></img>
        </div>
        <div class="box-text">
            <h3>很抱歉提交失败，请重试</h3>
        </div>
    </div>
</body>
</html>