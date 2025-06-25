<!-- /WEB-INF/views/common/sidebar.jsp -->
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String position = (String) session.getAttribute("position");
String department = (String) session.getAttribute("department");
%>
<div class="sidebar">
	<div class="menu-block">
		<h3>メニュー</h3>
		<ul>
			<!-- 一般社員または主任（営業部） -->
			<%
			if (("一般社員".equals(position) || "主任".equals(position)) && "営業部".equals(department)) {
			%>
			<li><a href="<%=request.getContextPath()%>/applicationMain" class="btn">申請一覧</a></li>
			<li><a href="#">パスワード変更</a></li>
			<%
			}
			%>

			<!-- 一般社員（管理部） -->
			<%
			if ("一般社員".equals(position) && "管理部".equals(department)) {
			%>
			<li><a href="<%=request.getContextPath()%>/applicationList">申請一覧</a></li>
			<hr>
			<li><a href="#">精算承認</a></li>
			<hr>
			<li><a href="<%=request.getContextPath()%>/project_management_view">プロジェクト管理</a></li>
			<li><a href="<%=request.getContextPath()%>/employeeList">社員管理</a></li>
			<li><a href="<%=request.getContextPath()%>/department">部署管理</a></li>
			<li><a href="#">役職管理</a></li>
			<hr>
			<li><a href="<%=request.getContextPath()%>/payment management.jsp">支払い管理</a></li>
			<hr>
			<li><a href="#">パスワード変更</a></li>
			<%
			}
			%>

			<!-- 部長（営業部） -->
			<%
			if ("部長".equals(position) && "営業部".equals(department)) {
			%>
			<li><a href="<%=request.getContextPath()%>/applicationMain" class="btn">申請一覧</a></li>
			<hr>
			<li><a href="#">精算承認</a></li>
			<hr>
			<li><a href="#">パスワード変更</a></li>
			<%
			}
			%>
		</ul>
	</div>
	<div class="welcome-message">
		
	</div>
</div>