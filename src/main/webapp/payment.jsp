<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>支払い管理 - ABC株式会社</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/static/css/style.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
<script src="<%=request.getContextPath()%>/static/js/script.js"></script>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp" />
	<div class="page-container">
		<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />
		<div class="content-container">
			<h2>申請一覧</h2>

			<form action="payment" method="post">
				<div class="action-toolbar">
					<div class="spacer"></div>
					<button id="paidBtn" type="submit" disabled>支払済</button>
				</div>

				<div class="table-area">
					<table id="applicationTable">
						<thead>
							<tr>
								<th><div>All</div> <input type="checkbox" id="selectAll"></th>
								<th>申請ID</th>
								<th>申請種別</th>
								<th>申請時間</th>
								<th>金額（税込）</th>
								<th>ステータス</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="p" items="${paymentList}">
								<tr class="clickable-row" data-id="${p.applicationId}">
									<td><input type="checkbox" class="row-check" name="appIds"
										value="${p.applicationId}"></td>
									<td>${p.applicationId}</td>
									<td>${p.applicationType}</td>
									<td>${p.applicationDate}</td>
									<td><fmt:formatNumber value="${p.amount}" type="number"
											pattern="#,##0" />円</td>
									<td>${p.status}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
				<div class="btn-section">
					<button type="button" onclick="history.back()">戻る</button>
				</div>
			</form>
		</div>
	</div>

	<div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>

	<script>
	const checkboxes = document.querySelectorAll('.row-check');
	const paidBtn = document.getElementById('paidBtn');

	document.getElementById('selectAll').addEventListener('change', function () {
		checkboxes.forEach(cb => cb.checked = this.checked);
		updateToolbarState();
	});

	checkboxes.forEach(cb => cb.addEventListener('change', updateToolbarState));

	function updateToolbarState() {
		const checked = document.querySelectorAll('.row-check:checked');
		paidBtn.disabled = (checked.length === 0);
	}

	document.querySelectorAll('.clickable-row').forEach(row => {
		row.addEventListener('click', function (e) {
			if (e.target.tagName === 'INPUT') return;
			const id = this.dataset.id;
			window.location.href = `applicationDetail?id=${id}`;
		});
	});
	</script>
</body>
</html>
