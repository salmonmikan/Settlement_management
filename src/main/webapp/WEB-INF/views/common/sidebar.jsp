<%@ page contentType="text/html; charset=UTF-8"%>
<%
String position = (String) session.getAttribute("position_id");
String department = (String) session.getAttribute("department_id");
%>

<div class="sidebar">
    <!-- Nút ba gạch mở menu -->
    <div class="menu-toggle" onclick="toggleSidebar()">☰</div>

    <!-- Lớp phủ nền mờ -->
    <div class="menu-overlay" onclick="toggleSidebar()"></div>

    <div class="menu-content">
        <!-- Nút thu menu -->
        <div class="menu-close" onclick="toggleSidebar()">✕</div>

        <h3>メニュー</h3>
        <ul>
            <% if ("P0002".equals(position) && "D0002".equals(department)) { %>
                <li><a href="<%=request.getContextPath()%>/applicationMain">申請一覧</a></li>
                <li><a href="<%=request.getContextPath()%>/approverApplications">精算承認</a></li>
                <li><a href="<%=request.getContextPath()%>/project_management_view">プロジェクト管理</a></li>
                <li><a href="<%=request.getContextPath()%>/employeeList">社員管理</a></li>
                <li><a href="<%=request.getContextPath()%>/department">部署管理</a></li>
                <li><a href="<%=request.getContextPath()%>/positionControl">役職管理</a></li>
                <li><a href="<%=request.getContextPath()%>/paymentList">支払い管理</a></li>
                <li><a href="<%=request.getContextPath()%>/changePass.jsp">パスワード変更</a></li>
            <% } else if ("P0004".equals(position) && "D0002".equals(department)) { %>
                <li><a href="<%=request.getContextPath()%>/applicationMain">申請一覧</a></li>
                <li><a href="<%=request.getContextPath()%>/project_management_view">プロジェクト管理</a></li>
                <li><a href="<%=request.getContextPath()%>/employeeList">社員管理</a></li>
                <li><a href="<%=request.getContextPath()%>/department">部署管理</a></li>
                <li><a href="<%=request.getContextPath()%>/positionControl">役職管理</a></li>
                <li><a href="<%=request.getContextPath()%>/paymentList">支払い管理</a></li>
                <li><a href="<%=request.getContextPath()%>/changePass.jsp">パスワード変更</a></li>
            <% } else if ("P0002".equals(position) && "D0001".equals(department)) { %>
                <li><a href="<%=request.getContextPath()%>/applicationMain">申請一覧</a></li>
                <li><a href="<%=request.getContextPath()%>/approverApplications">精算承認</a></li>
                <li><a href="<%=request.getContextPath()%>/changePass.jsp">パスワード変更</a></li>
            <% } else if ("P0003".equals(position) && "D0001".equals(department)) { %>
                <li><a href="<%=request.getContextPath()%>/applicationMain">申請一覧</a></li>
                <li><a href="<%=request.getContextPath()%>/project_management_view">プロジェクト管理</a></li>
                <li><a href="<%=request.getContextPath()%>/changePass.jsp">パスワード変更</a></li>
            <% } else if ("P0004".equals(position) && "D0001".equals(department)) { %>
                <li><a href="<%=request.getContextPath()%>/applicationMain">申請一覧</a></li>
                <li><a href="<%=request.getContextPath()%>/changePass.jsp">パスワード変更</a></li>
            <% } else { %>
                <li><a href="<%=request.getContextPath()%>/applicationMain">申請一覧</a></li>
                <li><a href="<%=request.getContextPath()%>/changePass.jsp">パスワード変更</a></li>
            <% } %>
        </ul>
        <div class="back_top" style="text-align: center; margin-top: 20px;">
            <a href="<%=request.getContextPath()%>/menu">トップに戻る</a>
        </div>
    </div>
</div>

<script>
function toggleSidebar() {
    const sidebar = document.querySelector(".sidebar");
    sidebar.classList.toggle("open");
}
</script>
