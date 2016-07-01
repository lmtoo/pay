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
            <option value="" ${empty notifyChannelList ? "selected" : ""}>全部</option>
            <c:forEach items="${notifyChannelList}" var="notifyChannel">

                <c:if test="${channel == '无'}">
                    <option ${"" == notifyChannel.desc ? "selected" : ""}>
                        <c:choose>
                            <c:when test="${notifyChannel.desc == ''}">
                                无
                            </c:when>
                            <c:otherwise>
                                ${notifyChannel.desc}
                            </c:otherwise>
                        </c:choose>
                    </option>
                </c:if>

                <c:if test="${channel != '无'}">
                    <option ${channel == notifyChannel.desc ? "selected" : ""}>
                        <c:choose>
                            <c:when test="${notifyChannel.desc == ''}">
                                无
                            </c:when>
                            <c:otherwise>
                                ${notifyChannel.desc}
                            </c:otherwise>
                        </c:choose>
                    </option>
                </c:if>
            </c:forEach>
        </select>
    </div>
    <div class="form-group">
        <span>状态：</span>
        <select name="status" class="form-control" id="search_status">
            <option value="" ${empty notifyStatusList ? "selected" : ""}>全部</option>
            <c:forEach items="${notifyStatusList}" var="notifyStatus">
                <option value="${notifyStatus}" ${status == notifyStatus ? "selected" : ""}>${notifyStatus.desc}</option>
            </c:forEach>
        </select>
    </div>
    <div class="form-group">
        <span>下单时间：</span>
        <div class="input-group date" id="startTimePicker">
            <input type='text' class="form-control date" name="search_startTime" id="start_time" value="${startTime}" />
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
        </div>
    </div>
    <div class="form-group">
        <span>至</span>
        <div class="input-group date" id="endTimePicker">
            <input type='text' class="form-control date" name="search_endTime" id="end_time" value="${endTime}" />
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
        </div>
    </div>
    <div class="form-group">
        <button id="search" type="submit" class="btn">查询</button>
    </div>
</div>
<table id="contentTable" class="table table-striped table-condensed table-bordered" width="100%" style="table-layout:fixed;">
    <tr>
        <th style="width:10%;">订单号</th>
        <th style="width:5%;">类型</th>
        <th style="width:5%;">渠道</th>
        <th style="width:15%;">目的地</th>
        <th style="width:25%;">参数</th>
        <th style="width:5%;">状态</th>
        <th style="width:15%;">备注</th>
        <th style="width:10%;">创建时间</th>
        <th style="width:10%;">更新时间</th>
        <th style="width:5%;">操作</th>
    </tr>

        <c:forEach items="${result.list}" var="item" varStatus="n">
            <tr>
                <td style="word-wrap:break-word;font-size:13px;">
                	<a href="${ctx }/console/paymentOrder/query.htm?orderId=${item.id.orderId}">${item.id.orderId}</a>
                </td>
                <td style="word-wrap:break-word;font-size:13px;">${item.id.type.desc}</td>
                <td style="word-wrap:break-word;font-size:13px;">${item.channel.desc}</td>
                <td style="word-wrap:break-word;font-size:13px;">${item.target}</td>
                <td style="word-wrap:break-word;font-size:13px;">${item.data}</td>
                <td style="word-wrap:break-word;font-size:13px;">${item.status.desc}</td>
                <td style="word-wrap:break-word;font-size:13px;">${item.memo}</td>
                <td style="word-wrap:break-word;font-size:13px;">${item.createTime}</td>
                <td style="word-wrap:break-word;font-size:13px;">${item.updateTime}</td>
                <td style="word-wrap:break-word;font-size:13px;">
                	<c:if test="${item.status != 'SUCCESS'}">
                		<a class="retry" href="retry.htm?orderId=${item.id.orderId}&type=${item.id.type}" target="_blank">重试</a>
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
                    var params = getParams();
                    window.location.href = 'query.htm?pageNum=' + (page + 1) + '&' + params;
				}
				return false;
			}
		});
		$(".retry").click(function(){
			var url = $(this).attr("href");
			$.getJSON(url, function(data){
				alert(data.code + ":" + data.msg);
				window.location.reload();
			});
			return false;
		});

        $("#startTimePicker").datetimepicker({
            sideBySide: true,
            showClear: true,
            showClose: true,
            format: "YYYY-MM-DD HH:mm:ss"
        });

        $("#endTimePicker").datetimepicker({
            sideBySide: true,
            showClear: true,
            showClose: true,
            format: "YYYY-MM-DD HH:mm:ss"
        });

        $("#startTimePicker").on("dp.change", function (e) {
            $('#endTimePicker').data("DateTimePicker").minDate(e.date);
        });

        $("#endTimePicker").on("dp.change", function (e) {
            $('#startTimePicker').data("DateTimePicker").maxDate(e.date);
        });

        $(".btn").click(function(){
            var params = getParams();
            window.location.href = "/console/notify/query.htm?"+params;
        });
   	});

    // 获得查询参数
    function getParams(){
        var orderId = $("#search_orderId")[0].value;
        var status = $("#search_status option:selected").val();
        var startTime = $("#start_time")[0].value;
        var endTime = $("#end_time")[0].value;
        var channel = $("#search_channel option:selected").val();
        var params = 'orderId='+orderId+'&status='+status+'&channel='+channel+'&startTime='+startTime+'&endTime='+endTime;
        return params;
    }
</script>