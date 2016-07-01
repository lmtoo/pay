<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

<head>
    <meta charset="utf-8" />
    <title>支付配置管理页面</title>
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
</head>

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
    <div class="span4 offset7" style="padding-left: 15px;padding-top: 2px;">
        <button onclick="add();" type="submit" class="btn btn-primary" id="add">新增</button>
        <button onclick="refresh();" type="submit" class="btn btn-primary" id="refresh">刷新数据</button>
        <button onclick="cacheDBConfig();" type="submit" class="btn btn-primary" id="cacheDBConfigClean">清空本地缓存</button>
        <form action="/server/managePage.htm" method="post">
            <div class="form-inline" role="form">
                <div class="form-group">
                    <span>配置key：</span>
                    <input name="keyName" id="search_keyName" class="form-control" type="text" placeholder="配置key" value="${keyName}">
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
        <th style="width:5%;">配置id</th>
        <th style="width:15%;">配置key</th>
        <th style="width:50%;">配置value</th>
        <th>备注</th>
        <th>创建时间</th>
        <th>更新时间</th>
        <th>操作</th>
    </tr>

        <c:forEach items="${paymentConfigs.list}" var="item1" varStatus="nl1">
            <tr>
                <td style="word-wrap:break-word;font-size:13px;">${item1.id}</td>
                <td style="word-wrap:break-word;font-size:13px;">${item1.keyName}</td>
                <td style="word-wrap:break-word;font-size:13px;" id="configRecordVal_${item1.id}">${item1.keyValue}</td>
                <td style="word-wrap:break-word;font-size:13px;">${item1.memo}</td>
                <td style="word-wrap:break-word;font-size:13px;">${item1.createTime}</td>
                <td style="word-wrap:break-word;font-size:13px;">${item1.updateTime}</td>
                <td style="word-wrap:break-word;font-size:13px;">
                    <a title="编辑" onclick="update('${item1.id}','${item1.keyName}','${item1.memo}')" class="btn btn-primary btn-xs"><span class="glyphicon glyphicon-edit"></span></a>
                </td>
            </tr>
        </c:forEach>
</table>

<!-- Form 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog" style="width: 900px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel"></h4>
            </div>
            <div class="modal-body">
                <form id="inputForm" method="post" class="form-horizontal" role="form" enctype="multipart/form-data">
                    <div class="form-group" style="display:none">
                        <label for="id" class="col-sm-2 control-label">ID:</label>
                        <div class="col-lg-8">
                            <input type="text" id="id" name="id" class="form-control required"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="keyName" class="col-sm-2 control-label">keyName:</label>
                        <div class="col-lg-8">
                            <input type="text" id="keyName" name="keyName" class="form-control required"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="keyValue" class="col-sm-2 control-label">keyValue:</label>
                        <div class="col-lg-8">
                            <textarea id="keyValue" name="keyValue" class="form-control required" rows="20" cols="20"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="memo" class="col-sm-2 control-label">memo:</label>
                        <div class="col-lg-8">
                            <input type="text" id="memo" name="memo" class="form-control required"/>
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
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
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


<div id="pagination" class="pagination"></div>
<script type="text/javascript">
    $(document).ready(function() {
        //聚焦第一个输入框
        $("#id").focus();
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

    function cleanInputContent(){
        $("#myModal").find(":text").val('');
        $("#myModal").find("textarea").val('');
        $("#myModal").find("span[class='error']").html('');
        $("#myModal2").find(":text").val('');
        $("#myModal2").find("textarea").val('');
        $("#myModal2").find("span[class='error']").html('');
    }

    function add() {
        cleanInputContent();
        $("#myModalLabel").text("更新及修改操作");
        $("#inputForm").attr("action", "/server/paymentConfigOper.htm");
        $('#myModal').modal('show');
    }

    function update(id,keyName,memo) {
        cleanInputContent();
        $("#id").val(id);
        $("#keyName").val(keyName);
        $("#keyValue").val($("#configRecordVal_"+id).html());
        $("#memo").val(memo);
        $("#inputForm").attr("action", "/server/paymentConfigOper.htm");
        $("#myModalLabel").text("更新及修改操作");
        $('#myModal').modal('show');
    }
    /**
     * 更新配置文件操作
     */
    function paymentConfigOper(){
        var param = $('#updateForm').serialize();
        $.post($('#updateForm').attr('action'), param, function(data) {
            alert("code:"+data.code+",data:"+data.data+",msg:"+data.msg);
            refresh();
        });
    }

    /**
     * 清空服务器数据
     */
    function cacheDBConfig() {
        cleanInputContent();
        $("#myModalLabel2").text("清空支付配置信息本地缓存");
        $("#inputForm2").attr("action", "/server/cacheDBConfigClean.htm");
        $('#myModal2').modal('show');
    }

    function refresh(){
        location.reload();
    }

$(function(){
        var currentPage = ${paymentConfigs.page.pageNum - 1};
        /* alert(currentPage); */
        $("#pagination").pagination(${paymentConfigs.page.total}, {
            current_page: currentPage,
            items_per_page: ${paymentConfigs.page.pageSize},
            callback : function(page, pagebar){
                if(page != currentPage){
                    window.location.href = 'managePage.htm?pageNum=' + (page + 1);
                }
                return false;
            }
        });
});
</script>
</body>
</html>