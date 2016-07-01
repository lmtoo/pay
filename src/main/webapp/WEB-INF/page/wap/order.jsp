<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>支付订单</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=no, minimal-ui" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="format-detection"content="telephone=no, email=no" />
    <meta content="telephone=no" name="format-detection" />
    <!-- uc强制竖屏 -->
    <meta name="screen-orientation" content="portrait">
    <meta name="full-screen" content="yes">
    <meta name="browsermode" content="application">
    <!-- QQ强制竖屏 -->
    <meta name="x5-orientation" content="portrait">
    <meta name="x5-fullscreen" content="true">
    <meta name="x5-page-mode" content="app">

    <link href="${staticWebPath}/b5mcdn/wap/css/common.css?t=${today}" rel="stylesheet" type="text/css" />
    <c:if test="${!isBHBBusiness}">
        <link rel="stylesheet" href="${staticWebPath}/b5mcdn/wap-pay/css/payNew.css?t=${today}"/>
    </c:if>
    <c:if test="${isBHBBusiness}">
        <link rel="stylesheet" href="${staticWebPath}/bhbcdn/wap-pay/css/payNew.css?t=${today}"/>
    </c:if>

    <style type="text/css">
    	#footer{background:transparent;}
    </style>
</head>
    <c:if test="${fn:contains(ua, 'b5m')}">
    	<style>
        	#footer{ display: none!important;}
        	.wap-header{ display: none!important;}
        </style>    
    </c:if>
<body>
<div id="page">
<c:set var="finalAmount" value="${paymentOrder.amount}" />
 <header class="wap-header">
    <div class="wap-topbar">
        <div class="h-left"><a title="返回" href="javascript:history.go(-1);">返回</a></div>
        <div class="h-center">
            <div class="h-center goods-detail-words">支付页面</div>
        </div>
        <div class="search_nav_icon">
        </div>
    </div>
    <nav class="nav_wrap_new">
        <i class="top_nav_icon"></i>
        <ul class="top_nav_ul clear-fix">            
        	<li class="top_nav_li top_nav_home fl">
            	<a href="${mobilePath}"><i class="nav_home_icon"></i><span class="top_nav_li_span">主页</span></a>
        	</li>           
            <li class="top_nav_li top_nav_korea fl">
            	<a href="${koreaPath}/ent"><i class="nav_korea_icon"></i><span class="top_nav_li_span">韩国馆</span></a>
            </li>
            <li class="top_nav_li top_nav_cart fl">
            	<a href="${mobileCartPath}"><i class="nav_cart_icon"></i><span class="top_nav_li_span">购物车</span></a>
			</li>
            <li class="top_nav_li top_nav_order fl">
             	<a href="${mobilePath}/user/orderList"><i class="nav_order_icon"></i><span class="top_nav_li_span">订单</span></a>
           	</li>           
            <li class="top_nav_li top_nav_user fl">
             	<a href="${mobilePath}/user/index"><i class="nav_user_icon"></i><span class="top_nav_li_span">我的</span></a>
            </li>          
        </ul>
    </nav>
</header>

    <c:if test="${!isB5CBusiness}">
        <div class="time-tip">
            <p><span class="icon"></span>请在2小时内完成支付，否则会自动取消订单</p>
        </div>
    </c:if>

    <section>
    	<div class="orders-container">
    	<form method="post" id="postForm" action="${rootPath}/wap/pay.htm" >
            <input type="hidden"  name="userId" id="userId" value="${paymentOrder.userId }"/>
            <input type="hidden"  name="orderId" value="${paymentOrder.orderId }"/>
            <input type="hidden"  name="sign" value="${paymentOrder.sign}"/>
            <input type="hidden"  name="amount" value="${paymentOrder.amount}"/>
            <input type="hidden"  name="amountDetail" value="${paymentOrder.amountDetail}"/>
            <input type="hidden"  name="totalAmount" value="${paymentOrder.amount}"/>
            <input type="hidden"  name="subject" value="${paymentOrder.subject }"/>
            <input type="hidden"  id="sourceHiddenId" name="source" value="${paymentOrder.source}"/>
            <input type="hidden"  name="freeFeeType" value="${paymentOrder.freeFeeType }"/>
            <input type="hidden"  name="inviterId" value="${paymentOrder.inviterId }"/> 
            <input type="hidden"  name="code" id="code" value="${paymentOrder.code}"/> 
            <input type="hidden"  name="thirdPayType" value="<c:if test="${paymentOrder.thirdPayType > 0}">${paymentOrder.thirdPayType}</c:if><c:if test="${paymentOrder.thirdPayType < 1}">5</c:if>" id="thirdPayType"/>
            <input type="hidden"  name="trafficSource" id="trafficSource" value=""/> 
           	<input type="hidden"  name="usebz" value="<c:if test="${(null != paymentOrder.bzAmount) && (paymentOrder.bzAmount > 0)}">1</c:if><c:if test="${(null == paymentOrder.bzAmount) || (paymentOrder.bzAmount < 1)}">0</c:if>" />
           	<input type="hidden"  name="wxresultcode" id="wxresultcode" value="${paymentOrder.code}"/>
            <input type="hidden"  name="isSuperBzPayPWD"  id="isSuperBzPayPWD" value="${isSuperBzPayPWD}"/>
            <input type="hidden"  name="is_app" data-isnew="" data-isold="" id="is_app" value="${isAPP}" />
            <input type="hidden"  name="appVersion"  id="appVersion" value="${appVersion}"/>
            <input type="hidden"  name="uaType"  id="uaType" value="${uaType}"/>
            <input type="hidden"  name="crossPay"  id="crossPay" value="${paymentOrder.crossPay}"/>
            <input type="hidden"  name="addressId"  id="addressId" value="${paymentOrder.addressId}"/>
            <input type="hidden"  name="isDisabledEnabledBz"  id="isDisabledEnabledBz" value="${isDisabledEnabledBz}"/>
            <input type="hidden"  name="bzMaxValue"  id="bzMaxValue" value="${bzMaxValue}"/>
            <input type="hidden"  name="bzUsable1"  id="bzUsable1" value="${paymentOrder.bzUsable}"/>
            <input type="hidden"  name="defaultSelectedBz"  id="isDisabledEnabledBz" value="${defaultSelectedBz}"/>
            <input type="hidden"  name="isLessBalance"  id="isLessBalance" value="${isLessBalance}"/>

            <%-- 超级帮钻 Start--%>
            <c:if test="${isEnabledBz && isAPP}">
            	<c:if test="${paymentOrder.bzAmount == null || paymentOrder.bzAmount < 0}">
                    <div class="bangzuan-cont">
                        <div class="bz-cont cf <c:if test="${!isAPP}">checked</c:if> ">
                            <span class="go-pay">去支付<i class="icon-checkbox1"></i></span>
                            <span class="bz-txt"><i class="bz-icon"></i>超级帮钻支付</span>
                        </div>
                        <div class="bz-input">
                            <input type="tel" class="bz-amount" name="bzAmount" value="${bzMaxValue}">
                        </div>
                        <div class="max-tip"></div>
                        <div class="bz-dknum cf">
                            <p class="bzdh">100<span class="bz-icon"></span>=1元</p>
                            <p class="befs"  data-max="${paymentOrder.bzBalance}">余额：<span class="bz-icon"></span>${paymentOrder.bzBalance}</p>
                        </div>
                        <div class="check-bz-tip">${bzWapTipInfo}</div>
                    </div>
            	</c:if>
            </c:if>
           	<c:if test="${paymentOrder.bzAmount != null && paymentOrder.bzAmount > 0}">
                <div class="bangzuan-cont <c:if test="${paymentOrder.bzAmount > 0}">frozen</c:if> ">
                    <div class="bz-cont cf checked">
                        <span class="bz-txt"><i class="bz-icon"></i>超级帮钻支付</span>
                    </div>
                    <div class="bz-input">
                        <span>使用超级帮钻额:  ${paymentOrder.bzAmount}</span>
                        <span class="dx-tip">抵现&yen;<em><c:if test="${paymentOrder.bzAmount > 0}">
                            <fmt:formatNumber value="${paymentOrder.bzAmount/100}" pattern="#0.00#"/>
                        </c:if><c:if test="${paymentOrder.bzAmount <= 0}">0.00</c:if></em></span>
                    </div>
                    <div class="bz-dknum cf">
                        <p class="bzdh">100<span class="bz-icon"></span>=1元</p>
                        <p class="befs"  data-max="${paymentOrder.bzBalance}">余额：<span class="bz-icon"></span>${paymentOrder.bzBalance}</p>
                    </div>
                    <input type="hidden" class="bz-amount" name="bzAmount" value="<c:if test="${paymentOrder.bzAmount > 0}">${paymentOrder.bzAmount}</c:if>" />
                    <div class="check-bz-tip">${bzWapTipInfo}</div>
                </div>
	        </c:if>
            <%-- 超级帮钻 End--%>    
            
            <%-- 第三方支付方式 Start--%>
            <div class="pay-content">
                <h3 class="tit">现金支付${environment}</h3>
                <c:if test="${wapPlatform != null && fn:length(wapPlatform) > 0}" >
                    <ul class="pay-items">
                        <c:forEach items="${wapPlatform}" var="item1" varStatus="nl1">
                            <li id="${item1.blockId}" class="<c:if test="${item1.defaultChecked}">checked</c:if> ${item1.blockClass}" >
                                <label>
                                    <span class="list-l"><em class="${item1.formClass}"></em>${item1.title}</span>
                                <span class="list-r"> <i class="icon-checkbox1"></i>
                                    <input type="radio" value="${item1.formValue}" name="defaultBank" class="pay-radio" data-id="${item1.dataId}" id="${item1.formId}" <c:if test="${item1.defaultChecked}">checked="checked"</c:if> data-thirdPayType="${item1.thirdPayType}">
                                </span>
                                </label>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>
            </div>
            <%-- 第三方支付方式 end--%>
            <!--新添加信息 e-->
            <div class="orders-list">
                <span class="list-l">订单总价：</span>
                <span class="list-r"><fmt:formatNumber value="${paymentOrder.amount}" pattern="#0.00#"/></span>
            </div>
            <c:if test="${(isEnabledBz && isAPP) || (paymentOrder.bzAmount != null && paymentOrder.bzAmount > 0)}">
            <div class="orders-list">
                <span class="list-l">超级帮钻支付：</span>
                <span class="list-r dx-numb"><fmt:formatNumber value="${paymentOrder.bzAmount/100}" pattern="#0.00#"/></span>
            </div>
            </c:if>
            <div class="orders-list">
            	<span class="list-l">现金支付：</span>
            	<span class="list-r red">￥<b id="pay-amount" data-price="${finalAmount-paymentOrder.bzAmount/100}" ><fmt:formatNumber value="${finalAmount-paymentOrder.bzAmount/100}" pattern="#0.00#"/></b></span>
            </div> 
        	<div class="bottom-btn"><input type="button" class="btn-red" value="立即支付" id="submitBotton" name="submitBotton"><p class="txt">实付款:￥<span id="pay-actual"></span></p></div>
        </form>	
        </div>
    </section>
</div>

<c:if test="${!isBHBBusiness}">
    <script src="${rootPath}/resources/js/main.js?t=${today}" type="text/javascript"></script>
</c:if>
<c:if test="${isBHBBusiness}">
    <script src="${rootPath}/resources/js/bhb_main.js?t=${today}" type="text/javascript"></script>
</c:if>
<%--<script src="${cdnTJPath}/tongji/stat3.min.js?t=${today}" type="text/javascript"></script>--%>
</body>
</html>