<%@page import="com.b5m.payment.web.controller.PayController"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="com.alibaba.fastjson.JSONObject"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.ResourceBundle"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>成功提示</title>
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<meta name="format-detection" content="telephone=no" />
<meta name="author" content="" />
<meta name="revisit-after" content="1 days" />
<meta name="keywords" content="" />
<meta name="description" content="" />
<link rel="icon" href="${rootPath}/favicon.ico" type="image/x-icon" />
<link rel="shortcut icon" href="${rootPath}/favicon.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="${staticWebPath}/common/css/common_min.css?t=${today}" />
<link rel="stylesheet" type="text/css" href="${staticWebPath}/pay/css/weixin_tip.css?t=${today}" />
</head>
<body>
<%-- 头部 Start --%>
<% request.setAttribute("headModel", PayController.getPageModuledule()); %>
${headModel.top_bar }
<%-- 头部 End --%>
	<div id="main" class="wraper">
		<div class="weixin-pay-tip">
			<img src="${staticWebPath}/common/img/placeholder.png" data-src="${staticWebPath}/pay/img/bb-sm.png" alt="" />
			<span>您的订单已经支付成功，正在带您返回我的订单。</span>
		</div>
	</div>
<%-- 底部 Start --%>	
${headModel.foot_top }
<%-- 底部 End --%>
<script type="text/javascript" id="requirejs" data-main="${staticWebPath}/pay/js/weixin_tip.js?v=201502110322" data-version="201502110322" src="http://static-web.b5m.com/public/js/require-min.js?v=201502110322"></script>
<script type="text/javascript" src="${cdnTJPath}/tongji/stat3.min.js?v="></script>
</body>
</html>