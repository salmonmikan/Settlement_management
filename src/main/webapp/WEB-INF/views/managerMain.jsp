<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String staffName = "石井 和也"; // demo用
  String role = "admin"; // manager/admin で切り替え可能
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>管理ダッシュボード - ABC株式会社</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
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
  </style>
</head>
<body>

  <div class="page-container">
    <h1>管理者メニュー</h1>

    <div class="staff-dashboard-wrapper">

      <!-- Sidebar -->
      <div class="sidebar">
        <div class="menu-block">
          <h3>メニュー</h3>
          <ul>
            <li><a href="#">個人設定</a></li>
            <li><a href="#">承認申請一覧</a></li>
            <li><a href="#">承認履歴</a></li>
            <li><a href="#">プロジェクト管理</a></li>
            <li><a href="#">スタッフ管理</a></li>
          </ul>
        </div>
        <div class="welcome-message">
          ようこそ、<%= staffName %> さん！（管理者）
        </div>
      </div>

      <!-- Main content -->
      <div class="staff-main-content">
		
		<div class="panel">
          <h4>申請する</h4>
          <div class="btn-section">
            <a href="#">交通費申請</a>
            <a href="<%= request.getContextPath() %>/views/business_trip_form.jsp">出張費申請</a>
            <a href="#">立替金申請</a>
          </div>
        </div>
        <!-- 承認処理 -->
        <div class="panel">
          <h4>申請一覧</h4>
          <ul>
            <li><a href="approvalList.jsp?status=pending">承認待ち申請</a></li>
            <li><a href="approvalList.jsp?status=approved">承認済み履歴</a></li>
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