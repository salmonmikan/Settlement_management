<%@ page contentType="text/html; charset=UTF-8"%>
<%
String position = (String) session.getAttribute("position_id");
String department = (String) session.getAttribute("department_id");
%>
<div class="sidebar">
	<div class="menu-block">
		<h3>メニュー</h3>
		<ul>
			<%
			if ("P0002".equals(position) && "D0002".equals(department)) {
			%>
			<!-- 部長 / 管理部 -->
			<li><a href="<%=request.getContextPath()%>/applicationMain"
				class="btn">申請一覧</a></li>
			<hr>
			<li><a href="<%=request.getContextPath()%>/approverApplications">精算承認</a></li>
			<hr>
			<li><a
				href="<%=request.getContextPath()%>/project_management_view">プロジェクト管理</a></li>
			<li><a href="<%=request.getContextPath()%>/employeeList">社員管理</a></li>
			<li><a href="<%=request.getContextPath()%>/department">部署管理</a></li>
			<li><a href="<%=request.getContextPath()%>/positionControl">役職管理</a></li>
			<hr>
			<li><a href="<%=request.getContextPath()%>/paymentList">支払い管理</a></li>
			<hr>
			<li><a href="<%=request.getContextPath()%>/changePass.jsp"
				class="btn">パスワード変更</a></li>
			<%
			} else if ("P0004".equals(position) && "D0002".equals(department)) {
			%>
			<!-- 一般社員 / 管理部 -->
			<li><a href="<%=request.getContextPath()%>/applicationMain"
				class="btn">申請一覧</a></li>
			<hr>
			<li><a
				href="<%=request.getContextPath()%>/project_management_view">プロジェクト管理</a></li>
			<li><a href="<%=request.getContextPath()%>/employeeList">社員管理</a></li>
			<li><a href="<%=request.getContextPath()%>/department">部署管理</a></li>
			<li><a href="<%=request.getContextPath()%>/positionControl">役職管理</a></li>
			<hr>
			<li><a href="<%=request.getContextPath()%>/paymentList">支払い管理</a></li>
			<hr>
			<li><a href="<%=request.getContextPath()%>/changePass.jsp"
				class="btn">パスワード変更</a></li>
			<%
			} else if ("P0002".equals(position) && "D0001".equals(department)) {
			%>
			<!-- 部長 / 営業部 -->
			<li><a href="<%=request.getContextPath()%>/applicationMain"
				class="btn">申請一覧</a></li>
			<hr>
			<li><a href="<%=request.getContextPath()%>/approverApplications">精算承認</a></li>
			<hr>
			<li><a href="<%=request.getContextPath()%>/changePass.jsp"
				class="btn">パスワード変更</a></li>
			<%
			} else if ("P0003".equals(position) && "D0001".equals(department)) {
			%>
			<!-- 主任 / 営業部 -->
			<li><a href="<%=request.getContextPath()%>/applicationMain"
				class="btn">申請一覧</a></li>
			<hr>
			<li><a
				href="<%=request.getContextPath()%>/project_management_view">プロジェクト管理</a></li>
			<hr>
			<li><a href="<%=request.getContextPath()%>/changePass.jsp"
				class="btn">パスワード変更</a></li>
			<%
			} else if ("P0004".equals(position) && "D0001".equals(department)) {
			%>
			<!-- 一般社員 / 営業部 -->
			<li><a href="<%=request.getContextPath()%>/applicationMain"
				class="btn">申請一覧</a></li>
			<hr>
			<li><a href="<%=request.getContextPath()%>/changePass.jsp"
				class="btn">パスワード変更</a></li>
			<%
			} else {
			%>
			<!-- どの条件にも当てはまらない場合 -->
			<li><a href="<%=request.getContextPath()%>/applicationMain"
				class="btn">申請一覧</a></li>
			<hr>
			<li><a href="<%=request.getContextPath()%>/changePass.jsp"
				class="btn">パスワード変更</a></li>
			<%
			}
			%>
		</ul>
	</div>
	<div class="back_top" style="text-align: center; margin-top: 30px;">
		<a href="<%=request.getContextPath()%>/menu">トップに戻る</a>
	</div>
</div>
