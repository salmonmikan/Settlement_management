<%@ page contentType="text/html; charset=UTF-8" %>
<%
		  String staffName = (String) session.getAttribute("staffName");
		  if (staffName == null) {
		    response.sendRedirect(request.getContextPath() + "/views/login.jsp");
		    return;
		  }
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>スタッフダッシュボード - ABC株式会社システム</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

  <div class="page-container">
    <h1>スタッフメニュー</h1>

    <div class="staff-dashboard-wrapper">

      <!-- Sidebar -->
      <div class="sidebar">
		  <div class="menu-block">
		    <h3>メニュー</h3>
		    <ul>
		      <li><a href="#">個人設定</a></li>
		      <li><a href="#">パスワード変更</a></li>
		      <li><a href="#">申請一覧</a></li>
		    </ul>
		  </div>
		  
		  <div class="welcome-message">
		    ようこそ、<%= staffName %> さん！
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

        <div class="panel">
          <h4>申請一覧</h4>
          <ul>
            <li><a href="approvalList.jsp?status=pending">未提出: 1件</a></li>
            <li><a href="approvalList.jsp?status=pending">提出済: 1件</a></li>
            <li><a href="approvalList.jsp?status=approved">差し戻し: 1件</a></li>
          </ul>
        </div>
      </div>

    </div>
  </div>

  <div class="footer">
    &copy; 2025 ABC株式会社 - All rights reserved.
  </div>

</body>
</html>