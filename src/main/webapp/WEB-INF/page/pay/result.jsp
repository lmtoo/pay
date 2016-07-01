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
<title>支付提醒</title>
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
	<link rel="stylesheet" type="text/css" href="${staticWebPath}/common/css/common_min.css?t=${today}" />
	<link rel="stylesheet" type="text/css" href="${staticWebPath}/pay/css/pay-tip.css?t=${today}" />
</head>
<body>
	<div id="page">
		<div class="tpbar"></div>
		<div class="wp" id="main">
<%-- 头部 Start --%>
<% request.setAttribute("headModel", PayController.getPageModuledule()); %>
${headModel.top_bar }
<%-- 头部 End --%>
			<input type="hidden"  name="channel" value="${msg.code}"/>
			<c:if test="${msg.code == 9}">
			<div>
				<h1><i class="icon-success"></i>支付成功</h1>
				<p class="desc">本订单使用超级帮钻全额抵现，订单已经支付成功，我们将以最高的效率为您处理订单，感谢您的惠顾！</p>
				<p class="stip">小帮热线：400-085-0505</p>
				<div class="btn">
					<a href="${returnUrl}">查看订单</a>
				</div>
			</div>
			<div class="vip-dialog">
				<div class="dialog-title"></div>
				<div class="dialog-info"></div>
				<div class="link"></div>
			</div>
			</c:if>
			<c:if test="${msg.code != 9}">
			<div>
				<h1><i class="icon-error"></i>支付出错</h1>
				<p class="desc"><c:if test="${msg.data == ''}">对不起，订单在支付过程中出现问题，请尝试进行重新支付，给您造成的不便我们深表歉意！</c:if><c:if test="${msg.data != ''}">${msg.data}</c:if></p>
				<p class="stip">如有疑问请点击右侧小帮咨询，或直接拨打小帮热线：400-085-0505</p>
				<div class="btn">
					<a href="javascript:closePage();"><c:if test="${msg.code == 2}">关闭</c:if><c:if test="${msg.code != 2}">重新支付</c:if></a>
				</div>
			</div>
			</c:if>
		</div>
	</div>
	<!-- Live800在线客服图标:客服[文本图标] 开始--> 
	<div style='display:none;'><a href='http://www.live800.com'>网站聊天</a></div><script language="javascript" src="http://chat.live800.com/live800/chatClient/textButton.js?jid=7356334111&companyID=447850&configID=124837&codeType=custom"></script><script id='write' language="javascript">function writehtml(){var temptext=text_generate();document.getElementById('live124837').innerHTML=temptext;setTimeout('write.src',9000);}writehtml();</script><div style='display:none;'><a href='http://en.live800.com'>live chat</a></div> 
	<%-- 底部 Start --%>	
	${headModel.foot_top }
	<%-- 底部 End --%>
	<!-- 在线客服图标:客服 结束--> 	
	<script type="text/javascript" id="requirejs" data-main="${staticWebPath}/pay/js/pay-tip.js?v=${today}" data-version="201502110322" src="${staticWebPath}/public/js/require-min.js?v=${today}"></script>
	<script type="text/javascript" src="${cdnTJPath}/tongji/stat3.min.js?v=${today}"></script>
</body>
</html>
<script>
function closePage(){
	var userAgent = navigator.userAgent;
	if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Presto") != -1) {
	    window.location.replace("about:blank");
	} else {
	    window.opener = null;
	    window.open("", "_self");
	    window.close();
	}
}
</script>

