<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- <%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %> --%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
    String version= "?v="+application.getInitParameter ("version");
%>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%@include file="../layouts/bringin.jsp"%>
<title>支付系统配置中心</title>
</head>
<body style="min-width:1200px">
	<div class="container-fluid" style="padding:0;">
	    <div class="row-fluid">
	    	<div class="col-lg-2" style="padding:0;width:16%;float:left;">
	        	<%@ include file="/WEB-INF/layouts/left.jsp"%>
	        </div>
	        <div class="col-lg-10" style="padding:0;width:83%;float:left;">
	            <sitemesh:body/>
	        </div>
	    </div>
	    <%@ include file="/WEB-INF/layouts/footer.jsp"%>
	</div>
</body>
<script type="text/javascript">

</script>
</html>