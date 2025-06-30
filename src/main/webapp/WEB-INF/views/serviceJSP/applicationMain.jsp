<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="model.Application"%>
<%@ page import="java.util.List"%>
<%
  List<Application> applications = (List<Application>) request.getAttribute("applications");
%>

<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>申請一覧 - ABC株式会社</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css" />
<script src="<%= request.getContextPath() %>/static/js/script.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/header.jsp" />
<div class="page-container">
    <jsp:include page="/WEB-INF/views/common/sidebar.jsp" />
    <div class="content-container">
        <h2>申請一覧</h2>

    <form action="submitApplication" method="post">
      <div class="table-area">
        <table id="applicationTable">
          <thead>
            <tr>
              <th><input type="checkbox" id="selectAll"></th>
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
                <td><%= app.getCreatedAt().toLocalDateTime().toString().replace('T', ' ') %></td>
                <td><%= String.format("%,d円", app.getAmount()) %></td>
                <td><%= app.getStatus() %></td>
              </tr>
            <% } %>
          </tbody>
        </table>
      </div>

      <% Boolean submitted = (Boolean) request.getAttribute("submitSuccess"); %>
      <% if (submitted != null && submitted) { %>
          <div style="color: rgb(127, 15, 59); margin: 0 auto;">提出が完了しました。</div>
      <% } %>

      <div class="btn-section">
        <% String position = (String) request.getAttribute("position"); %>
        <button type="submit" onclick="return confirmSubmit()">提出</button>
      </div>
    </form>
  </div>

  <% String msg = (String) session.getAttribute("message"); %>
  <% if (msg != null) { %>
    <div class="custom-message error"><%= msg %></div>
    <% session.removeAttribute("message"); %>
  <% } %>

  <!-- モーダル -->
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

<div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
const checkboxes = document.querySelectorAll('.row-check');
const modal = document.getElementById('submitModal');

document.getElementById('selectAll').addEventListener('change', function () {
    checkboxes.forEach(cb => cb.checked = this.checked);
});

document.getElementById('statusFilter').addEventListener('change', function () {
    const selected = this.value;
    document.querySelectorAll('#applicationTable tbody tr').forEach(row => {
        const status = row.getAttribute('data-status');
        row.style.display = (!selected || selected === status) ? '' : 'none';
    });
});

document.querySelectorAll('.clickable-row').forEach(row => {
    row.addEventListener('click', function (e) {
        if (e.target.closest('td')?.cellIndex === 0) return;
        window.location.href = 'applicationDetail?id=' + this.dataset.id;
    });
});

function confirmSubmit() {
    const checked = document.querySelectorAll('.row-check:checked');
    if (checked.length === 0) {
        Swal.fire({ title: '注意', text: '提出する申請を選択してください。', confirmButtonText: 'OK', confirmButtonColor: '#00a1bb' });
        return false;
    }
    for (const cb of checked) {
        if (cb.closest('tr').getAttribute('data-status') !== '未提出') {
            Swal.fire({ title: '注意', text: '未提出の申請のみ提出可能です。', confirmButtonText: 'OK', confirmButtonColor: '#00a1bb' });
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

window.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    const filter = urlParams.get("status");
    if (filter === "not_submitted") {
        const statusSelect = document.getElementById("statusFilter");
        statusSelect.value = "未提出";
        statusSelect.dispatchEvent(new Event('change'));
    }else if (filter === "submitted") {
        const statusSelect = document.getElementById("statusFilter");
        statusSelect.value = "提出済み";
        statusSelect.dispatchEvent(new Event('change'));
    }else if (filter === "rejected") {
        const statusSelect = document.getElementById("statusFilter");
        statusSelect.value = "差戻し";
        statusSelect.dispatchEvent(new Event('change'));
    }
});
</script>

<script>
<% if (request.getAttribute("message") != null) { %>
Swal.fire({
    icon: 'warning',
    title: '注意',
    text: '<%=request.getAttribute("message")%>',
    confirmButtonText: 'OK'
});
<% } %>
</script>

</body>
</html>
