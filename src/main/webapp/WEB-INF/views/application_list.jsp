<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="bean.Application" %>
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
    .page-container { display: flex; flex-direction: column; flex: 1; }
    form { display: flex; flex-direction: column; }
    .table-area { flex: 1; padding-bottom: 1rem; }
    .btn-section {
      margin-top: auto;
      padding-top: 1.5rem;
      display: flex;
      justify-content: center;
      gap: 1rem;
    }
  </style>
</head>
<body>
  <div class="page-container">
    <h2>申請一覧</h2>

    <div class="action-toolbar">
      <div class="spacer"></div>
      <button id="editBtn" disabled>編集</button>
      <button id="deleteBtn" disabled>削除</button>
    </div>

    <form action="submitApplication" method="post">
      <div class="table-area">
        <table id="applicationTable">
          <thead>
            <tr>
              <th><div>All</div><input type="checkbox" id="selectAll"></th>
              <th>申請ID</th>
              <th>申請種別</th>
              <th>申請時間</th>
              <th>金額（税込）</th>
              <th>
                <select id="statusFilter" class="status-filter-button">
                  <option value="">すべて</option>
                  <option value="未提出">未提出</option>
                  <option value="提出済">提出済</option>
                  <option value="差戻し">差戻し</option>
                  <option value="承認済">承認済</option>
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
      <div class="btn-section">
        <button type="button" onclick="history.back()">戻る</button>
        <button type="submit">提出</button>
      </div>
    </form>
  </div>

  <div class="footer">
    &copy; 2025 ABC株式会社 - All rights reserved.
  </div>

  <script>
    const checkboxes = document.querySelectorAll('.row-check');
    const editBtn = document.getElementById('editBtn');
    const deleteBtn = document.getElementById('deleteBtn');

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
        if (e.target.tagName === 'INPUT') return;
        const id = this.dataset.id;
        window.location.href = `detail.jsp?id=${id}`;
      });
    });
  </script>
</body>
</html>