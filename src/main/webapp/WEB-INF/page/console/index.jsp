<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div style="padding-left:10px;"><h1 style="color:red">${ip} , 支付系统事关重大，请谨慎操作</h1></div>
<div class="panel" style="float:left; margin:10px; border:1px solid #ccc;width:100%;">
	<div class="title" style="background-color:#ccc; height:40px; font-weight:bold;padding:10px;">
		自&nbsp;&nbsp;<font color="green">${startTime }</font>&nbsp;&nbsp;启动以来交易数据统计
	</div>
	<div class="body" style="padding:10px;line-height:30px;">
		<div class="group_item">
			<div class="left">成交数量:</div>
			<div class="num_content">${totalOrderCount }&nbsp;&nbsp;笔</div>
		</div>
		<div class="group_item">
			<div class="left">成交金额:</div>
			<div class="num_content">${totalOrderAmount }&nbsp;&nbsp;元</div>
		</div>
		<div class="group_item">
			<div class="left">应收人民币:</div>
			<div class="num_content">${totalMoneyAmount }&nbsp;&nbsp;元</div>
		</div>
		<div class="group_item">
			<div class="left">超级帮钻抵扣:</div>
			<div class="num_content">${totalBzAmount }&nbsp;&nbsp;个</div>
		</div>
	</div>
</div>
<div style="clear：both"></div>
<c:forEach items="${systemInfos}" var="item" varStatus="n">
	<div class="panel" style="width:660px; float:left; margin:10px; border:1px solid #ccc;">
		<div class="title" style="background-color:#ccc; height:40px; font-weight:bold;padding:10px;">${item.ip }</div>
		<div class="body" style="padding:10px;line-height:30px;">
			<div class="group_item">
				<div class="left">JVM</div>
				<div class="right">${item.jvm }</div>
			</div>
			<div class="group_item">
				<div class="left">JDK</div>
				<div class="right">${item.jdk }</div>
			</div>
			<div class="group_item">
				<div class="left">Encoding</div>
				<div class="right">${item.encoding }</div>
			</div>
			<div class="group_item">
				<div class="left">操作系统</div>
				<div class="right">${item.osName }</div>
			</div>
			<div class="group_item">
				<div class="left">用户目录</div>
				<div class="right">${item.userHome }</div>
			</div>
			<div class="group_item">
				<div class="left">临时目录</div>
				<div class="right">${item.tempDir }</div>
			</div>
			<div class="group_item">
				<div class="left">已用内存</div>
				<div class="right">${item.usedMemory }M</div>
			</div>
			<div class="group_item">
				<div class="left">待使用内存</div>
				<div class="right">${item.freeMemoery }M</div>
			</div>
			<div class="group_item">
				<div class="left">已开辟内存</div>
				<div class="right">${item.totalMemory }M</div>
			</div>
			<div class="group_item">
				<div class="left">剩余可用内存</div>
				<div class="right">${item.useableMemory }M</div>
			</div>
		</div>
	</div>
</c:forEach>
<style>
	.num_content{
		width:250px;float:left;color:green;
	}
	@media screen and (max-width:600px){
		.num_content{
			width:100px;
		}
		.group_item .left{
			width:100px;
			padding-right:5px;
		}
	}
	.group_item{
		display:inline;
	}
	.group_item .left{
		width:130px;float:left;font-weight:bold;padding-right:20px;
	}
	.group_item .right{
		width:500px;float:left;color:green;
	}
</style>