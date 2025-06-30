<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.Application" %>
<%@ page import="java.util.List" %>
<%
  List<Application> applications = (List<Application>) request.getAttribute("applications");
%>

<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>申請一覧 - ABC株式会社</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <script src="<%= request.getContextPath() %>/static/js/script.js"></script>
  <style>
    .page-container {
    display: flex;
    gap: 50px;
    align-items: flex-start;
    }
    form { display: flex; flex-direction: column; }
    .table-area { flex: 1; padding-bottom: 1rem; }
    .btn-section {
      margin-top: auto;
      padding-top: 1.5rem;
      display: flex;
      justify-content: center;
      gap: 1rem;
    }
    .modal {
      position: fixed;
      top: 0; left: 0; right: 0; bottom: 0;
      background-color: rgba(0,0,0,0.4);
      display: flex;
      align-items: center;
      justify-content: center;
      z-index: 9999;
    }
    .modal.hidden { display: none; }
    .modal-content {
      background-color: #fff;
      padding: 2rem;
      border-radius: 8px;
      min-width: 300px;
      text-align: center;
    }
    .modal-buttons {
      margin-top: 1.5rem;
      display: flex;
      justify-content: center;
      gap: 2rem;
    }
    .custom-message {
	  margin: 10px auto;
	  width: fit-content;
	  padding: 10px 20px;
	  border-radius: 6px;
	  font-weight: bold;
	  text-align: center;
	}
	
	.custom-message.error {
	  background-color: #ffe5e5;
	  color: #d32f2f;
	  border: 1px solid #d32f2f;
	}
	
	.custom-message.success {
	  background-color: #e0f7fa;
	  color: #00796b;
	  border: 1px solid #00796b;
	}
  </style>
</head>
<body>
  <div class="page-container" style="align-items: center">
  	<!-- Sidebar -->
   <jsp:include page="/WEB-INF/views/common/sidebar.jsp" /> 
    
    <div>
    <h2>申請一覧</h2>

    <!-- <div class="action-toolbar">
      <div class="spacer"></div>
      <button id="editBtn" disabled>編集</button>
      <button id="deleteBtn" disabled>削除</button>
    </div> -->
    <!-- あとで作成する -->

    <form action="submitApplication" method="post">
      <div class="table-area">
        <table id="applicationTable">
          <thead>
            <tr>
              <th ><div></div><input type="checkbox" id="selectAll"></th>
              <th>申請ID</th>
              <th>申請種別</th>
              <th>申請時間</th>
              <th>金額（含税）</th>
              <th>
                <select id="statusFilter" class="status-filter-button">
                	<option value="">状況</option>
					<option value="未提出">未提出</option>
					<option value="提出済み">提出済み</option>
					<option value="差戻し">差戻し</option>
					<option value="承認済">承認済</option>
					<option value="支払い済">支払い済</option>
                </select>
              </th>
            </tr>
          </thead>
          <tbody>
            <% for (Application app : applications) { %>
              <tr class="clickable-row" data-id="<%= app.getApplicationId() %>" data-status="<%= app.getStatus() %>">
                <td><input type="checkbox" class="row-check" name="appIds" value="<%= app.getApplicationId() %>"></td>
                <td><%= app.getApplicationId() %></td>
                <td><%= app.getApplicationType() %></td>
                <td><%= app.getApplicationDate().toLocalDateTime().toString().replace('T', ' ') %></td>
                <td><%= String.format("%,d円", app.getAmount()) %></td>
                <td><%= app.getStatus() %></td>
              </tr>
            <% } %>
          </tbody>
        </table>
      </div>
      　<% Boolean submitted = (Boolean) request.getAttribute("submitSuccess"); %>
		<% if (submitted != null && submitted) { %>
		  <div style="color: rgb(127, 15, 59); margin: 0 auto;">
		    提出が完了しました。
		  </div>
		<% } %>
      <div class="btn-section">
        <%
		  String position = (String) request.getAttribute("position");
		  String menuPath = "staffMenu";
		  if ("部長".equals(position)) {
		    menuPath = "buchouMenu";
		  } else if ("管理部".equals(position)) {
		    menuPath = "manageMenu";
		  }
		%>
		<button type="button" onclick="location.href='<%= request.getContextPath() %>/menu'">戻る</button>
        <button type="submit" onclick="return confirmSubmit()">提出</button>
      </div>
    </form>
  </div>
    <%
	  String msg = (String) session.getAttribute("message");
	  if (msg != null) {
	%>
	  <div class="custom-message error">
	    <%= msg %>
	  </div>
	<%
	    session.removeAttribute("message"); // tránh hiển thị lại sau refresh
	  }
	%>
  <!-- 提出確認モーダル -->
  <div id="submitModal" class="modal hidden">
    <div class="modal-content">
      <p>選択された申請を提出しますか？</p>
      <div class="modal-buttons">
        <button type="button" onclick="closeModal()">キャンセル</button>
        <button type="button" onclick="submitForm()">提出</button>
      </div>
    </div>
  </div>
  </div>
  <div class="footer">
    &copy; 2025 ABC株式会社 - All rights reserved.
  </div>

 <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

	<script>
	  const checkboxes = document.querySelectorAll('.row-check');
	  const editBtn = document.getElementById('editBtn');
	  const deleteBtn = document.getElementById('deleteBtn');
	  const modal = document.getElementById('submitModal');
	
	  document.getElementById('selectAll').addEventListener('change', function () {
	    checkboxes.forEach(cb => cb.checked = this.checked);
	    updateToolbarState();
	  });
	
	  checkboxes.forEach(cb => cb.addEventListener('change', updateToolbarState));
	
	  function updateToolbarState() {
	    const checked = document.querySelectorAll('.row-check:checked');
	    editBtn.disabled = (checked.length !== 1);
	    deleteBtn.disabled = (checked.length === 0);
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
		    const targetCell = e.target.closest('td');

		    
		    if (targetCell && targetCell.cellIndex === 0) {
		      return;
		    }

		    
		    const id = this.dataset.id;
		    window.location.href = 'applicationDetail?id=' + id;
		  });
		});
	
	  function confirmSubmit() {
	    const checked = document.querySelectorAll('.row-check:checked');
	    if (checked.length === 0) {
	      Swal.fire({
	        title: '注意',
	        text: '提出する申請を選択してください。',
	        confirmButtonText: 'OK',
	        confirmButtonColor: '#00a1bb'
	      });
	      return false;
	    }
	
	    for (const cb of checked) {
	      const row = cb.closest('tr');
	      const status = row.getAttribute('data-status');
	      if (status !== '未提出') {
	        Swal.fire({
	          title: '注意',
	          text: '未提出の申請のみ提出可能です。',
	          confirmButtonText: 'OK',
	          confirmButtonColor: '#00a1bb'
	        });
	        return false;
	      }
	    }
	
	    modal.classList.remove('hidden');
	    return false;
	  }
	
	  function closeModal() {
	    modal.classList.add('hidden');
	  }
	
	  function submitForm() {
	    document.querySelector('form').submit();
	  }
	
	  window.addEventListener("pageshow", function () {
	    document.querySelectorAll('.row-check').forEach(cb => cb.checked = false);
	    document.getElementById('submitModal').classList.add('hidden');
	    updateToolbarState();
	  });
	</script>
	
	<script>
	<% if (request.getAttribute("message") != null) { %>
	  Swal.fire({
	    icon: 'warning',
	    title: '注意',
	    text: '<%= request.getAttribute("message") %>',
	    confirmButtonText: 'OK'
	  });
	<% } %>
	</script>
</body>
</html>