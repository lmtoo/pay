<%@page import="com.b5m.payment.web.controller.PayController"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="com.alibaba.fastjson.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.ResourceBundle"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="title" content="支付中心 - 帮5买 - 全网.正品.低价！" />
    <title>支付中心&nbsp;-&nbsp;微信扫码支付！</title>
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="apple-mobile-web-app-status-bar-style" content="black" />
	<meta name="format-detection" content="telephone=no" />
	<meta name="author" content="" />
	<meta name="data-mps" content="1024">
	<meta name="revisit-after" content="1 days" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<link rel="icon" href="${rootPath}/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="${rootPath}/favicon.ico" type="image/x-icon" />
	<link rel="stylesheet" type="text/css" href="${staticWebPath}/common/css/common_min.css?v=" />
	<link rel="stylesheet" type="text/css" href="${staticWebPath}/pay/css/weixin.css?v=" />
</head>
<body data-mps="38">
<input type="hidden"  id="rootPath" value="${rootPath}"/>
<div id="page">
<div class="tpbar"></div>
<%-- 头部 Start --%>
<% request.setAttribute("headModel", PayController.getPageModuledule()); %>
${headModel.top_bar }
<%-- 头部 End --%>
	<div id="main">		
		<div class="center-pay-info mt20 cfx">
			<h1>微信扫一扫，轻松付款！</h1>
			<c:set value="${fn:split(orderId, ',') }" var="orders" />
			<c:if test="${fn:length(orders) ==1}">
				<p style="margin-left: 40px;">
					<label>支付号：</label>
					<span>${orderId}</span>
				</p>
			</c:if>
			<c:if test="${fn:length(orders) >1}">
				<p style="margin-left: 40px;">
					<label>合并：</label>
					<span>${fn:length(orders)}</span>
				</p>
			</c:if>
			<p>|</p>
			<p style="margin-top: 1px;">
				<label>待支付总额：</label>
				<span class="amount"><b>￥</b><fmt:formatNumber value="${finalAmount}" pattern="#0.00#"/></span>
			</p>
		</div>		
		<div class="pay-title">
			<a class="other-pay" href="javascript:window.opener=null;window.close();">&lt; 选择其他支付方式</a>
			<h1>微信支付</h1>
		</div>
		<div class="pay-wx-sao">
			<div class="qr-code-ctn">
				<div class="qr-code" id="wxcode"></div>
				<img src="${staticWebPath}/pay/img/sao-btip.png" alt="微信扫一扫使用方式" />
			</div>
			<div class="img"><img src="${staticWebPath}/pay/img/wx-sao-tip.png" alt="" /></div>
		</div>
	</div>
</div>
<%-- 底部 Start --%>	
${headModel.foot_top }
${headModel.foot_bottom }
<%-- 底部 End --%>
<script type="text/javascript">
	var weixinCallBack = function($){
		// code here: $ is jQuery
		$("#wxcode").qrcode({
		    render: "table", //table方式 
		    width: 252, //宽度
		    height:250, //高度 
		    text: "${wxcode}" //扫码
		}); 
		getsuccessStaus();
	};
	
	var getsuccessStaus = function(){
		var orderId =  "${orderId}";
		setTimeout(function(){
			if(orderId != '' && orderId.length >0){
				$.ajax({
					url : '${rootPath}/order/details.htm',
					timeout : 10000,
					type : "POST",
					dataType : "json",
					data : {"orderId" : "${orderId}"},
					success : function(data) {
						if(data != null &&  data.ok){
							var status = data.data.status;
                            if(status === 1){
                                location.href = '${rootPath}/order/wxresult.htm?orderId=' + orderId;
							}else if(status === 0){
								getsuccessStaus();
							}
						}
					},
					error: function (data, status, e) {
						getsuccessStaus();
					}
				});
			}
		}, 6000);
	};
</script>
<script type="text/javascript" id="requirejs" data-main="${staticWebPath}/pay/js/weixin.js?v=201502110322" data-version="201502110322" src="${staticWebPath}/public/js/require-min.js?v=201502110322"></script>
<script type="text/javascript" src="${cdnTJPath}/tongji/stat3.min.js?v="></script>
</body>
</html>
