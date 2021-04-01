<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>预览</title>
</head>
<body>
<div style="display: flex;justify-content: center;">
<div style="width: 100%;padding-top: 5%;font-size: 20px">
	<!--标题  -->
	<h3 style="margin-bottom: 2%">${questionnaire.title}</h3>
	<!--简介  -->
	<p style="font-size: 24px">${questionnaire.instructions}</p>
	<!--题目主体  -->
	<form id = "form1" method="post">
		<c:forEach items="${questionnaire.list}" var="question" varStatus="status">
		 <!--题干  -->
		 	${question.order }.${question.title }
		 	<c:if test="${question.isRequired}">
			<font color="#FF0000">*</font>
			</c:if>
			<font id ="m${question.order }" color="#FF0000" style="display: none">此题为必填项，请填写</font>
		 	<br/>
			<c:if test="${question.type == '单选题'   or question.type == '性别'}">
				<!--循环选项  -->
				<c:forEach items="${question.options}" var="option" varStatus="statusoption">
					<c:if test="${option.img == 'sr'}">
						<input type="radio" name="option${question.order}" value="${option.content }" onclick="t2(&quot;demo${statusoption.count}&quot;,&quot;aa${question.order}&quot;)">${option.content }
						<br/>
						<input type="text" class = "aa${question.order}" id="demo${statusoption.count}" name="demo${statusoption.count}" style="width:90%;height: 50px;display: none">
					</c:if>
					<c:if test="${option.img != 'sr'}">
						<input type="radio" name="option${question.order}" value="${option.content }" onclick="t1(&quot;aa${question.order}&quot;)">${option.content }
						<br/>
					</c:if>
				</c:forEach>
			</c:if>
			<c:if test="${question.type == '多选题' }">
				<!--循环选项  -->
				<c:forEach items="${question.options}" var="option" varStatus="statusoption">
					<c:if test="${option.img == 'sr'}">
						<input type="checkbox" name="option${question.order}" value="${option.content }" onclick="t3(&quot;demo${statusoption.count}&quot;)">${option.content }
						<br/>
						<input type="text" class = "aa${question.order}" id="demo${statusoption.count}" name="demo${statusoption.count}" style="width:90%;height: 50px;display: none">
					</c:if>
					<c:if test="${option.img != 'sr'}">
						<input type="checkbox" name="option${question.order}" value="${option.content }">${option.content }
						<br/>
					</c:if>
				</c:forEach>
			</c:if>
			<c:if test="${question.type == '填空题' or question.type == '姓名' }">
				<input type="text" id="${question.order}" name="${question.order}" style="width:90%;height: 50px">
				<br/>
			</c:if>
			<c:if test="${question.type == '日期'}">
				<input type="date" id="${question.order}" name="${question.order}" style="width:90%;height: 50px">
				<br/>
			</c:if>
          </c:forEach>
          <input class="jquery_button" type="button" name="btnSave" id="btnSave" value="提交" style="width: 90%;display: flex;justify-content: center;height:60px;margin-top: 20dp" onclick="check()"/>          
	</form>
	</div>
</div>
<script>
	/*单选题  */
	function t1(demo) {
		var aa = document.getElementsByClassName(demo);//获取到的是一个类数组
		for(var i=0;i<aa.length;i++){
		  aa[i].style.display = "none"
		}		
	}
	function t2(demo,de) {
		var ui =document.getElementById(demo);
		ui.style.display="block";
		var aa = document.getElementsByClassName(de);//获取到的是一个类数组
		for(var i=0;i<aa.length;i++){
			if(aa[i].id != demo){
				console.log(demo)
				aa[i].style.display = "none"	
			}
		}
		
	}
	/*多选题  */
	function t3(demo) {
		var ui =document.getElementById(demo);
		if(ui.style.display == "none"){
			ui.style.display="block";	
		}else{
			ui.style.display="none";	
		}
	}
	
	
	function sub(){
		var ui =document.getElementById('btnSave');
		if(check() == 0){
			console.log('提交')
			ui.submit();
		}
	}
	function check() {  
		var flag = 0;
		<c:set var="flag" scope="session" value="0"></c:set>
		<c:forEach items="${questionnaire.list}" var="question" varStatus="status">
			<c:if test="${question.isRequired}">
				<c:if test="${question.type == '单选题'  or question.type == '性别'  }">
					var count1 = 0;
					var rcheckBox1 = document.getElementsByName('option'+${question.order});
					//根据 name集合长度 遍历name集合
					for(var i=0;i<rcheckBox1.length;i++)
					{ 
						//判断那个单选按钮为选中状态
						if(rcheckBox1[i].checked)
						{
							count1++;
						} 
					} 
			        if(count1 == 0){
			        	var ui =document.getElementById('m'+${question.order });
			    		ui.style.display="block";
			    		flag = 1; 
			        }else{
			        	var ui =document.getElementById('m'+${question.order });
			    		ui.style.display="none";
			        	<c:forEach items="${question.options}" var="option" varStatus="statusoption">
							<c:if test="${option.img == 'sr'}">
								var ui =document.getElementById('demo'+${statusoption.count});
				        		if(ui.style.display == "block"){
				        			var va = document.getElementById('demo'+${statusoption.count}).value;
				        			if(va == undefined || va == null ||va == ""){
				        				var ui =document.getElementById('m'+${question.order });
						        		ui.style.display="block";
						        		flag =1;
				        			}else{
				        				var ui =document.getElementById('m'+${question.order });
				    	        		ui.style.display="none";
				        			}	
				        		}
							</c:if>
						</c:forEach>
			        }
			</c:if>
			<c:if test="${question.type == '多选题' }">
				var count = 0;
				var rcheckBox = document.getElementsByName('option'+${question.order});
				//根据 name集合长度 遍历name集合
				for(var i=0;i<rcheckBox.length;i++)
				{ 
					//判断那个单选按钮为选中状态
					if(rcheckBox[i].checked)
					{
						count++;
					} 
				} 
	            if(count == 0){
	            	var ui =document.getElementById('m'+${question.order });
	        		ui.style.display="block";
	        		flag = 1; 
	            }else{
	            	var ui =document.getElementById('m'+${question.order });
	        		ui.style.display="none";
	            	<c:forEach items="${question.options}" var="option" varStatus="statusoption">
						<c:if test="${option.img == 'sr'}">
							var ui =document.getElementById('demo'+${statusoption.count});
			        		if(ui.style.display == "block"){
			        			var va = document.getElementById('demo'+${statusoption.count}).value;
			        			if(va == undefined || va == null ||va == ""){
			        				var ui =document.getElementById('m'+${question.order });
					        		ui.style.display="block";
					        		flag =1;
			        			}else{
			        				var ui =document.getElementById('m'+${question.order });
			    	        		ui.style.display="none";
			        			}	
			        		}
						</c:if>
					</c:forEach>
	            }
			</c:if>
			<c:if test="${question.type == '填空题' }">
				var va = document.getElementById(${question.order}).value;
    			if(va == undefined || va == null ||va == ""){
    				console.log(va);
    				var ui =document.getElementById('m'+${question.order });
	        		ui.style.display="block";
	        		flag=1;
    			}else{
    				var ui =document.getElementById('m'+${question.order });
	        		ui.style.display="none";
    			}
    			
			</c:if>
			<c:if test="${question.type == '姓名' }">
				var va = document.getElementById(${question.order}).value;
				if(va == undefined || va == null ||va == ""){
					var ui =document.getElementById('m'+${question.order });
	        		ui.style.display="block";
	        		flag=1;
				}else{
					var ui =document.getElementById('m'+${question.order });
	        		ui.style.display="none";
				}
			</c:if>
			<c:if test="${question.type == '日期' }">
				var va = document.getElementById(${question.order}).value;
				if(va == undefined || va == null ||va == ""){
					var ui =document.getElementById('m'+${question.order });
	        		ui.style.display="block";
	        		flag=1;
				}else{
    				var ui =document.getElementById('m'+${question.order });
	        		ui.style.display="none";
    			}
			</c:if>
		</c:if>
	</c:forEach>
	var ui =document.getElementById('form1');
	if(flag == 0){
		ui.action = "http://localhost:8080/WorkProject/ylx/saveInfo/${questionnaire.id}";
		ui.submit();
	}
		
	} 
</script>
</body>
</html>