<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="model.Application"%>
<%@ page import="java.util.List"%>
<%
List<Application> applications = (List<Application>) request.getAttribute("applications");
String mode = (String) request.getAttribute("mode");
if (mode == null)
	mode = "approver"; // default
%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>承認一覧 - ABC株式会社</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/static/css/style.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" />
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body data-mode="<%=mode%>">
	<jsp:include page="/WEB-INF/views/common/header.jsp" />
	<div class="page-container">
		<jsp:include page="/WEB-INF/views/common/sidebar.jsp" />
		<div class="content-container">
				<h2><%="approver".equals(mode) ? "承認一覧" : "管理部：承認済み一覧"%></h2>

				<form action="#" method="post">
					<div class="table-area">
						<table id="applicationTable">
							<thead>
								<tr>
									<th><div>選択</div>
										<input type="checkbox" id="selectAll"></th>
									<th>申請ID</th>
									<th>社員ID</th>
									<th>氏名</th>
									<th>申請種別</th>
									<th>申請時間</th>
									<th>金額（税込）</th>
									<th><select id="statusFilter" class="status-filter-button">
											<option value="">ステータス</option>
											<option value="未提出">未提出</option>
											<option value="提出済み">提出済み</option>
											<option value="差戻し">差戻し</option>
											<option value="承認済み">承認済み</option>
											<option value="支払済み">支払済み</option>
									</select></th>
								</tr>
							</thead>
							<tbody>
								<%
								for (Application app : applications) {
								%>
								<tr class="clickable-row" data-id="<%=app.getApplicationId()%>"
									data-status="<%=app.getStatus()%>">
									<td><input type="checkbox" class="row-check" name="appIds"
										value="<%=app.getApplicationId()%>"></td>
									<td><%=app.getApplicationId()%></td>
									<td><%=app.getStaffId()%></td>
									<td><%=app.getStaffName()%></td>
									<td><%=app.getApplicationType()%></td>
									<td><%=app.getApplicationDate().toLocalDateTime().toString().replace('T', ' ')%></td>
									<td><%=String.format("%,d円", app.getAmount())%></td>
									<td><%=app.getStatus()%></td>
								</tr>
								<%
								}
								%>
							</tbody>
						</table>
					</div>
					<div class="btn-section">
						<button type="submit">提出</button>
					</div>
				</form>
			</div>

			<%
			String msg = (String) session.getAttribute("message");
			if (msg != null) {
			%>
			<div class="custom-message error">
				<%=msg%>
			</div>
			<%
			session.removeAttribute("message");
			%>
			<%
			}
			%>
		</div>
	</div>

	<div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>

	<script>
	const checkboxes = document.querySelectorAll('.row-check');
	
	document.getElementById('selectAll').addEventListener('change', function () {
	    checkboxes.forEach(cb => cb.checked = this.checked);
	    updateToolbarState();
	  });
	  
    document.getElementById('statusFilter').addEventListener('change', function () {
      const selected = this.value;
      const rows = document.querySelectorAll('#applicationTable tbody tr');
      rows.forEach(row => {
        const status = row.getAttribute('data-status');
        row.style.display = (!selected || selected === status) ? '' : 'none';
      });
    });

    document.querySelectorAll('.clickable-row').forEach(row => {
      row.addEventListener('click', function (e) {
        if (e.target.tagName === 'INPUT') return;
        const id = this.dataset.id;
        window.location.href = 'applicationDetail?id=' + id;
      });
    });

    <%if (request.getAttribute("message") != null) {%>
    Swal.fire({
      icon: 'warning',
      title: '注意',
      text: '<%=request.getAttribute("message")%>',
      confirmButtonText: 'OK'
    });
    <%}%>
  </script>
</body>
</html>
