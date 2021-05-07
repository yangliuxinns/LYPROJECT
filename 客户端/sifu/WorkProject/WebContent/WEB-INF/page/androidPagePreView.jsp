<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="referrer" content="no-referrer">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes" />
<title>预览</title>
<link rel="stylesheet" href="../../static/style.css">
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=xWfzGbIe2LFNjHNtjmsCBsB56UExdabO"></script>
</head>
<body>
<div style="display: flex;justify-content: center;">
<div style="width: 100%;padding-top: 5%;font-size: 18px;margin-left: 5%;margin-right: 5%">
	<!--标题  -->
	<h3 style="margin-bottom: 2%;text-align:center;color:${questionnaire.imgColor};">${questionnaire.title}</h3>
	<!--简介  -->
	<p style="font-size: 20px;text-align:left">${questionnaire.instructions}</p>
	<!--题目主体  -->
	<form id = "form1" method="post">
		<c:forEach items="${questionnaire.list}" var="question" varStatus="status">
			<!--每道题 -->
			<c:if test="${question.pageNumber == 1}">
				<div id = "div${question.order}">
			</c:if>
			<c:if test="${question.pageNumber != 1}">
				<div id = "div${question.order}" style="display:none ">
			</c:if>
		 <!--题干  -->
	 		<span style="display: inline-block;margin: 8px 0;">${question.order }.${question.title }</span>
		 	<c:if test="${question.isRequired}">
				<font color="#FF0000">*</font>
			</c:if>
			<font id ="m${question.order }" color="#FF0000" style="display: none;font-size:10px;">此题为必填项，请填写</font>
		 	<br/>
			<c:if test="${question.type == '单选题'   or question.type == '性别'}">
				<!--循环选项  -->
				<c:forEach items="${question.options}" var="option" varStatus="statusoption">
					<c:if test="${option.img == 'sr'}">
						<span style="justify-content: center; border: 1px solid #F5F5F5;display: block;margin-bottom: -20px; padding:4px 10px;border-radius: 4px">
							<input type="radio" name="option${question.order}" value="${option.content }" onclick="t2(&quot;demo${statusoption.count}&quot;,&quot;aa${question.order}&quot;,&quot;de${question.order}&quot;,&quot;de${statusoption.count}&quot;)" style="border:1px solid red">${option.content }
						</span>
						<br/>
						<span class = "de${question.order}" id="de${statusoption.count}" style="border: 1px solid #F5F5F5;display: block;margin-bottom: 0px; padding:4px 10px;border-radius: 4px;display: none">
							<input type="text" class = "aa${question.order}" id="demo${statusoption.count}" value="" name="demo${statusoption.count}" style="width:100%;height:25px; display: none;border:none;">
						</span>
					</c:if>
					<c:if test="${option.img != 'sr'}">
						<span style="border: 1px solid #F5F5F5;display: block;margin-bottom: -20px; padding:4px 10px;border-radius: 4px">
							<input type="radio" name="option${question.order}" value="${option.content }" onclick="t1(&quot;aa${question.order}&quot;,&quot;de${question.order}&quot;)">&nbsp;&nbsp;${option.content }
						</span>
						<br/>
						<c:if test="${option.imgcontent != null}">
							<div style="border: 1px solid #F5F5F5;border-radius: 4px;width: 50px;height: 50px">
								<img src="http://192.168.137.1:8080/WorkProject/ylx/seekExperts?id=${option.id }&pId=${questionnaire.id}" style="width: 50px;height: 50px">
							</div>
						<br>
						</c:if>
					</c:if>
				</c:forEach>
			</c:if>
			<c:if test="${question.type == '多选题' }">
				<!--循环选项  -->
				<c:forEach items="${question.options}" var="option" varStatus="statusoption">
					<c:if test="${option.img == 'sr'}">
						<span style="border: 1px solid #F5F5F5;display: block;margin-bottom: -20px; padding:4px 10px;border-radius: 4px">
							<input type="checkbox" name="option${question.order}" value="${option.content }" onclick="t3(&quot;demo${statusoption.count}&quot;,&quot;de${statusoption.count}&quot;)">${option.content }
						</span>
						<br/>
						<span id = "de${statusoption.count}" style="border: 1px solid #F5F5F5;display: none;margin-bottom: 0px; padding:4px 10px;border-radius: 4px">
							<input type="text" value="" class = "aa${question.order}" id="demo${statusoption.count}" name="demo${statusoption.count}" style="width:100%;height:25px;display: none;border:none;">
						</span>
					</c:if>
					<c:if test="${option.img != 'sr'}">
						<span style="border: 1px solid #F5F5F5;display: block;margin-bottom: -20px; padding:4px 10px;border-radius: 4px">
							<input type="checkbox" name="option${question.order}" value="${option.content }">${option.content }
						</span>
						<br/>
						<c:if test="${option.imgcontent != null}">
							<div style="border: 1px solid #F5F5F5;border-radius: 4px;width: 50px;height: 50px">
								<img src="http://192.168.137.1:8080/WorkProject/ylx/seekExperts?id=${option.id }&pId=${questionnaire.id}" style="width: 50px;height: 50px">
							</div>
						<br>
						</c:if>
					</c:if>
				</c:forEach>
			</c:if>
			<c:if test="${question.type == '填空题' or question.type == '姓名' or question.type == '手机'}">
				<input type="text" id="${question.order}" name="${question.order}" value=""  style="width:100%;height: 50px;margin: 8px 0;">
				<br/>
			</c:if>
			<c:if test="${question.type == '日期'}">
				<input type="date" id="${question.order}" name="${question.order}" value=""  style="width:100%; height:50px;margin: 8px 0">
				<br/>
			</c:if>
			<c:if test="${question.type == '地区'}">
				<div>
				    <fieldset>
				        <form action="#">
				            <label for="addr-show">
				            	<input type="text" id="${question.order}" name="${question.order}" value="" style="width:100%; height:50px;margin: 8px 0">
				            </label>
				            <br/>
				 
				            <!--省份选择-->
				            <select id="prov" onchange="showCity(this,&quot;${question.order}&quot;)">
				                <option>请选择省份=</option>
				            </select>
				 
				            <!--城市选择-->
				            <select id="city" onchange="showCountry(this,&quot;${question.order}&quot;)">
				                <option>请选择城市=</option>
				            </select>
				 
				            <!--县区选择-->
				            <select id="country" onchange="selecCountry(this)">
				                <option>请选择县区=</option>
				            </select>
				            <button type="button" class="btn met1" onclick="showAddr(&quot;${question.order}&quot;)" id="button-show" >确定</button>
				        </form>
				    </fieldset>
				</div>
			</c:if>
			<c:if test="${question.type == '地图' }">
					<input type="text" id="${question.order}" name="${question.order}" style="width:80%;height: 50px;margin: 8px 0;">
					<button type="button" value="点击获取地理位置" style="width:100px; height: 30px;margin: 8px 2px;" onclick="auto(&quot;ip${question.order}&quot;,&quot;container${question.order}&quot;,&quot;btn${question.order}&quot;,&quot;tx${question.order}&quot;,&quot;divs${question.order}&quot;)">点击获取位置</button>
					<br/>	
					<div id="divs${question.order}" style="display: none;border: 1px solid #F5F5F5;border-radius: 4px">
						<div style="margin:10px">
						请输入:<input type="text" id="ip${question.order}" size="30" value="地点" style="width:300px;" />
						 	<button id="btn${question.order}" style="width:100px; height: 20px;margin: 10px 2px;">搜索</button>
						</div>
						<div id="container${question.order}" style="width: 100%;height: 200px"></div>
						<div style="margin:10px">
						您的地址：<input type="text" id="tx${question.order}" style="width:300px;">
							<button type="button" id="ok${question.order}" style="width:100px; height: 20px;margin: 8px 2px;" onClick="ok(&quot;divs${question.order}&quot;,&quot;ok${question.order}&quot;,&quot;${question.order}&quot;,&quot;tx${question.order}&quot;)">确定</button>
						</div>
					</div>			
			</c:if>
			</div>
          </c:forEach>
          <input id = 'nm' value="1" type="hidden">
          <div style="display: flex;text-align:center;justify-content: center;margin-bottom: 15px;">
          		<input class="jquery_button" type="button" name="btnTop" id="btnTop" value="上一页" style="display:none; width: 100%;justify-content: center;height:50px;margin-top: 50px;background-color:${questionnaire.imgColor};color: #fff;font-size: 22px;border:0px solid red;border-radius: 4px;margin-right:2px" onclick="cha()"/>                    
          		<input class="jquery_button" type="button" name="btnNext" id="btnNext" value="下一页" style="width: 100%;justify-content: center;height:50px;margin-top: 50px;background-color:${questionnaire.imgColor};color: #fff;font-size: 22px;border:0px solid red;border-radius: 4px" onclick="che()"/>                    
          		<input class="jquery_button" type="button" name="btnSave" id="btnSave" value="提交" style="display:none;width: 100%;justify-content: center;height:50px;margin-top: 50px;background-color:${questionnaire.imgColor};color: #fff;font-size: 22px;border:0px solid red;border-radius: 4px" onclick="check()"/>                    
          		
          </div>
	</form>
	</div>
</div>
<script type="text/javascript">
		
	function auto(demo,demo2,btn,tx,div) {
		
		
		function G(id) {
		    return document.getElementById(id);
		}
		var div1 = G(div);
		div1.style.display="block";
		var btn = G(btn);
		var txt = G(tx);
		var map = new BMap.Map(demo2);
		var point = new BMap.Point(116.331398,39.897445);
		map.centerAndZoom(point,12);
		map.enableScrollWheelZoom(true);
		var geoc = new BMap.Geocoder(); 
	    var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
	    	    {"input" : demo
	    	    ,"location" : map
	    	});
	    	 
    	ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
    		
    		var str = "";
    	    var _value = e.fromitem.value;
    	    var value = "";
    	    if (e.fromitem.index > -1) {
    	        value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
    	    }    
    	    str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;
    	    
    	    value = "";
    	    if (e.toitem.index > -1) {
    	        _value = e.toitem.value;
    	        value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
    	    }    
    	    str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
    	    console.log(str);
    	});
    	 
    	var myValue;
    	ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
    		var _value = e.item.value;
    	    myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
    	    txt.value = myValue;
    	    setPlace();
    	});
    	 
    	function setPlace(){// 创建地址解析器实例
    		var myGeo = new BMap.Geocoder();// 将地址解析结果显示在地图上,并调整地图视野
    		myGeo.getPoint(myValue, function(point){
    		  if (point) {
    		    map.centerAndZoom(point, 16);
    		    map.addOverlay(new BMap.Marker(point));
    		  }
    		}, "北京");
    	}
    	map.addEventListener('click', function (e) {
    		//通过点击百度地图，可以获取到对应的point, 由point的lng、lat属性就可以获取对应的经度纬度     
    	    var pot = e.point;
    	    geoc.getLocation(pot, function(rs){
    	        //addressComponents对象可以获取到详细的地址信息
    	        var addComp = rs.addressComponents;
    	        var site = addComp.province  + addComp.city + addComp.district  + addComp.street + addComp.streetNumber;
    	        console.log(site);   //详细地址
    	        txt.value = site;
    	    });    
    	})
    	 
    	 
    	//定位到当前位置
    	var geolocation = new BMap.Geolocation();
    	geolocation.getCurrentPosition(function(r){ 
    		if(this.getStatus() == BMAP_STATUS_SUCCESS){
    			geoc.getLocation(r.point, function(rs){
        	        //addressComponents对象可以获取到详细的地址信息
        	        var addComp = rs.addressComponents;
        	        var site = addComp.province  + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
        	        txt.value = site;
        
        	    });   
    		}
    		else {
    			alert('failed'+this.getStatus());
    		}        
    	});
	    	 
	}
	
	 

</script>
<script src="../../static/city.js"></script>
<script src="../../static/me.js"></script>
<script>
	function ok(arr,brr,txt,value) {
		var div =document.getElementById(arr);
		var btn =document.getElementById(brr); 
		var tx = document.getElementById(txt);
		var address = document.getElementById(value);
		tx.value = address.value;
		div.style.display = "none";
	}
	/*单选题  */
	function t1(demo,de) {
		var aa = document.getElementsByClassName(demo);//获取到的是一个类数组
		var ab = document.getElementsByClassName(de);//获取到的是一个类数组
		for(var i=0;i<aa.length;i++){
		  aa[i].style.display = "none"
		}	
		for(var i=0;i<ab.length;i++){
		  ab[i].style.display = "none"
		}	
	}
	function t2(demo,de,dd,da) {
		var ui =document.getElementById(demo);
		var aa2 = document.getElementsByClassName(dd);
		var ua = document.getElementById(da);
		var aa = document.getElementsByClassName(de);//获取到的是一个类数组
			ui.style.display="block";
			ua.style.display="block";
		for(var i=0;i<aa.length;i++){
			if(aa[i].id != demo){
				aa[i].style.display = "none"	
			}
		}
		for(var i=0;i<aa2.length;i++){
			if(aa2[i].id != da){
				aa2[i].style.display = "none"	
			}
		}
		
	}
	function cha(){
		var top = document.getElementById('btnTop');
		var next = document.getElementById('btnNext');
		var save = document.getElementById('btnSave');
		var a= document.getElementById('nm');
		var s = a.value*1;
		s=s-1;
		a.value =s;
		console.log(s);
		<c:forEach items="${questionnaire.list}" var="question" varStatus="status">
			var div${question.pageNumber} = document.getElementById('div'+${question.order });
			if(${question.pageNumber} == s){
				div${question.pageNumber}.style.display="block";
			}else{
				div${question.pageNumber}.style.display="none";
			}
		</c:forEach>
		if(s != 1){
			top.style.display="block";
			top.style.width = "50%";
		}else{
			top.style.display="none";
		}
		if(s != ${questionnaire.totalPage}){
			next.style.display="block";
			save.style.display="none";
			next.style.width = "50%";
		}else{
			next.style.display="none";
			save.style.display="block";
			save.style.width = "50%";
		}
	  }
	function che(){
		var top = document.getElementById('btnTop');
		var next = document.getElementById('btnNext');
		var save = document.getElementById('btnSave');
		var a= document.getElementById('nm');
		var s = a.value*1;
		if(check2() == 0){
			s=s+1;
			a.value =s;
			console.log(s);
			<c:forEach items="${questionnaire.list}" var="question" varStatus="status">
				var div${question.pageNumber} = document.getElementById('div'+${question.order });
				if(${question.pageNumber} == s){
					div${question.pageNumber}.style.display="block";
				}else{
					div${question.pageNumber}.style.display="none";
				}
			</c:forEach>
			if(s != 1){
				top.style.display="block";
			}else{
				top.style.display="none";
			}
			if(s != ${questionnaire.totalPage}){
				next.style.display="block";
				save.style.display="none";
			}else{
				next.style.display="none";
				save.style.display="block";
			}
		}
	  }
	/*多选题  */
	function t3(demo,demo2) {
		var ui =document.getElementById(demo);
		var ui2 =document.getElementById(demo2);
		if(ui.style.display == "none"){
			ui.style.display="block";
			ui2.style.display="block";
		}else{
			ui.style.display="none";
			ui2.style.display="none";
		}
	}
	
	
	function sub(){
		var ui =document.getElementById('btnSave');
		if(check() == 0){
			ui.submit();
		}
	}
	function check2(){
		var flag = 0;
		var a= document.getElementById('nm');
		var s = a.value*1;
		<c:forEach items="${questionnaire.list}" var="question" varStatus="status">
			if(${question.pageNumber} == s){
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
				<c:if test="${question.type == '地图' }">
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
				<c:if test="${question.type == '地区' }">
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
				<c:if test="${question.type == '手机' }">
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
			}
		</c:forEach>
		return flag;
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
				<c:if test="${question.type == '地图' }">
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
				<c:if test="${question.type == '地区' }">
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
				<c:if test="${question.type == '手机' }">
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
		/*检查上传数据  */
		<c:forEach items="${questionnaire.list}" var="question" varStatus="status">
			<c:if test="${question.type == '单选题'  or question.type == '性别'  }">
			console.log("题号"+${question.order});
				var count1 = 0;
				var rcheckBox1 = document.getElementsByName('option'+${question.order});
				//根据 name集合长度 遍历name集合
				for(var i=0;i<rcheckBox1.length;i++)
				{ 
					//判断那个单选按钮为选中状态
					if(rcheckBox1[i].checked)
					{
						count1++;
						console.log("选项内容"+rcheckBox1[i].value);
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
			        			console.log("其他内容"+va);
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
			console.log("题号"+${question.order});
				var count = 0;
				var rcheckBox = document.getElementsByName('option'+${question.order});
				//根据 name集合长度 遍历name集合
				for(var i=0;i<rcheckBox.length;i++)
				{ 
					//判断那个单选按钮为选中状态
					if(rcheckBox[i].checked)
					{
						count++;
						console.log("选项内容"+rcheckBox1[i].value);
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
			        			console.log("其他内容"+va);
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
				console.log("题号"+${question.order});
					var va = document.getElementById(${question.order}).value;
					console.log("填空"+va);
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
				console.log("题号"+${question.order});
					var va = document.getElementById(${question.order}).value;
					console.log("姓名"+va);
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
				console.log("题号"+${question.order});
					var va = document.getElementById(${question.order}).value;
					console.log("日期"+va);
					if(va == undefined || va == null ||va == ""){
						var ui =document.getElementById('m'+${question.order });
		        		ui.style.display="block";
		        		flag=1;
					}else{
	    				var ui =document.getElementById('m'+${question.order });
		        		ui.style.display="none";
	    			}
				</c:if>
				<c:if test="${question.type == '地图' }">
				console.log("题号"+${question.order});
					var va = document.getElementById(${question.order}).value;
					console.log("地图"+va);
					if(va == undefined || va == null ||va == ""){
						var ui =document.getElementById('m'+${question.order });
		        		ui.style.display="block";
		        		flag=1;
					}else{
						var ui =document.getElementById('m'+${question.order });
		        		ui.style.display="none";
					}
				</c:if>
				<c:if test="${question.type == '地区' }">
				console.log("题号"+${question.order});
					var va = document.getElementById(${question.order}).value;
					console.log("地区"+va);
					if(va == undefined || va == null ||va == ""){
						var ui =document.getElementById('m'+${question.order });
		        		ui.style.display="block";
		        		flag=1;
					}else{
						var ui =document.getElementById('m'+${question.order });
		        		ui.style.display="none";
					}
				</c:if>
				<c:if test="${question.type == '手机' }">
					console.log("题号"+${question.order});
					var va = document.getElementById(${question.order}).value;
					console.log("手机"+va);
					if(va == undefined || va == null ||va == ""){
						var ui =document.getElementById('m'+${question.order });
		        		ui.style.display="block";
		        		flag=1;
					}else{
						var ui =document.getElementById('m'+${question.order });
		        		ui.style.display="none";
					}
				</c:if>
		</c:forEach>
		var ui =document.getElementById('form1');
		if(flag == 0){
			alert('此为预览界面，不能提交');
		} 
	} 
</script>
</body>
</html>