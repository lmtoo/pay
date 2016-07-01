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
	<meta name="title" content="支付中心 - 帮5买 - 全网.正品.低价！" />
    <title>支付中心&nbsp;-&nbsp;我的B5M&nbsp;-&nbsp;帮5买&nbsp;-&nbsp;全网.正品.低价！</title>
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="apple-mobile-web-app-status-bar-style" content="black" />
	<meta name="format-detection" content="telephone=no" />
	<meta name="author" content="" />
	<meta name="data-mps" content="DO1024">
	<meta name="revisit-after" content="1 days" />
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<link rel="icon" href="${rootPath}/favicon.ico" type="image/x-icon" />
	<link rel="shortcut icon" href="${rootPath}/favicon.ico" type="image/x-icon" />
	<link rel="stylesheet" type="text/css" href="${staticWebPath}/common/css/common_min.css?t=${today}" />
	<link rel="stylesheet" type="text/css" href='${staticWebPath}/pay/css/pay.css?t=${today}' />
</head>
<body data-mps="PA2401">
<input type="hidden"  id="rootPath" value="${rootPath}"/>
<c:set var="finalAmount" value="${paymentOrder.amount}" />

<div data-mps="10001" class="top-hdbanner"></div>

<%-- 头部 Start --%>
<% request.setAttribute("headModel", PayController.getPageModuledule()); %>
${headModel.top_bar }
<%-- 头部 End --%>

<div class="wp center cfx">
	<div class="center-pay">
		<div class="center-pay-info mt20 cfx">
			<h1>订单提交成功，请您尽快付款！</h1>
			<c:set value="${ fn:split(paymentOrder.orderId, ',') }" var="orders" />
			<c:if test="${fn:length(orders) ==1}">
				<p style="margin-left: 40px;">
					<label>支付号：</label>
					<span>${paymentOrder.orderId}<c:if test="${paymentOrder.mobileNum!=null && paymentOrder.mobileNum!=''}"> <b style="margin:0 10px 0 40px;font-weight:bold;color:#e57b00;font-family:Tahoma;font-size:16px;">${paymentOrder.mobileNum}</b>自动充值 快速到账${paymentOrder.chargeValue}</c:if></span>
				</p>
			</c:if>
			<c:if test="${fn:length(orders) >1}">
				<p style="margin-left: 40px;">
					<label>合并：</label>
					<span>${fn:length(orders)}笔订单<c:if test="${paymentOrder.mobileNum!=null && paymentOrder.mobileNum!=''}"> <b style="margin:0 10px 0 40px;font-weight:bold;color:#e57b00;font-family:Tahoma;font-size:16px;">${paymentOrder.mobileNum}</b>自动充值 快速到账${paymentOrder.chargeValue}</c:if></span>
				</p>
			</c:if>
			<p>|</p>
			<p style="margin-top: 1px;">
				<label>订单总金额：</label>
				<c:if test="${!isBZModeDisplay}">
						<span class="amount"><b>￥</b><fmt:formatNumber value="${finalAmount}" pattern="#0.00#"/></span>
				</c:if>
				<c:if test="${isBZModeDisplay}">
					<span class="amount"><fmt:formatNumber value="${finalAmount*100}" pattern="#"/></span> 超级帮钻
				</c:if>
			</p>
		</div>
		<div class="center-pay-type">只差一步，请尽快支付 ${environment} <span> 请在2小时内完成支付，否则会自动取消订单</span> </div>
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
				
				<%-- 超级帮钻 Start--%>
				<c:if test="${isEnabledBz}">
					<%-- 首次使用帮钻 --%>
					<c:if test="${paymentOrder.bzAmount == null }">
					<div class="bzdx">
						<h3>帮5支付</h3>
						<div data-mps="MO11000">
							<label class="labed-checked">
								<span class="use-bz-check" data-mps="PO1"></span>
								<span class="icon-bz" data-mps="PO1"></span>
								<span class="bz-text" data-mps="PO1">超级帮钻</span>
							</label>
							<div class="iline promp-line">
								<div class="promp-ctn">
								您有<b>${paymentOrder.bzBalance}</b>个超级帮钻<div class="ques-ctn"><i class="fa">&#xf059;</i>
								<div class="tip-bz-describe">
									<div>${bzTipInfo}<br/>
										<a href="${b5mPath}/module_html/default-1.html" class="link-bz-info" target="_blank" data-mps="PO2">详情介绍</a>
									</div>
								</div>
								</div>
								<c:if test="${!isBZModeDisplay}">
									可抵现
									<dfn>&yen;</dfn><b><c:if test="${paymentOrder.bzBalance > 0}"><fmt:formatNumber value="${paymentOrder.bzBalance/100}" pattern="#0.00#"/></c:if><c:if test="${paymentOrder.bzBalance <= 0}">0.00</c:if></b>
								</c:if>
								<c:if test="${isBZModeDisplay}">
									<c:if test="${!isLessBalance}">
										本次使用 <b><fmt:formatNumber value="${finalAmount*100}" pattern="#"/></b> 超级帮钻
									</c:if>
									<c:if test="${isLessBalance}">
										<b> 余额不足 </b>
									</c:if>
								</c:if>
								</div>
									<a class="bz-recharge" href="${ucenterPath}/bps/index.htm?form=pay" target="_blank" data-mps="PO3"><c:if test="${(isBZModeDisplay) && isLessBalance}">去</c:if>充值</a>
							</div>
							<div class="iline input-line hide">
								<div class="promp-ctn">使用
									<input type="text" name="bzAmount" data-max="${bzMaxValue}" value="${bzMaxValue}" class="bz-amount" autocomplete="off" />
									<c:if test="${!isBZModeDisplay}">
										<a href="javascript:;" class="use-max-btn hide">使用全部</a>个超级帮钻，抵现
										<dfn>&yen;</dfn><b class="dx-tip"></b>
										<div class="max-tip">本次最多可用<b>${bzMaxValue}</b>超级帮钻，您有<b>${paymentOrder.bzBalance}</b>个超级帮钻</div>
									</c:if>
									<c:if test="${isBZModeDisplay}">
										个超级帮钻
									</c:if>
								</div>
							</div>			
							<div class="iline done-line hide">
								<div class="promp-ctn">
									本次使用<b class="dx-bz"></b>个超级帮钻
									<div class="ques-ctn"><i class="fa">&#xf059;</i>
										<div class="tip-bz-describe">
											<div>${bzTipInfo}<br/>
												<a href="${b5mPath}/module_html/default-1.html" class="link-bz-info">详情介绍</a>
											</div>
										</div>
									</div>
									<c:if test="${!isBZModeDisplay}">
										可抵现<dfn>&yen;</dfn><b class="dx-yen"></b><h4>取消订单后解冻</h4>
									</c:if>
							</div>
						</div>			
					</div>
					</div>
					</c:if>	
					<%-- 使用帮钻 锁定 后 --%>
					<c:if test="${paymentOrder.bzAmount !=null && paymentOrder.bzAmount >= 0}">
						<div class="bzdx <c:if test="${paymentOrder.bzAmount >= 0}">frozen</c:if>">
						<h3>帮5支付</h3>
						<div>
							<label class="labed-checked label-checked">
								<span class="use-bz-check checked" ></span>
								<span class="icon-bz"></span>
								<span class="bz-text">超级帮钻</span>
							</label>
							<div class="iline done-line">
								<input type="hidden" name="bzAmount" value="${paymentOrder.bzAmount}"/>
								<div class="promp-ctn">
									本次使用<b class="dx-bz">${paymentOrder.bzAmount}</b>个超级帮钻
									<div class="ques-ctn"><i class="fa">&#xf059;</i>
										<div class="tip-bz-describe">
											<div>${bzTipInfo}<br/>
												<a href="${b5mPath}/module_html/default-1.html" class="link-bz-info">详情介绍</a>
											</div>
										</div>
									</div>
									<c:if test="${!isBZModeDisplay}">
										可抵现<dfn>&yen;</dfn><b class="dx-yen"><c:if test="${paymentOrder.bzAmount > 0}"><fmt:formatNumber value="${paymentOrder.bzAmount/100}" pattern="#0.00#"/></c:if><c:if test="${paymentOrder.bzAmount <= 0}">0.00</c:if></b>
										<h4>取消订单后解冻</h4>
									</c:if>
								</div>
							</div>
						</div>
					</div>
					</c:if>		
				</c:if>
				<%-- 超级帮钻 End--%>
								
				<div class="center-pay-list" data-mps="${paymentDivDataMps}">

                    <c:if test="${zfPlatform != null && fn:length(zfPlatform) > 0}" >
                        <h2 class="cfx">
                            <c:if test="${zfPlatform != null && fn:length(zfPlatform) > bankMaxLength}" >
                                <a class="show-all" href="javascript:;">展开更多<i></i></a>
                            </c:if>
                            支付平台
                        </h2>
                        <ul class="hide-more cfx" data-mps="34002">
                            <c:set var="zf1" value="0"/>
                            <c:forEach items="${typeListMap['platform']}" var="userItemKey" varStatus="nl">
                                <c:forEach items="${zfPlatform}" var="item1" varStatus="nl1">
                                    <c:if test="${userItemKey == item1.keyName}">
                                        <c:set var="zf1" value="${zf1+1}"/>
                                        <li <c:if test="${zf1 > bankMaxLength}"> class="more" </c:if> >
                                            <input id="${item1.formId}" data-id="${item1.dataId}" type="radio" class="pay-radio" name="defaultBank" value="${item1.formValue}" data-mps="${item1.dataMps}" data-thirdpaytype="${item1.thirdPayType}"/>
                                            <label for="${item1.formId}"><i class="pay-radio-custom"></i><img src="${staticWebPath}${item1.channelImg}?t=${today}" alt="${item1.title}" title="${item1.title}" data-mps="${item1.dataMps}"/></label>
                                        </li>
                                    </c:if>
                                </c:forEach>
                            </c:forEach>
                        </ul>
                    </c:if>

                    <c:if test="${payOnlineChannel != null && payOnlineChannel != '' }" >
                        <c:if test="${payOnlinePlatform != null && fn:length(payOnlinePlatform) > 0}" >
                            <h2 class="cfx">
                                <c:if test="${payOnlinePlatform != null && fn:length(payOnlinePlatform) > bankMaxLength}" >
                                    <a class="show-all" href="javascript:;">展开更多<i></i></a>
                                </c:if>
                                网银支付
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

                    <c:if test="${fastpayChannel != null && fastpayChannel != '' }" >
                        <c:if test="${fastPayPlatform != null && fn:length(fastPayPlatform) > 0}" >
                            <h2 class="cfx">
                                <c:if test="${fastPayPlatform != null && fn:length(fastPayPlatform) > bankMaxLength}" >
                                    <a class="show-all" href="javascript:;">展开更多<i></i></a>
                                </c:if>
                                快捷支付<span>（包含银行信用卡）</span>
                                <c:if test="${fastPayTips != null && fastPayTips != ''}" ><span class="rec-way" >${fastPayTips}</span></c:if>
                                <div class="preview">
                                    <a class="show-info" href="javascript:;">预览需要填写的信息  ></a>
                                    <div class="preview-info">
                                        <span class="tri"></span>
                                        <div class="pre-header cfx">
                                            <a class="pre-close" href="javascript:;">×</a>
                                            <a class="pre-tab cur" href="javascript:;">储蓄卡</a>
                                            <a class="pre-tab" href="javascript:;">信用卡</a>
                                        </div>
                                        <img id="pre-cxk" class="card-img cur" src="${staticWebPath}/pay/img/pay-fast-cxk.jpg?t=20141281049" />
                                        <img id="pre-xyk" class="card-img" src="${staticWebPath}/pay/img/pay-fast-xyk.jpg?t=20141281049" />
                                    </div>
                                </div>
                            </h2>
                            <ul class="hide-more cfx" data-mps="34004">
                                <c:set var="f1" value="0"/>
                                <c:forEach items="${typeListMap['fastpay']}" var="userItemKey" varStatus="nl">
                                    <c:forEach items="${fastPayPlatform}" var="item12" varStatus="nl12">
                                        <c:if test="${(userItemKey == item12.keyName) }">
                                            <c:set var="f1" value="${f1+1}"/>
                                            <li <c:if test="${f1 > bankMaxLength}"> class="more" </c:if> >
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
			<div class="center-pay-btn mt20 cfx" data-mps="MO14000" >
				<input type="button" id="J_pay_next" data-attr="1019" class="pay-next fr" value="立即支付" data-mps="paynow" />
				<c:if test="${paymentOrder.crossPay != 4}">
					<strong class="pay-amount fr">待支付金额：<span>￥</span><span class="amount"><fmt:formatNumber value="${finalAmount-paymentOrder.bzAmount/100}" pattern="#0.00#"/></span></strong>
				</c:if>
			</div>
			<div id="pay-window" class="b5m-pay" style="display: none;">
				<div class="pay-tip pay-suc">
					<span>如已经支付成功，请点击：</span>
					<a class="done" href="${returnUrl}">已完成支付</a>
				</div>
				<div class="pay-tip pay-fai">
					<span>如支付遇到问题，你可以：</span>
					<a class="choose" href="javascript:;">重新支付</a>
				</div>
				<div class="pay-footer">
					<span class="net-tip">若因网络不佳，打开支付页面慢，请耐心等候</span>
					<span>客服电话：</span>
					<span class="phone-num">400-085-0505</span>
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
				<div class="pay-tip">
					<a class="done" href="javascript:;">确定</a>
				</div>
			</div>

            <div class="bz-pwd-ctn" style="display: none;">
                <div class="set-1" style="display: none;">
                    <h3>为了您的账号安全，使用超级帮钻需要设置<span>支付密码</span></h3>
                    <div class="step-ctn">
                        <i class="xbb"></i>
                        <ul>
                            <li>
                                <label>密码设置</label>
                                <input id="bz-set-pwd" class="ipt-big" type="password" maxlength="15" />
                                <i class="icon-suc" style="display: none;"></i>
                                <em class="err">请输入6-15位数字、字母或符号两种以上组合</em>
                            </li>
                            <li>
                                <label>重新输入</label>
                                <input id="bz-re-pwd" class="ipt-big" type="password" maxlength="15" />
                                <i class="icon-suc" style="display: none;"></i>
                                <em class="err">两次输入的密码不一致</em>
                            </li>
                            <li class="code">
                                <c:if test="${userBindMobile == ''}" >
                                    <label class="lb-mobile">您尚未绑定手机号码，请先绑定手机号码<a class="btn-unbind" href="${ucenterPath}/nuser/userinfo.htm#2">去绑定</a></label>
                                </c:if>
                                <c:if test="${userBindMobile != ''}" >
                                    <label class="lb-mobile">您绑定的手机号码为:<span class="mobile-num">{}</span><a class="btn-unbind" href="${ucenterPath}/nuser/userinfo.htm#2">修改手机号码</a></label>
                                </c:if>
                                <input id="bz-mb-code" class="ipt-big ipt-code" type="text" maxlength="6" />
                                <a class="btn-get-code" href="javascript:;">获取验证码</a>
                                <em class="err"></em>
                            </li>
                            <li>
                                <a class="confirm btn-sub" href="javascript:;">提交</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="set-2" style="display: none;">
                    <div class="step-ctn">
                        <i class="xbb"></i>
                        <ul>
                            <li class="suc-tip">
                                <i class="tri"></i>
                                恭喜你，超级帮钻支付密码<span>设置成功</span>
                            </li>
                            <li>
                                <a class="confirm" href="javascript:;">确定</a>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="ipt-pwd" style="display: none;">
                    <h3>使用超级帮钻需要<span>支付密码</span></h3>
                    <div class="step-ctn">
                        <i class="xbb"></i>
                        <ul>
                            <li class="pay-pwd">
                                <label>支付密码</label>
                                <input id="bz-pay-pwd" class="ipt-big" type="password" maxlength="15" /> <a href="${ucenterPath}/nuser/userinfo.htm#2">忘记密码?</a>
                                <em class="err">支付密码不正确，请查证后再试</em>
                            </li>
                            <li>
                                <a class="confirm btn-sub" href="javascript:;">确定</a>
                            </li>
                        </ul>
                    </div>

                </div>
            </div>

		</form>
	</div>			
</div>
${headModel.foot_top }
${headModel.foot_bottom }
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
