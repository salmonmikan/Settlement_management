<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${empty sessionScope.staffName}">
    <c:redirect url="/LoginServlet" />
</c:if>

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>スタッフダッシュボード - ABC株式会社システム</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" />
  <script src="${pageContext.request.contextPath}/static/js/script.js"></script>
</head>
<body>

	<nav>
		精算管理システム
		<form class="logoutForm" action="${pageContext.request.contextPath}/logOutServlet" method="post">
    	<button type="submit"  title="Log out"><i class="fa-solid fa-right-from-bracket"></i></button>
		</form>
	</nav>

  <div class="page-container">
    <%-- [CẢI THIỆN] Hiển thị tên người dùng bằng EL --%>
    <p style="text-align: right;">ようこそ、${sessionScope.staffName} さん！</p>

    <div class="staff-dashboard-wrapper">

      <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

      <div class="staff-main-content">
        <div class="panel">
          <h4>申請する</h4>
          <div class="btn-section">
            <a href="${pageContext.request.contextPath}/transportationInit">交通費申請</a>
            <a href="${pageContext.request.contextPath}/businessTripInit">出張費申請</a>
            <a href="${pageContext.request.contextPath}/reimbursementInit">立替金申請</a>
          </div>
        </div>

        <div class="panel">
          <h4>申請状況</h4>
          <ul>
             
            <li><a href="${pageContext.request.contextPath}/approvalList?status=not_submitted">未提出:</a></li>
            <li><a href="${pageContext.request.contextPath}/approvalList?status=submitted">提出済:</a></li>
            <li><a href="${pageContext.request.contextPath}/approvalList?status=rejected">差戻:</a></li>
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