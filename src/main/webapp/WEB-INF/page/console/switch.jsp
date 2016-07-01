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
<div class="row">
	<div class="span4 offset7" style="padding-left: 15px;padding-top: 2px;">
		<form action="/console/switch/query.htm" method="post">
			<div class="form-inline" role="form">
				<div class="form-group">
					<span>渠道key：</span>
					<input name="keyName" id="search_keyName" class="form-control" type="text" placeholder="渠道key" value="${keyName}">
				</div>
				<div class="form-group">
					<button id="search" type="submit" class="btn btn-primary">查询</button>
				</div>
			</div>
		</form>
	</div>
</div>
<table id="contentTable" class="table table-striped table-condensed table-bordered" width="100%" style="table-layout:fixed;">
    <tr>
        <th style="width:25%;">key</th>
        <th style="width:10%;">value</th>
        <th style="width:35%;">备注</th>
        <th style="width:10%;">创建时间</th>
        <th style="width:10%;">更新时间</th>
        <th style="width:10%;">操作</th>
    </tr>

        <c:forEach items="${result.list}" var="item" varStatus="n">
            <tr>
                <td style="word-wrap:break-word;font-size:13px;">${item.key}</td>
                <td style="word-wrap:break-word;font-size:13px;" class="td_value">${item.value}</td>
                <td style="word-wrap:break-word;font-size:13px;">${item.memo}</td>
                <td style="word-wrap:break-word;font-size:13px;">${item.createTime}</td>
                <td style="word-wrap:break-word;font-size:13px;">${item.updateTime}</td>
                <td style="word-wrap:break-word;font-size:13px;">
					<input class="switch" type="checkbox" href="toggle.htm?key=${item.key}"/>
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
		$(".td_value").each(function(){
			var value = $(this).text();
			if(value == 'true'){
				$(this).text("启用");
				$(this).css("color", "green");
				$(this).parent().find(".switch").bootstrapSwitch('state', false);
			}else{
				$(this).text("禁用");
				$(this).css("color", "red");
				$(this).parent().find(".switch").bootstrapSwitch('state', true);
			}
		});
		$('.switch').on('switchChange.bootstrapSwitch', function (event, state) {
			var url = $(event.target).attr("href");
			$.getJSON(url, function(data){
				alert(data.code == '2000' ? data.data : data.msg);
			});
		});
   	});
</script>