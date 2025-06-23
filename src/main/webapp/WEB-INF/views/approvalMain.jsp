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
  <title>承認一覧 - ABC株式会社</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body>
  <div class="page-container">
    <h2>承認一覧</h2>

    <form>
      <div class="table-area">
        <table id="applicationTable">
          <thead>
            <tr>
              <th><div>All</div><input type="checkbox" id="selectAll"></th>
              <th>申請ID</th>
              <th>社員ID</th>
              <th>社員名</th>
              <th>申請種別</th>
              <th>申請時間</th>
              <th>金額（含税）</th>
              <th>
                <select id="statusFilter" class="status-filter-button">
                  <option value="">すべて</option>
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
                <td><%= app.getStaffId() %></td>
                <td><%= app.getStaffName() %></td>
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
        <button type="button" onclick="location.href='<%= request.getContextPath() %>/menu'">戻る</button>
        <button type="button" onclick="approveSelected()">承認</button>
      </div>
    </form>
  </div>

  <script>
    document.getElementById('selectAll').addEventListener('change', function () {
      document.querySelectorAll('.row-check').forEach(cb => cb.checked = this.checked);
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
    	    window.location.href = 'applicationDetail?id=' + id + '&from=approval';
    	  });
    	});

    function approveSelected() {
      const checked = document.querySelectorAll('.row-check:checked');
      if (checked.length === 0) {
        Swal.fire({
          icon: 'warning',
          title: '注意',
          text: '承認する申請を選択してください。',
          confirmButtonText: 'OK'
        });
        return;
      }
      Swal.fire({
        icon: 'success',
        title: 'OK',
        text: '（承認処理仮）選択された申請を承認します。',
        confirmButtonText: '閉じる'
      });
    }
  </script>
</body>
</html>