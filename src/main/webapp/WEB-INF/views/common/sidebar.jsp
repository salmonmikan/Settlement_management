<!-- /WEB-INF/views/common/sidebar.jsp -->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String staffName = "石井 和也"; // demo用
String role = "admin"; // manager/admin で切り替え可能
String position = (String) session.getAttribute("position_id");
String department = (String) session.getAttribute("department_id");
%>
<div class="sidebar">
	<div class="menu-block">
		<h3>メニュー</h3>
		<ul>
			<!-- 「test」はcf用の評価式、任意の変数ではない -->
			<!-- 一般社員または主任（営業部） -->
			<%
			if (("P0004".equals(position) || "P0003".equals(position)) && "D0001".equals(department)) {
			%>
			<li><a href="staffMenu.jsp">社員メニュー</a></li>
			<%}%>

			<!-- 一般社員（管理部） -->
			<%
			if ("P0004".equals(position) && "D0002".equals(department)) {
			%>
			<li><a href="<%=request.getContextPath()%>/applicationList">申請一覧</a></li>
			<hr>
			<li><a href="#">承認申請一覧</a></li>
			<li><a href="#">承認履歴</a></li>
			<hr>
			<li><a href="<%=request.getContextPath()%>/project_management_view">プロジェクト管理</a></li>
			<li><a href="<%=request.getContextPath()%>/employeeList">社員管理</a></li>
			<li><a href="<%=request.getContextPath()%>/department">部署管理</a></li>
			<li><a href="#">役職管理</a></li>
			<hr>
			<li><a href="<%=request.getContextPath()%>/payment management.jsp">支払い管理</a></li>
			<li><a href="#">パスワード変更</a></li>
			<%
			}
			%>

			<!-- 部長（営業部） -->
			<% if ("P0002".equals(position) && "D0001".equals(department)) {
			%>
			<li><a href="buchougamen.jsp">部長メニュー</a></li>
			<%
			}
			%>
		</ul>
	</div>
	<div class="welcome-message">
		ようこそ、<%=staffName%>さん！（管理者）
	</div>
</div>