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
	.important{
		color:red;
		font-weight:bold;
	}
</style>
<div class="form-inline" role="form">
    <div class="form-group">
        <span>订单号：</span>
        <input name="orderId" id="search_orderId" class="form-control" type="text" placeholder="订单号" value="${orderId}">
    </div>

    <div class="form-group">
        <span>用户ID：</span>
        <input name="userId" id="search_userId" class="form-control" type="text" placeholder="用户ID" value="${userId}">
    </div>

    <div class="form-group">
        <span>第三方支付流水号：</span>
        <input name="thirdTradeNo" id="search_thirdTradeNo" class="form-control" type="text" placeholder="第三方支付流水号" value="${thirdTradeNo}">
    </div>
	
	<div class="form-group">
        <span>第三方支付类型：</span>
        <select name="thirdPayType" class="form-control" id="search_thirdPayType">
            <option value="" ${empty payChannelList ? "selected" : ""}>全部</option>
            <c:forEach items="${payChannelList}" var="payChannel">
                <option value="${payChannel.value}" ${thirdPayType == payChannel.value ? "selected" : ""}>${payChannel.desc}（${payChannel.value}）</option>
            </c:forEach>
        </select>
    </div>
	
	<div class="form-group">
        <span>订单状态：</span>
        <select class="form-control" name="status" id="search_status">
            <option value="" ${empty status ? "selected" : ""}>全部</option>
            <option value="0" ${status == 0 ? "selected" : ""}>暂未支付</option>
            <option value="1" ${status == 1 ? "selected" : ""}>支付成功</option>
            <option value="2" ${status == 2 ? "selected" : ""}>支付失败</option>
            <option value="3" ${status == 3 ? "selected" : ""}>已完成</option>
            <option value="4" ${status == 4 ? "selected" : ""}>已付款</option>
            <option value="7" ${status == 7 ? "selected" : ""}>支付错误</option>
        </select>
    </div>

    <div class="form-group">
        <span>超级帮钻抵现状态：</span>
        <select class="form-control" name="bzStatus" id="search_bzStatus">
            <option value="" ${empty bzStatus ? "selected" : ""}>全部</option>
            <option value="0" ${bzStatus == 0 ? "selected" : ""}>暂未支付</option>
            <option value="1" ${bzStatus == 1 ? "selected" : ""}>支付成功</option>
            <option value="5" ${status == 5 ? "selected" : ""}>前后两次帮钻抵现额不一致</option>
            <option value="6" ${status == 6 ? "selected" : ""}>超级帮钻支付密码错误</option>
            <option value="8" ${status == 8 ? "selected" : ""}>帮钻支付成功</option>
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
        <th style="width:5%;">订单号</th>
        <th style="width:5%;">用户ID</th>
        <th style="width:5%;">订单状态</th>
        <th style="width:5%;">应付金额</th>
        <th style="width:5%;">实付金额</th>
        <th style="width:5%;">第三方支付类型</th>
        <th style="width:5%;">第三方支付流水号</th>
        <th style="width:5%;">超级帮钻抵现额度</th>
        <th style="width:5%;">超级帮钻抵现状态</th>
        <th style="width:5%;">订单业务类型</th>
        <th style="width:5%;">运费</th>
        <th style="width:5%;">税款</th>
        <th style="width:5%;">货款金额</th>
        <th style="width:5%;">商户海关备案号</th>
        <th style="width:5%;">创建时间</th>
        <th style="width:5%;">更新时间</th>
        <th style="width:10%;">操作</th>
    </tr>

    <c:forEach items="${result.list}" var="item" varStatus="n">
        <tr>
            <td style="word-wrap:break-word;font-size:13px;">
            	<a href="${ctx }/console/paymentOrder/query.htm?orderId=${item.orderId}">${item.orderId}</a>
            </td>
            <td style="word-wrap:break-word;font-size:13px;">
            	<a href="${ctx}/console/paymentOrder/query.htm?userId=${item.userId}">${item.userId}</a>
            </td>
            <td style="word-wrap:break-word;font-size:13px;">
                <c:if test="${item.status == 0}">暂未支付</c:if>
                <c:if test="${item.status == 1}">支付成功</c:if>
                <c:if test="${item.status == 2}">支付失败</c:if>
                <c:if test="${item.status == 3}">已完成</c:if>
                <c:if test="${item.status == 4}">已付款</c:if>
                <c:if test="${item.status == 5}">前后两次帮钻抵现额不一致</c:if>
                <c:if test="${item.status == 6}">超级帮钻支付密码错误</c:if>
                <c:if test="${item.status == 7}">支付错误</c:if>
                <c:if test="${item.status == 8}">帮钻支付成功</c:if>
            </td>
            <td style="word-wrap:break-word;font-size:13px;">${item.totalAmount}</td>
            <td style="word-wrap:break-word;font-size:13px;">${item.finalAmount}</td>
            <td style="word-wrap:break-word;font-size:13px;">${item.thirdPayType}</td>
            <td style="word-wrap:break-word;font-size:13px;">${item.thirdTradeNo}</td>
            <td style="word-wrap:break-word;font-size:13px;">${item.bzAmount}</td>
            <td style="word-wrap:break-word;font-size:13px;">
                <c:if test="${item.bzStatus == 0}">暂未支付</c:if>
                <c:if test="${item.bzStatus == 1}">支付成功</c:if>
                <c:if test="${item.bzStatus == 2}">支付失败</c:if>
                <c:if test="${item.bzStatus == 3}">已完成</c:if>
                <c:if test="${item.bzStatus == 4}">已付款</c:if>
                <c:if test="${item.bzStatus == 5}">前后两次帮钻抵现额不一致</c:if>
                <c:if test="${item.bzStatus == 6}">超级帮钻支付密码错误</c:if>
                <c:if test="${item.bzStatus == 7}">支付错误</c:if>
                <c:if test="${item.bzStatus == 8}">帮钻支付成功</c:if>
            </td>
            <td style="word-wrap:break-word;font-size:13px;">
                <c:if test="${item.source == 223}">PC端支付</c:if>
                <c:if test="${item.source == 224}">手机端支付</c:if>
                <c:if test="${item.source == 229}">app端帮钻充值</c:if>
                <c:if test="${item.source == 230}">1元兑换</c:if>
            </td>
            <td style="word-wrap:break-word;font-size:13px;">${item.fee}</td>
            <td style="word-wrap:break-word;font-size:13px;">${item.tax}</td>
            <td style="word-wrap:break-word;font-size:13px;">${item.goodsAmount}</td>
            <td style="word-wrap:break-word;font-size:13px;">${item.bgCode}</td>
            <td style="word-wrap:break-word;font-size:13px;">${item.createTime}</td>
            <td style="word-wrap:break-word;font-size:13px;">${item.updateTime}</td>

            <td style="word-wrap:break-word;font-size:13px;">
            	<div>
	                <a href="${ctx}/console/callback/query.htm?orderId=${item.orderId}">查看回调处理</a>
            	</div>
            	<div>
	                <a href="${ctx}/console/notify/query.htm?orderId=${item.orderId}">查看结果通知</a>&nbsp;&nbsp;
            	</div>
            	<c:if test="${item.status != 1}">
	            	<div>
		                <a class="important" href="${ctx}/console/paymentOrder/confirm.htm?orderId=${item.orderId}" redirect="${ctx}/console/paymentOrder/query.htm?orderId=${item.orderId}">确认支付结果</a>
	            	</div>
	            	<div>
		                <a class="important" _type="input" href="javascript:void()" _href="${ctx}/console/paymentOrder/retry.htm?orderId=${item.orderId}" redirect="${ctx}/console/paymentOrder/query.htm?orderId=${item.orderId}">超强纠错</a>
	            	</div>
            	</c:if>
            	<c:if test="${item.bzAmount > 0}">
	            	<div>
		                <a class="important" href="${ctx}/console/notify/retry.htm?type=BZ_FROZEN_SUCCESS&orderId=${item.orderId}" redirect="${ctx}/console/notify/query.htm?orderId=${item.orderId}">补发帮钻通知</a>&nbsp;&nbsp;
	            	</div>
            	</c:if>
            	<c:if test="${item.status == 1}">
	            	<div>
		                <a href="${ctx}/console/baoguan/query.htm?orderId=${item.orderId}">查看报关记录</a>&nbsp;&nbsp;
	            	</div>
	            	<div>
		                <a class="important" href="${ctx}/console/notify/retry.htm?type=PAY_SUCCESS&orderId=${item.orderId}" redirect="${ctx}/console/notify/query.htm?orderId=${item.orderId}">补发支付成功通知</a>&nbsp;&nbsp;
	            	</div>
	            	<c:if test="${item.thirdPayType != 9}">
		            	<div>
			                <a class="important" href="${ctx}/console/baoguan/retry.htm?orderId=${item.orderId}" redirect="${ctx}/console/baoguan/query.htm?orderId=${item.orderId}">主动报关</a>&nbsp;&nbsp;
		            	</div>
	            	</c:if>
            	</c:if>
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

        $(".important").click(function(){
       		var type = $(this).attr("_type");
           	var url = $(this).attr("href");
       		var redirect = $(this).attr("redirect") || '';
       		var isContinue = false;
       		if(type == 'input'){
       			var thirdTradeNo = prompt('请认真填写正确的第三方支付号');
       			url = $(this).attr("_href");
       			if(thirdTradeNo != ''){
	       			var channel = prompt('请认真填写正确的第三方支付渠道号');
       				url = url + '&thirdTradeNo=' + thirdTradeNo + '&channel=' + channel;
       				isContinue = true;
       			}
       		}else{
       			isContinue = confirm("确定要这样做吗?");
       		}
            if(isContinue){
	           	$.getJSON(url, function(data){
	           		if(data.code == '2000' || data.code == '50080001045'){
	           			if(redirect != ''){
	            			window.location.href = redirect;
	           			}else{
	           				alert(data.data || '操作成功');
	           			}
	           		}else{
	           			alert(data.msg || '操作失败');
	           		}
	           	});
            }
           	return false;
        });

        $(".btn").click(function(){
            var params = getParams();
            window.location.href = "/console/paymentOrder/query.htm?"+params;
        });
    });
    // 获得查询参数
    function getParams(){
        var search_orderId = $("#search_orderId")[0].value;
        var search_userId = $("#search_userId")[0].value;
        var search_status = $("#search_status")[0].value;
        var search_thirdPayType = $("#search_thirdPayType")[0].value;
        var search_thirdTradeNo = $("#search_thirdTradeNo")[0].value;
        var search_bzStatus = $("#search_bzStatus")[0].value;
        var search_startTime = $("#start_time")[0].value;
        var search_endTime = $("#end_time")[0].value;

        var params = 'orderId='+search_orderId+'&userId='+search_userId+'&status='+search_status+'&thirdPayType='+search_thirdPayType+'&thirdTradeNo='+search_thirdTradeNo+'&bzStatus='+search_bzStatus+'&startTime='+search_startTime
                +'&endTime='+search_endTime;
        return params;
    }
</script>