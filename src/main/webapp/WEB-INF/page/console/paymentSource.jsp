<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<body>

<style>
	.btn-xs, .btn-group-xs >.btn {
		padding: 10px 15px;
	}
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
	<div class="span4 offset7" style="padding-left: 15px;">
		<button onclick="addPaySource();" type="submit" class="btn btn-primary" id="add">新增</button>
		<button onclick="refresh();" type="submit" class="btn btn-primary" id="refresh">刷新数据</button>
		<button onclick="cachePaySourceDBClean();" type="submit" class="btn btn-primary" id="cachePaySourceDBClean">清空本地缓存</button>
		<form action="/console/managePaymentSource.htm" method="post">
			<div class="form-inline" role="form">
				<div class="form-group">
					<span>SourceId：</span>
					<input name="sourceId" id="search_sourceId" class="form-control" type="text" placeholder="SourceId" value="${sourceIdVal}">
				</div>
				<div class="form-group">
					<button id="search" type="submit" class="btn btn-primary">查询</button>
				</div>
			</div>
		</form>
	</div>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
	<div class="modal-dialog" style="width: 900px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel"></h4>
			</div>
			<div class="modal-body">
				<form id="inputForm" method="post" class="form-horizontal" role="form" action="">
					<input type="hidden" id="idValInput" name="id" class="form-control"/>
					<div class="form-group">
						<label for="sourceId" class="col-sm-2 control-label">sourceId:</label>
						<div class="col-lg-8">
							<input type="text" id="sourceId" name="sourceId" class="form-control required"/>
						</div>
					</div>
					<div class="form-group">
						<label for="sourceKey" class="col-sm-2 control-label">sourceKey:</label>
						<div class="col-lg-8">
							<input type="text" id="sourceKey" name="sourceKey" class="form-control required"/>
						</div>
					</div>
					<div class="form-group">
						<label for="returnUrl" class="col-sm-2 control-label">returnUrl:</label>
						<div class="col-lg-8">
							<input type="text" id="returnUrl" name="returnUrl" class="form-control required"/>
						</div>
					</div>
					<div class="form-group">
						<label for="notifyUrl" class="col-sm-2 control-label">notifyUrl:</label>
						<div class="col-lg-8">
							<input type="text" id="notifyUrl" name="notifyUrl" class="form-control required"/>
						</div>
					</div>
					<div class="form-group">
						<label for="memo" class="col-sm-2 control-label">memo:</label>
						<div class="col-lg-8">
							<input type="text" id="memo" name="memo" class="form-control required"/>
						</div>
					</div>
					<div class="form-group">
						<label for="resultUrl" class="col-sm-2 control-label">resultUrl:</label>
						<div class="col-lg-8">
							<input type="text" id="resultUrl" name="resultUrl" class="form-control required"/>
						</div>
					</div>
					<div class="form-group" id="pwd2Div" >
						<label for="pwd2" class="col-sm-2 control-label">pwd:</label>
						<div class="col-lg-8">
							<input type="password" id="pwd2" name="pwd" class="form-control required"/>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button type="submit" class="btn btn-primary">提交</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="myModal2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel2" aria-hidden="true" data-backdrop="static">
	<div class="modal-dialog" style="width: 900px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel2"></h4>
			</div>
			<div class="modal-body">
				<form id="inputForm2" method="post" class="form-horizontal" role="form" action="">
					<div class="form-group" id="ipsDiv" >
						<label for="ips" class="col-sm-2 control-label">ips:</label>
						<div class="col-lg-8">
							<input type="text" id="ips" name="ips" class="form-control"/>
						</div>
					</div>
					<div class="form-group" id="pwd1Div" >
						<label for="pwd1" class="col-sm-2 control-label">pwd:</label>
						<div class="col-lg-8">
							<input type="password" id="pwd1" name="pwd" class="form-control required"/>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button type="submit" class="btn btn-primary">提交</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<table id="contentTable" class="table table-striped table-condensed table-bordered" width="100%" style="table-layout:fixed;">
	<tr>
		<th style="width:5%;">ID</th>
		<th style="width:10%;">sourceId</th>
		<th style="width:10%;">sourceKey</th>
		<th style="width:15%;">returnUrl</th>
		<th style="width:15%;">notifyUrl</th>
		<th style="width:10%;">createTime</th>
		<th style="width:10%;">memo</th>
		<th style="width:15%;">resultUrl</th>
		<th style="width:8%;">操作</th>
	</tr>

	<c:forEach items="${result.list}" var="item" varStatus="num">
		<tr>
			<td style="word-wrap:break-word;font-size:13px;">${item.id}</td>
			<td style="word-wrap:break-word;font-size:13px;" class="common">${item.sourceId}</td>
			<td style="word-wrap:break-word;font-size:13px;" class="common">${item.sourceKey}</td>
			<td style="word-wrap:break-word;font-size:13px;" class="common">${item.returnUrl}</td>
			<td style="word-wrap:break-word;font-size:13px;" class="common">${item.notifyUrl}</td>
			<td style="word-wrap:break-word;font-size:13px;">${item.createTime}</td>
			<td style="word-wrap:break-word;font-size:13px;" class="common">${item.memo}</td>
			<td style="word-wrap:break-word;font-size:13px;" class="common">${item.resultUrl}</td>
			<td align="center">
				<a title="编辑" onclick="modifyPaySource('${item.id}','${item.sourceId}','${item.sourceKey}','${item.returnUrl}','${item.notifyUrl}','${item.memo}','${item.resultUrl}')"
				   class="btn btn-primary btn-xs"><span class="glyphicon glyphicon-edit"></span></a>
				<button onclick="removePaySource(${item.id});" type="submit" class="btn btn-primary">删除 </button>
			</td>
		</tr>
	</c:forEach>
</table>
<div id="pagination" class="pagination"></div>
<script type="text/javascript">
	$(document).ready(function() {
		//聚焦第一个输入框
		$("#sourceId").focus();
		//为inputForm注册validate函数
		$("#inputForm").validate({
			submitHandler: function(form) {
				$(form).attr({enctype : "text/plain"});
				$(form).ajaxSubmit({
					data: $(form).serialize(),
					success: function(data) {
						console.log($(form).serialize());
						console.log(data.code);
						if (data.code == "2000") {
							BootstrapDialog.alert("更新成功", function() {
								$('#myModal').modal('hide');
								window.location.reload();
							});
						}else{
							BootstrapDialog.alert(data.msg);
						}
					},
					error: function() {
						BootstrapDialog.show({
							type: BootstrapDialog.TYPE_DANGER,
							title: '提示框',
							message: '系统错误!'
						});
					}
				});
			}
		});
		$("#inputForm2").validate({
			submitHandler: function(form) {
				$(form).attr({enctype : "text/plain"});
				$(form).ajaxSubmit({
					data: $(form).serialize(),
					success: function(data) {
						console.log($(form).serialize());
						console.log(data.code);
						if (data.code == "2000") {
							BootstrapDialog.alert("更新成功", function() {
								$('#myModal').modal('hide');
								window.location.reload();
							});
						}else{
							BootstrapDialog.alert(data.msg);
						}
					},
					error: function() {
						BootstrapDialog.show({
							type: BootstrapDialog.TYPE_DANGER,
							title: '提示框',
							message: '系统错误!'
						});
					}
				});
			}
		});
	});

	$(function(){
		var currentPage = ${result.page.pageNum - 1};
		$("#pagination").pagination(${result.page.total}, {
			current_page: currentPage,
			items_per_page: ${result.page.pageSize},
			callback : function(page, pagebar){
				if(page != currentPage){
					window.location.href = 'managePaymentSource.htm?pageNum=' + (page + 1);
				}
				return false;
			}
		});
	});

	function refresh(){
		location.reload();
	}

	function removePaySource(id){
		if(confirm('确定删除吗?')){
			var pwd = prompt('请输入确认删除的相关密码');
			var url = "/console/deletePaymentSource.htm?id="+id+"&pwd="+pwd;
			$.getJSON(url, function(data){
				if(data.code == '2000'){
					alert(data.data || '操作成功');
					refresh();
				}else{
					alert(data.msg || '操作失败');
				}
			});
		}
		return false;
	}

	function cleanText(){
		$("#myModal").find(":text").val('');
		$("#myModal").find("textarea").val('');
		$("#myModal").find("span[class='error']").html('');
		$("#myModal2").find(":text").val('');
		$("#myModal2").find("textarea").val('');
		$("#myModal2").find("span[class='error']").html('');
	}

	function addPaySource(){
		cleanText();
		$("#myModalLabel").text("新增支付来源操作");
		$("#inputForm").attr("action", "/console/addPaymentSource.htm");
		$('#myModal').modal('show');
	}

	function modifyPaySource(id,sourceId,sourceKey,returnUrl,notifyUrl,memo,resultUrl){
		cleanText();
		$("#myModalLabel").text("修改支付来源操作");
		$("#idValInput").val(id);
		$("#sourceId").val(sourceId);
		$("#sourceKey").val(sourceKey);
		$("#returnUrl").val(returnUrl);
		$("#notifyUrl").val(notifyUrl);
		$("#memo").val(memo);
		$("#resultUrl").val(resultUrl);
		$("#inputForm").attr("action", "/console/updatePaymentSource.htm");
		$('#myModal').modal('show');
	}

	function cachePaySourceDBClean() {
		cleanText();
		$("#myModalLabel2").text("清空支付来源本地缓存");
		$("#inputForm2").attr("action", "/console/cacheDBPaySourceClean.htm");
		$('#myModal2').modal('show');
	}

</script>
</body>
</html>