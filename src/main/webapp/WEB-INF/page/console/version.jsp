<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>

<head>
    <meta charset="utf-8" />
    <title>支付版本配置管理页面</title>
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

<div class="row">
    <div class="span4 offset7" style="padding-left: 30px;">
        <h3>前期端版本号：<em>${pageVersion}</em>
        <button onclick=" refreshPageVersion();" type="submit" class="btn btn-primary" id="add">刷新前端版本号</button></h3>
    </div>

    <div class="span4 offset7" style="padding-left: 30px;">
        <h3> 清空支付信息缓存：
        <button onclick="clearCacheObj();" type="submit" class="btn btn-primary" id="cacheDBConfigClean">清空缓存信息</button></h3>
    </div>
</div>

<!-- Form 模态框（Modal） -->
<div class="modal fade" id="myVersionModal" tabindex="-1" role="dialog" aria-labelledby="myVersionModalLabel" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog" style="width: 900px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myVersionModalLabel"></h4>
            </div>
            <div class="modal-body">
                <form id="inputForm" method="post" class="form-horizontal" role="form" enctype="multipart/form-data">
                    <div class="form-group" id="keyNameDiv">
                        <label for="keyName" class="col-sm-2 control-label">keyName:</label>
                        <div class="col-lg-8">
                            <input type="text" id="keyName" name="payIds" class="form-control required"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="pwd1" class="col-sm-2 control-label">pwd:</label>
                        <div class="col-lg-8">
                            <input type="password" id="pwd1" name="pwd" class="form-control required"/>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button type="button" id="submitBtn1" class="btn btn-primary"  >提交</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    function refreshPageVersion() {
        $("#keyNameDiv").css("display","none");
        $("#myVersionModalLabel").text("更新支付前端版本缓存");
        $("#inputForm").attr("action", "/server/refleshFrontTimestamp.htm");
        $("#submitBtn1").click(function(){
            var param = $('#inputForm').serialize();
            $.post($('#inputForm').attr('action'), param, function(data) {
                alert("code:"+data.code+",data:"+data.data+",msg:"+data.msg);
                refresh();
            });
        });
        $('#myVersionModal').modal('show');
    }

    function clearCacheObj() {
        $("#keyNameDiv").css("display","block");
        $("#myModal").find(":input").addClass('required');
        $("#myVersionModalLabel").text("清空支付信息缓存信息");
        $("#inputForm").attr("action", "/server/cancelPayment.htm");
        $("#submitBtn1").click(function(){
            var param = $('#inputForm').serialize();
            $.post($('#inputForm').attr('action'), param, function(data) {
                alert("code:"+data.code+",data:"+data.data+",msg:"+data.msg);
                refresh();
            });
        });
        $('#myVersionModal').modal('show');
    }

    function refresh(){
        location.reload();
    }

</script>
</body>
</html>