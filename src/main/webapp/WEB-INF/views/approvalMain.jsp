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


			<form action="approverApplications" method="post">
				<div class="action-toolbar">
					<div class="spacer"></div>
					<input type="text" id="staffSearchInput" placeholder="社員IDで検索"
						style="padding: 6px; border: 1px solid #ccc; border-radius: 4px;">
					<button type="submit" name="action" value="reject" id="rejectBtn"
						disabled onclick="">差戻</button>
					<!--					<button type="submit" name="action" value="delete" id="deleteBtn" disabled onclick="return confirm('本当に削除しますか？')">削除</button>-->
					<button type="submit" name="action" value="approval"
						id="applovalBtn" disabled>承認</button>
				</div>
				<div class="table-area">
					<table id="applicationTable">
						<thead>
							<tr>
								<th><div>選択</div> <input type="checkbox" id="selectAll"></th>
								<th>申請ID</th>
								<th>社員ID</th>
								<th>社員名</th>
								<th>申請種別</th>
								<th>申請時間</th>
								<th>金額（税込）</th>
								<th><select id="statusFilter" class="status-filter-button">
										<option value="">申請ステータス</option>
										<!--										<option value="未提出">未提出</option>-->
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
								data-status="<%=app.getStatus()%>"
								data-staff-id="<%=app.getStaffId()%>">
								<td><input type="checkbox" class="row-check" name="appIds"
									value="<%=app.getApplicationId()%>"></td>
								<td><%=app.getApplicationId()%></td>
								<td><%=app.getStaffId()%></td>
								<td><%=app.getStaffName()%></td>
								<td><%=app.getApplicationType()%></td>
								<td><%=app.getCreatedAt().toLocalDateTime().toString().replace('T', ' ')%></td>
								<td><%=String.format("%,d円", app.getAmount())%></td>
								<td><%=app.getStatus()%></td>
							</tr>
							<%
							}
							%>
						</tbody>
					</table>
				</div>
			</form>

			<%
			String errorMsg = (String) session.getAttribute("errorMsg");
			if (errorMsg != null) {
			%>
			<div class="custom-message error">
				<%=errorMsg%>
			</div>
			<%
			session.removeAttribute("errorMsg");
			%>
			<%
			}
			%>

			<%
			String success = (String) session.getAttribute("success");
			if (success != null) {
			%>
			<script>
    alert(<%="\"" + success.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "") + "\""%>);
</script>
			<%
			session.removeAttribute("success");
			}
			%>
		</div>
	</div>

	<div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>

	<script>
	const checkboxes = document.querySelectorAll('.row-check');
    const rejectBtn = document.getElementById('rejectBtn');
    const approvalBtn = document.getElementById('approvalBtn');
	
	document.getElementById('selectAll').addEventListener('change', function () {
	    checkboxes.forEach(cb => cb.checked = this.checked);
	    updateToolbarState();
	  });

	// チェックボックスを常時監視
	checkboxes.forEach(cb => cb.addEventListener('change', updateToolbarState));
    function updateToolbarState() {
        const checked = document.querySelectorAll('.row-check:checked');
        rejectBtn.disabled = (checked.length === 0);
        applovalBtn.disabled = (checked.length === 0);
      }
	  
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

 	// 社員IDでの検索用
	document.getElementById('staffSearchInput').addEventListener('input', function () {
	    const keyword = this.value.trim();
	    document.querySelectorAll('#applicationTable tbody tr').forEach(row => {
	      const staffId = row.getAttribute('data-staff-id');
	      row.style.display = (!keyword || staffId.includes(keyword)) ? '' : 'none';
	    });
	  });
  </script>
</body>
</html>
