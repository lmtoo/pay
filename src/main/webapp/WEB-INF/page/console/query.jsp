<%@page contentType="text/html; charset=utf-8" pageEncoding="utf-8"
	session="false"%>
<%@page import="java.sql.*"%>
<%@page import="java.util.ResourceBundle"%>
<%@ page import="com.b5m.payment.util.Constants" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>数据查询页面</title>
</head>
<body>
	<%!//数据库连接设置
	public static String getValue(String bundle, String key) {
		try {
			ResourceBundle rb = ResourceBundle.getBundle(bundle);
			return rb.getString(key);
		} catch(Exception e) {
			return null;

		}
	}
    static final String DRIVER_NAME = getValue("config/config", "jdbc.driver");
    static final String URL         = getValue("config/config", "jdbc.url"); 
    static final String USERNAME    = getValue("config/config", "jdbc.username");
    static final String PASSWORD    = getValue("config/config", "jdbc.password"); 

    static {
        try {
            Class.forName(DRIVER_NAME);
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }%>

	<form action="" method="post" class="form-horizontal" role="form" <%--enctype="multipart/form-data"--%>>
		<input type="hidden" name="sql" id="sql" value="" />
		<fieldset>
			<legend><small>查询</small></legend>

			<div class="form-group">
				<label for="sqlarea" class="col-sm-2 control-label">SQL语句：</label>
				<div class="col-lg-5">
					<textarea id="sqlarea" name="content" class="form-control" style="font-size:${param.fontSize==null?12:param.fontSize}px">${param.content == null ? "select * from t_user_payment_order t where t.order_id = 'b5m640492734002'":param.content}</textarea>
				</div>
			</div>

			<div class="form-group">
				<label for="startRow" class="col-sm-2 control-label">第一行:</label>
				<div class="col-lg-5">
					<input type="text" id="startRow" name="startRow" class="form-control" value="${param.startRow==null?0:param.startRow}"/>
				</div>
			</div>

			<div class="form-group">
				<label for="rowNum" class="col-sm-2 control-label">行数:</label>
				<div class="col-lg-5">
					<input type="text" id="rowNum" name="rowNum" class="form-control" value="${param.rowNum==null?100:param.rowNum}"/>
				</div>
			</div>


			<div class="form-group">
				<label for="password" class="col-sm-2 control-label">密码:</label>
				<div class="col-lg-5">
					<input type="password" id="password" name="password" class="form-control" value="${param.password}"/>
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<input id="submit_btn" on class="btn btn-primary" type="button" value="执行" onclick="exec('sqlarea');"/>&nbsp;
				</div>
			</div>
		</fieldset>

		<script>
			function exec(id) {
				var sql = "";
				var t = document.getElementById(id);
				if(window.getSelection) {
					if(t.selectionStart != undefined && t.selectionEnd != undefined) {
						sql = t.value.substring(t.selectionStart, t.selectionEnd);
					} else {
						sql = "";
					}
				} else {
					sql = document.selection.createRange().text;
				}
				document.getElementById('sql').value = sql;
				document.forms[0].submit();
			}
		</script>

	</form>

	<%
        String content = request.getParameter("content");
	    String sql = request.getParameter("sql");
	    String startRow = request.getParameter("startRow");
	    String rowNum = request.getParameter("rowNum");
	    String password = request.getParameter("password");
	    if(sql == null || "".equals(sql)){
	        sql = content;
	    }
	    if (sql == null || "".equals(sql)) {
	        return;
	    }
	    if (startRow == null || "".equals(startRow)) {
	        startRow = "0";
	    }
	    if (rowNum == null || "".equals(rowNum)) {
	        rowNum = "100";
	    }
	%>
	<div>执行结果：</div>
	<hr size="1">
	<%
	boolean isRun = true;
	if(password == null || "".equals(password)){
	    isRun = false;
        out.println("<div class='text-danger'>请输入密码!</div>");
    }else if(!Constants.INNER_QUERY_PWD.equals(password)){
        isRun = false;
        out.println("<div class='text-danger'>密码出错!</div>");
    }
    if(isRun && !sql.startsWith("select") && !sql.startsWith("SELECT")){
        isRun = false;
        out.println("<div class='text-danger'>非法操作!</div>");
    }
    if(isRun){
	    Connection con = null;
	    PreparedStatement pst = null;
	    try {
	        con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
	        sql = sql + " limit " + startRow + ", " + rowNum;
	        out.println(sql);
	        pst = con.prepareStatement(sql, 1005, 1008);
	        if (pst.execute()) {
	            ResultSet rs = pst.getResultSet();
	            rs.last();
	            int rowCount = rs.getRow();
	            //out.println("<div class=blue>查询行数：" + rowCount + "</div>");
	            rs.beforeFirst();
	            ResultSetMetaData metaData = rs.getMetaData();
	            int columnCount = metaData.getColumnCount();
	            String fontSize = request.getParameter("fontSize");
	            out.print("<table id='contentTable' class='table table-striped table-condensed table-bordered' width='100%' style='table-layout:fixed;'><tr>");
	            for (int i = 1; i <= columnCount; i++){
	                out.println("<th style=\"word-wrap:break-word;\">" + metaData.getColumnName(i) + "</th>");
	            }
	            out.println(" </tr>");
	            while (rs.next()) {
	                out.println("<tr>");
	                for (int i = 1; i <= columnCount; i++)
	                    out.println("<td style='word-wrap:break-word;' >" + rs.getObject(i) + "</td>");
	                out.println("</tr>");
	            }
	            out.println("</table>");
	            out.println("<div class=blue>查询行数：" + rowCount + "</div>");
	        } else{
	            out.println("<div class=blue>影响行数：" + pst.getUpdateCount() + "</div>");
	        }
	    } catch (Exception ex) {
	        out.println("<div class=red>" + ex.toString() + "</div>");
	    } finally {
	        try {
	            if (pst != null){
	                pst.close();
	            }
	            if (con != null){
	                con.close();
	            }
	        } catch (Exception ex) {
	            out.println("<div class=red>" + ex.toString() + "</div>");
	        }
	    }
    }
	%>




</body>
</html>
