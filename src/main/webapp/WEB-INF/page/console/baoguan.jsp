<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<style>
	.form-inline{
		border:1px solid #ccc;
		padding-bottom:10px;
		margin:5px 0;
	}
	.form-group{
		margin:10px;
	}
</style>
<div class="form-inline" role="form">
	<div class="form-group">
		<span>订单号：</span>
		<input name="orderId" id="search_orderId" class="form-control" type="text" placeholder="订单号" value="${orderId}">
	</div>
	<div class="form-group">
		<span>渠道：</span>
		<select name="channel" class="form-control" id="search_channel">
			<option value="" ${empty payChannelList ? "selected" : ""}>全部</option>
			<c:forEach items="${payChannelList}" var="payChannel">
				<option value="${payChannel.value}" ${channel == payChannel.value ? "selected" : ""}>${payChannel.desc}</option>
			</c:forEach>
		</select>
	</div>
	<div class="form-group">
		<span>状态：</span>
		<select name="status" class="form-control" id="search_status">
			<option value="" ${empty status ? "selected" : ""}>全部</option>
			<option value="0" ${status == 0 ? "selected" : ""}>报关失败</option>
			<option value="1" ${status == 1 ? "selected" : ""}>报关成功</option>
			<option value="2" ${status == 2 ? "selected" : ""}>暂未报关</option>
		</select>
	</div>
	<div class="form-group">
		<button id="search" type="submit" class="btn">查询</button>
	</div>
</div>

<table id="contentTable" class="table table-striped table-condensed table-bordered" width="100%" style="table-layout:fixed;">
    <tr>
        <th style="width:10%;">订单号</th>
        <th style="width:10%;">渠道</th>
		<th style="width:10%;">报关配置key</th>
        <th style="width:5%;">状态</th>
        <th style="width:45%;">报关返回结果</th>
        <th style="width:10%;">创建时间</th>
        <th style="width:10%;">更新时间</th>
        <th style="width:5%;">操作</th>
    </tr>

        <c:forEach items="${result.list}" var="item" varStatus="n">
            <tr>
                <td style="word-wrap:break-word;font-size:13px;">
                	<a href="${ctx }/console/paymentOrder/query.htm?orderId=${item.orderId}">${item.orderId}</a>
                </td>
                <td style="word-wrap:break-word;font-size:13px;">${item.channel.desc}</td>
				<td style="word-wrap:break-word;font-size:13px;">${item.bgConfigKey}</td>
				<td style="word-wrap:break-word;font-size:13px;">${item.status.message}</td>
                <td style="word-wrap:break-word;font-size:13px;"><c:out value="${item.result}" escapeXml="true"/></td>
                <td style="word-wrap:break-word;font-size:13px;">${item.createTime}</td>
                <td style="word-wrap:break-word;font-size:13px;">${item.updateTime}</td>
                <td style="word-wrap:break-word;font-size:13px;">
                	<c:if test="${item.status != 'SUCCEED'}">
                		<a class="retry" href="retry.htm?orderId=${item.orderId}" target="_blank">重试</a>
                	</c:if>
                	&nbsp;
                </td>
            </tr>
        </c:forEach>
</table>
<div id="pagination" class="pagination"></div>
<script>
   	$(function(){
   		var currentPage = ${result.page.pageNum - 1};
   		$("#pagination").pagination(${result.page.total}, {
			current_page: currentPage,
			items_per_page: 30,
			callback : function(page, pagebar){
				if(page != currentPage){
					window.location.href = 'query.htm?pageNum=' + (page + 1);
				}
				return false;
			}
		});
		$(".retry").click(function(){
			var url = $(this).attr("href");
			$.getJSON(url, function(data){
				layer.msg(data.code + ":" + data.msg,function(){
					window.location.reload();
				});
			});
			return false;
		});

		$(".btn").click(function(){
			var orderId = $("#search_orderId")[0].value;
			var channel = $("#search_channel")[0].value;
			var status = $("#search_status option:selected").val();
			window.location.href = "/console/baoguan/query.htm?orderId="+orderId+"&channel="+channel+"&status="+status;
		});
   	});
</script>