<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.ResourceBundle"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="com.b5m.payment.web.controller.PayController"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>

<html lang="zh-CN">
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<meta name="title" content="支付中心 - 帮5采 - 全网.正品.低价！" />
	<title>支付中心&nbsp;-&nbsp;我的B5C&nbsp;-&nbsp;帮5采&nbsp;-&nbsp;全网.正品.低价！</title>
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="apple-mobile-web-app-status-bar-style" content="black" />
	<meta name="format-detection" content="telephone=no" />
	<meta name="author" content="" />
	<meta name="data-mps" content="DO1050">
	<meta name="revisit-after" content="1 days" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<link rel="icon" href="${rootPath}/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="${rootPath}/favicon.ico" type="image/x-icon" />
	<link rel="stylesheet" type="text/css" href="${staticWebPath}/common/css/common_min.css?t=${today}" />
	<link rel="stylesheet" type="text/css" href='${staticWebPath}/pay/css/pay.css?t=${today}' />
</head>
<body data-mps="PA5021">
<input type="hidden"  id="rootPath" value="${rootPath}"/>
<c:set var="finalAmount" value="${paymentOrder.amount}" />

<div data-mps="10001" class="top-hdbanner"></div>

<%-- 头部 Start --%>
<% request.setAttribute("headModel", PayController.getPageModuledule()); %>
<%-- 头部 End --%>

<div class="wp center cfx">
	<div class="center-pay">
		<div class="center-pay-type">只差一步，请尽快支付 ${environment}</div>
		<form name="payFrm" method="post" action="${rootPath}/third/pay.htm" target="_blank">
			<div class="center-pay-detail mt10 cfx">
				<%-- 提交数据 Start--%>
				<input type="hidden"  name="userId" value="${paymentOrder.userId }"/>
				<input type="hidden"  name="orderId" value="${paymentOrder.orderId }"/>
				<input type="hidden"  name="sign" value="${paymentOrder.sign}"/>
				<input type="hidden"  name="amount" value="${paymentOrder.amount}"/>
				<input type="hidden"  name="amountDetail" value="${paymentOrder.amountDetail}"/>
				<input type="hidden"  name="totalAmount" value="${paymentOrder.amount}"/>
				<input type="hidden"  name="subject" value="${paymentOrder.subject}"/>
				<input type="hidden"  name="source" value="${paymentOrder.source}"/>
				<input type="hidden"  name="freeFeeType" value="${paymentOrder.freeFeeType}"/>
				<input type="hidden"  name="inviterId" value="${paymentOrder.inviterId}"/>
				<input type="hidden"  name="trafficSource" id="trafficSource" value=""/>
				<input type="hidden"  name="thirdPayType" value="4" id="thirdPayType"/>
				<input type="hidden"  name="usebz" value="<c:if test="${(null != paymentOrder.bzAmount) && (paymentOrder.bzAmount > 0)}">1</c:if><c:if test="${(null == paymentOrder.bzAmount) || (paymentOrder.bzAmount < 1)}">0</c:if>" />
				<input type="hidden"  name="isSuperBzPayPWD"  id="isSuperBzPayPWD" value="${isSuperBzPayPWD}"/>
				<input type="hidden"  name="userBindMobile"  id="userBindMobile" value="${userBindMobile}"/>
				<input type="hidden"  name="crossPay"  id="crossPay" value="${paymentOrder.crossPay}"/>
				<input type="hidden"  name="addressId"  id="addressId" value="${paymentOrder.addressId}"/>
				<input type="hidden"  name="isDisabledEnabledBz"  id="isDisabledEnabledBz" value="${isDisabledEnabledBz}"/>
				<input type="hidden"  name="bzMaxValue"  id="bzMaxValue" value="${bzMaxValue}"/>
				<input type="hidden"  name="bzUsable1"  id="bzUsable1" value="${paymentOrder.bzUsable}"/>
				<input type="hidden"  name="defaultSelectedBz"  id="defaultSelectedBz" value="${defaultSelectedBz}"/>
				<input type="hidden"  name="isLessBalance"  id="isLessBalance" value="${isLessBalance}"/>

				<%-- 提交数据 End--%>
				<div class="center-pay-list" data-mps="MO11000">

					<c:if test="${payOnlineChannel != null && payOnlineChannel != '' }" >
						<c:if test="${payOnlinePlatform != null && fn:length(payOnlinePlatform) > 0}" >
							<h2 class="cfx">
								<c:if test="${payOnlinePlatform != null && fn:length(payOnlinePlatform) > bankMaxLength}" >
									<a class="show-all" href="javascript:;">展开更多<i></i></a>
								</c:if>
								首信易支付
								<c:if test="${payOnlineTips != null && payOnlineTips != ''}" ><span class="rec-way" >${payOnlineTips}</span></c:if>
							</h2>
							<ul class="hide-more cfx" data-mps="34003">
								<c:set var="p1" value="0"/>
								<c:forEach items="${typeListMap['online']}" var="userItemKey" varStatus="nl">
									<c:forEach items="${payOnlinePlatform}" var="item12" varStatus="nl12">
										<c:if test="${(userItemKey == item12.keyName) }">
											<c:set var="p1" value="${p1+1}"/>
											<li <c:if test="${p1 > bankMaxLength}"> class="more" </c:if> >
												<input id="${item12.formId}" data-id="${item12.dataId}" type="radio" class="pay-radio" name="defaultBank" value="${item12.formValue}" data-thirdpaytype="${item12.thirdPayType}"/>
												<label for="${item12.formId}"><i class="pay-radio-custom"></i><img src="${staticWebPath}${item12.channelImg}?t=${today}" alt="${item12.title}" title="${item12.title}" data-mps="${item12.dataMps}"/></label>
											</li>
										</c:if>
									</c:forEach>
								</c:forEach>
							</ul>
						</c:if>
					</c:if>

				</div>
			</div>
			<div class="center-pay-btn mt20 cfx" data-mps="MO11000" >
				<input type="button" id="J_pay_next" data-attr="1019" class="pay-next fr" value="立即支付" data-mps="buynow" />
				<c:if test="${paymentOrder.crossPay != 4}">
					<strong class="pay-amount fr">待支付金额：<span>￥</span><span class="amount"><fmt:formatNumber value="${finalAmount-paymentOrder.bzAmount/100}" pattern="#0.00#"/></span></strong>
				</c:if>
			</div>
			<div id="pay-window" class="b5m-pay" style="display: none;" data-mps="MO12000" >
				<div class="pay-tip pay-suc">
					<span>如已经支付成功，请点击：</span>
					<a class="done" href="${returnUrl}" data-mps="PO1">已完成支付</a>
				</div>
				<div class="pay-tip pay-fai">
					<span>如支付遇到问题，你可以：</span>
					<a class="choose" href="javascript:;" data-mps="PO2">重新支付</a>
				</div>
				<div class="pay-footer">
					<span class="net-tip">若因网络不佳，打开支付页面慢，请耐心等候</span>
					<span>客服电话：</span>
					<span class="phone-num">400-097-9655</span>
				</div>
			</div>
			<div id="bz-tip" class="b5m-pay" style="display: none;">
				<h3>温馨提示</h3>
				<p>你的超级帮钻数量发生了变化，导致了余额不足，请刷新页面后重试。</p>
				<a href="javascript:;" class="reload-btn">刷新页面</a>
			</div>
			<div id="no-slt-window" class="b5m-pay no-slt" style="display: none;">
				<div class="pay-tip pay-fai">
					<span>请选择支付方式！</span>
				</div>
				<div class="pay-tip" >
					<a class="done" href="javascript:;">确定</a>
				</div>
			</div>
		</form>
	</div>
</div>
<script type="text/javascript">
	//订单支付读取来源
	function getCookie(name) {
		var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
		if(arr != null) return unescape(arr[2]);
		return null;
	}
	document.getElementById("trafficSource").value=getCookie('_b5mtraffic');
</script>
<script type="text/javascript" id="requirejs" data-main="${staticWebPath}/??common/js/b5m_host.js,pay/js/pay.js?v=${today}" data-version="${today}" src="${cdnTJPath}/public/js/require-min.js?v=${today}"></script>
<script src="${cdnTJPath}/tongji/stat3.min.js?t=${today}" type="text/javascript"></script>
</body>
</html>
