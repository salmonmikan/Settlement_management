<%@ page contentType="text/html; charset=UTF-8"%>
<!--<<<<<<< HEAD-->
<!--<%-->
<!--		String staffName = (String) session.getAttribute("staffName");-->
<!--		if (staffName == null) {-->
<!--			response.sendRedirect(request.getContextPath() + "/views/login.jsp");-->
<!--			return;-->
<!--		};-->
<!--%>-->
<!--<%-->
<!--  String staffId = (String) session.getAttribute("staffId");-->
<!--  if (staffId == null) {-->
<!--    response.sendRedirect(request.getContextPath() + "/login.jsp");-->
<!--    return;-->
<!--  }-->
<!--%>-->
<!--=======-->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- [SỬA LỖI & CẢI THIỆN] Kiểm tra đăng nhập an toàn bằng JSTL --%>
<c:if test="${empty sessionScope.staffName}">
	<%-- Nếu trong session không có "staffName", chuyển hướng về trang login --%>
	<c:redirect url="/LoginServlet" />
</c:if>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>スタッフダッシュボード - ABC株式会社システム</title>
<%-- [CẢI THIỆN] Sử dụng EL (Expression Language) cho các đường dẫn --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static/css/style.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" />
<script src="${pageContext.request.contextPath}/static/js/script.js"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp" />

	<div class="page-container">
		<%-- [CẢI THIỆN] Hiển thị tên người dùng bằng EL --%>
		<div class="content-container">
			<p style="text-align: right;">ようこそ、${sessionScope.staffName} さん！</p>

			<%-- Giả sử sidebar của bạn nằm ở đây, tôi giữ nguyên --%>
			<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />

			<div class="staff-main-content">
				<div class="panel">
					<h4>申請する</h4>
					<div class="btn-section">
						<%-- [CẢI THIỆN] Sử dụng EL cho các đường dẫn --%>
						<a href="${pageContext.request.contextPath}/transportationRequest">交通費申請</a>
						<a href="${pageContext.request.contextPath}/businessTripInit">出張費申請</a>
						<a href="${pageContext.request.contextPath}/reimbursementInit">立替金申請</a>
					</div>
				</div>

				<div class="panel">
					<h4>申請状況</h4>
					<ul>
						<%-- Bạn cần sửa các link này để trỏ đến Servlet xử lý, không phải file jsp --%>
						<li><a
							href="${pageContext.request.contextPath}/approvalList?status=not_submitted">未提出:</a></li>
						<li><a
							href="${pageContext.request.contextPath}/approvalList?status=submitted">提出済:</a></li>
						<li><a
							href="${pageContext.request.contextPath}/approvalList?status=rejected">差戻:</a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>

</body>
</html>