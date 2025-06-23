<%@ page contentType="text/html; charset=UTF-8" %>
<%
		String staffName = (String) session.getAttribute("fullName");
		if (staffName == null) {
			response.sendRedirect(request.getContextPath() + "/views/login.jsp");
			return;
		};
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>管理ダッシュボード - ABC株式会社</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" />
  <script src="<%= request.getContextPath() %>/static/js/script.js"></script>
  <style>
    .panel-fullheight {
      flex: 1;
    }
    .panel ul {
      padding-left: 1.2rem;
      margin-top: 0.5rem;
    }
    .panel ul li {
      margin-bottom: 0.5rem;
      font-size: 0.92rem;
    }
    .border-box {
           border: 2px solid #333;
           padding: 20px;
           margin: 30px;
       }
  </style>
</head>
<body>
	<nav>
		ようこそ、<%= staffName %> 様！
		<form class="logoutForm" action="<%= request.getContextPath() %>/logOutServlet" method="post">
    	<button type="submit"  title="Log out"><i class="fa-solid fa-right-from-bracket"></i></button>
		</form>
	</nav>
  <div class="page-container">
    <h1>管理者メニュー</h1>

    <div class="staff-dashboard-wrapper">

      <!-- Sidebar -->
      <div class="sidebar">
        <div class="menu-block">
          <h3>メニュー</h3>
          <ul>
            <li><a href="<%= request.getContextPath() %>/app">申請一覧</a></li>
            <hr>
            <li><a href="#">承認申請一覧</a></li>
            <li><a href="#">承認履歴</a></li>
            <hr>
            <li><a href="#">プロジェクト管理</a></li>
            <li><a href="<%= request.getContextPath() %>/employeeList">社員管理</a></li>
            <li><a href="<%= request.getContextPath() %>/department">部署管理</a></li>
            <li><a href="#">役職管理</a></li>
            <hr>
            <li><a href="#">支払い管理</a></li>
            <li><a href="#">パスワード変更</a></li>
          </ul>
        </div>
      </div>

      <!-- Main content -->
      <div class="staff-main-content">
		
		<div class="panel" style="max-height: 150px;">
          <h4>申請する</h4>
          <div class="btn-section">
            <a href="#">交通費申請</a>
            <a href="<%= request.getContextPath() %>/businessTrip">出張費申請</a>
            <a href="<%= request.getContextPath() %>/reimbursement">立替金申請</a>
          </div>
        </div>
        <!-- 承認処理 -->
        <div class="panel">
          <h4>申請一覧</h4>
          <ul>
            <li><a href="approvalList.jsp?status=pending">未提出: 1件</a></li>
            <li><a href="approvalList.jsp?status=pending">提出済: 1件</a></li>
            <li><a href="approvalList.jsp?status=approved">差し戻し: 1件</a></li>
          </ul>
        </div>

        <!-- 管理機能 -->
        

      </div>
    </div>
  </div>

  <div class="footer">
    &copy; 2025 ABC株式会社 - All rights reserved.
  </div>

</body>
</html>