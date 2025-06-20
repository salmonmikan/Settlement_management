<%@ page contentType="text/html; charset=UTF-8" %>
<%
		String staffName = (String) session.getAttribute("staffName");
		if (staffName == null) {
			response.sendRedirect(request.getContextPath() + "/views/login.jsp");
			return;
		};
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>スタッフダッシュボード - ABC株式会社システム</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
<!--  cdn link-->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" />
  <script src="<%= request.getContextPath() %>/static/js/script.js"></script>
</head>
<body>

	<nav>
		ようこそ、<%= staffName %> 様！
		<form class="logoutForm" action="<%= request.getContextPath() %>/logOutServlet" method="post">
    	<button type="submit"  title="Log out"><i class="fa-solid fa-right-from-bracket"></i></button>
		</form>
	</nav>

  <div class="page-container">
    <h1>スタッフメニュー</h1>

    <div class="staff-dashboard-wrapper">

      <!-- Sidebar -->
      <div class="sidebar">
		  <div class="menu-block">
		    <h3>メニュー</h3>
		    <ul>
		      <li><a href="#">パスワード変更</a></li>
		      <li><a href="<%= request.getContextPath() %>/applicationMain" class="btn">申請一覧</a></li>
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
            <a href="<%= request.getContextPath() %>/createApplication?type=出張費">出張費申請</a>
            <a href="<%= request.getContextPath() %>/reimbursement">立替金申請</a>

          </div>
        </div>

        <div class="panel">
          <h4>申請状況</h4>
          <ul>
            <li><a href="approvalList.jsp?status=pending">未提出</a></li>
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