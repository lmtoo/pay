<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
	$(document).ready(function(){
		$("#main-nav").find('a').each(function(i ,e){
			var a = $(this).attr("href");
			if((window.location.pathname+"/").indexOf(a+"/")!=-1){
				$(this).addClass("active").closest("ul").addClass('in').attr({"aria-expanded":"true","style":"height:auto"}).prev("a").removeClass("collapsed").attr("aria-expanded","true");
			}		
		});
	});
</script>
<ul id="main-nav" class="nav nav-tabs nav-stacked">
    <li class="active">
        <a href="${ctx}/console/index.htm" style="cursor:pointer">
            <i class="glyphicon glyphicon-th-large"></i>
            	控制台首页
        </a>
    </li>
    <li>
        <a href="#systemSetting" class="nav-header collapsed" data-toggle="collapse">
            <i class="glyphicon glyphicon-cog"></i>
            	配置项
            <span class="pull-right glyphicon glyphicon-chevron-down"></span>
        </a>
        <ul id="systemSetting" class="nav nav-list collapse secondmenu" style="height: 0px;">
            <li><a href="/server/managePage.htm">配置信息</a></li>
            <li><a href="/console/managePaymentSource.htm">请求来源信息</a></li>
            <li><a href="/server/versionPage.htm">前端版本信息</a></li>
            <li><a href="/console/query.htm">数据查询</a></li>
        </ul>
    </li>
    <li>
        <a href="#systemStatus" class="nav-header collapsed" data-toggle="collapse">
            <i class="glyphicon glyphicon-cog"></i>
            	运行状态
            <span class="pull-right glyphicon glyphicon-chevron-down"></span>
        </a>
        <ul id="systemStatus" class="nav nav-list collapse secondmenu" style="height: 0px;">
            <li><a href="/console/paymentOrder/query.htm">交易流水</a></li>
            <li><a href="/console/callback/query.htm">第三方支付回调</a></li>
            <li><a href="/console/notify/query.htm">支付结果通知</a></li>
            <li><a href="/console/baoguan/query.htm">报关记录</a></li>
        </ul>
    </li>
    <li>
        <a href="#serviceControl" class="nav-header collapsed" data-toggle="collapse">
            <i class="glyphicon glyphicon-cog"></i>
            	服务管理
            <span class="pull-right glyphicon glyphicon-chevron-down"></span>
        </a>
        <ul id="serviceControl" class="nav nav-list collapse secondmenu" style="height: 0px;">
            <li><a href="/console/switch/query.htm">支付渠道开关控制</a></li>
        </ul>
    </li>
</ul>
