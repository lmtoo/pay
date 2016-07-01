<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<link href="${ctx}/resources/bootstrap/3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script src="${ctx}/resources/jquery/jquery-2.1.3.min.js" type="text/javascript"></script>
<script src="${ctx}/resources/bootstrap/3.3.0/js/bootstrap.min.js"></script>   <!-- 顺序重要 -->
<!-- bootstrap dialog -->
<link href="${ctx}/resources/bootstrap3-dialog/css/bootstrap-dialog.min.css" rel="stylesheet" type="text/css">
<script src="${ctx}/resources/bootstrap3-dialog/js/bootstrap-dialog.min.js"></script>
<style>
	#loginForm{
		margin:18% auto;
	}
	@media screen and (max-width: 600px){
		#loginForm{
			margin:280px auto;
		}
	}
</style>
<title>登录 - 支付中心</title>
<script type="text/javascript">
	$(function(){
		var serviceUrl = "/console/"
		$("#get").click(function(){
			var url = serviceUrl + 'get/key.htm';
			console.log("目标地址:" + url);
			$.getJSON(url, {key : $("#key1").val()}, function(data){
				BootstrapDialog.alert((data || {})["msg"]);
			});
		});
		$("#login").click(function(){
			var url = serviceUrl + 'login.htm';
			console.log("目标地址:" + url);
			$.getJSON(url, {key : $("#key2").val()}, function(data){
				if((data || {})["data"] == true){
					window.location.href = serviceUrl + 'index.htm';
				}else{
					BootstrapDialog.alert("登录口令错误");
				}
			});
		});
	});
</script>
</head>
<body>
	<form id="loginForm" action="" method="post" class="form-horizontal" role="form" style="width:350px;">
		<div class="form-group">
			<div class="col-lg-3">
				<div class="input-group shopCategoryGroup" style="width:100%">
					<input type="text" id="key1" name="key1" class="form-control required" style="min-width:260px"/>
					<span class="input-group-btn">
						<button class="btn btn-primary" id="get" type="button">获取口令</button>
					</span>
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="col-lg-3">
				<div class="input-group shopCategoryGroup" style="width:100%">
					<input type="text" id="key2" name="key2" class="form-control required" style="min-width:260px"/>
					<span class="input-group-btn">
						<button id="login" class="btn btn-primary" type="button">即刻登录</button>
					</span>
				</div>
			</div>
		</div>
	</form>
</body>
</html>