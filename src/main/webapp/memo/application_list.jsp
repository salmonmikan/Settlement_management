<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>申請一覧 - ABC株式会社</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
  <style>
    html, body {
      height: 100%;
    }
    body {
      display: flex;
      flex-direction: column;
      min-height: 100vh;
    }
    .page-container {
      flex: 1;
      display: flex;
      flex-direction: column;
    }
    form {
      flex: 1;
      display: flex;
      flex-direction: column;
    }
    .table-area {
      flex: 1;
      min-height: 400px;
    }
    .action-toolbar {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      position: sticky;
      top: 0;
      z-index: 100;
      margin-bottom: -10px;
      background: var(--bg-color);
    }
    .action-toolbar button {
      background: var(--primary-color);
      color: var(--primary-color);
      border: 1px solid var(--primary-color);
      padding: 0.2rem 1rem;
      border-radius: 4px;
      font-size: 0.9rem;
      cursor: pointer;
      transition: background 0.2s, color 0.2s;
    }
    .action-toolbar button:disabled {
      background: #fff;
      cursor: not-allowed;
      color: var(--primary-color);
    }
    .action-toolbar button:not(:disabled) {
      color: white;
    }
    .action-toolbar .spacer {
      flex: 1;
    }
    .status-filter-button {
      font-weight: bold;
      font-size: 0.9rem;
      color: var(--text-color);
      cursor: pointer;
      padding: 0.3rem 1rem 0.3rem 0.5rem;
      background: transparent;
      border: none;
      appearance: none;
      background-image: url("data:image/svg+xml;charset=US-ASCII,%3Csvg%20width%3D'10'%20height%3D'6'%20viewBox%3D'0%200%2010%206'%20xmlns%3D'http%3A//www.w3.org/2000/svg'%3E%3Cpath%20d%3D'M0%200l5%206%205-6z'%20fill%3D'%23333'/%3E%3C/svg%3E");
      background-repeat: no-repeat;
      background-position: right 0.4rem center;
      background-size: 10px 6px;
    }
  </style>
</head>
<body>
  <div class="page-container">
    <h2>申請一覧</h2>
    <!-- <p style="text-align: center; font-size: 0.9rem; color: #777;">
      ※ 各月分の申請は、翌月10日までに提出してください。
    </p> -->

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
              <th>
	              <div>All </div>
	              <input type="checkbox" id="selectAll">
              </th>
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
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr data-status="未提出">
              <td><input type="checkbox" class="row-check" name="appIds" value="101"></td>
              <td>101</td>
              <td>出張費</td>
              <td>2025/05/07 11:43:26</td>
              <td>28,000円</td>
              <td>未提出</td>
              <td class="btn-group"><a href="#">詳細</a></td>
            </tr>
            <tr data-status="提出済">
              <td><input type="checkbox" class="row-check" name="appIds" value="102"></td>
              <td>102</td>
              <td>交通費</td>
              <td>2025/05/14 12:41:54</td>
              <td>1,200円</td>
              <td>提出済</td>
              <td class="btn-group"><a href="#">詳細</a></td>
            </tr>
            <tr data-status="差戻し">
              <td><input type="checkbox" class="row-check" name="appIds" value="103"></td>
              <td>103</td>
              <td>出張費</td>
              <td>2025/05/21 11:43:26</td>
              <td>15,500円</td>
              <td class="status-returned">差戻し</td>
              <td class="btn-group"><a href="#">詳細</a></td>
            </tr>
            <tr data-status="承認済">
              <td><input type="checkbox" class="row-check" name="appIds" value="104"></td>
              <td>104</td>
              <td>立替金</td>
              <td>2025/05/24 11:43:26</td>
              <td>12,000円</td>
              <td>承認済</td>
              <td class="btn-group"><a href="#">詳細</a></td>
            </tr>
            <tr data-status="未提出">
              <td><input type="checkbox" class="row-check" name="appIds" value="101"></td>
              <td>101</td>
              <td>出張費</td>
              <td>2025/05/07 11:43:26</td>
              <td>28,000円</td>
              <td>未提出</td>
              <td class="btn-group"><a href="#">詳細</a></td>
            </tr>
            <tr data-status="提出済">
              <td><input type="checkbox" class="row-check" name="appIds" value="102"></td>
              <td>102</td>
              <td>交通費</td>
              <td>2025/05/14 12:41:54</td>
              <td>1,200円</td>
              <td>提出済</td>
              <td class="btn-group"><a href="#">詳細</a></td>
            </tr>
            <tr data-status="差戻し">
              <td><input type="checkbox" class="row-check" name="appIds" value="103"></td>
              <td>103</td>
              <td>出張費</td>
              <td>2025/05/21 11:43:26</td>
              <td>15,500円</td>
              <td class="status-returned">差戻し</td>
              <td class="btn-group"><a href="#">詳細</a></td>
            </tr>
            <tr data-status="承認済">
              <td><input type="checkbox" class="row-check" name="appIds" value="104"></td>
              <td>104</td>
              <td>立替金</td>
              <td>2025/05/24 11:43:26</td>
              <td>12,000円</td>
              <td>承認済</td>
              <td class="btn-group"><a href="#">詳細</a></td>
            </tr>
            <tr data-status="未提出">
              <td><input type="checkbox" class="row-check" name="appIds" value="101"></td>
              <td>101</td>
              <td>出張費</td>
              <td>2025/05/07 11:43:26</td>
              <td>28,000円</td>
              <td>未提出</td>
              <td class="btn-group"><a href="#">詳細</a></td>
            </tr>
            <tr data-status="提出済">
              <td><input type="checkbox" class="row-check" name="appIds" value="102"></td>
              <td>102</td>
              <td>交通費</td>
              <td>2025/05/14 12:41:54</td>
              <td>1,200円</td>
              <td>提出済</td>
              <td class="btn-group"><a href="#">詳細</a></td>
            </tr>
            <tr data-status="差戻し">
              <td><input type="checkbox" class="row-check" name="appIds" value="103"></td>
              <td>103</td>
              <td>出張費</td>
              <td>2025/05/21 11:43:26</td>
              <td>15,500円</td>
              <td class="status-returned">差戻し</td>
              <td class="btn-group"><a href="#">詳細</a></td>
            </tr>
            <tr data-status="承認済">
              <td><input type="checkbox" class="row-check" name="appIds" value="104"></td>
              <td>104</td>
              <td>立替金</td>
              <td>2025/05/24 11:43:26</td>
              <td>12,000円</td>
              <td>承認済</td>
              <td class="btn-group"><a href="#">詳細</a></td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="btn-section">
        <button type="submit">戻る</button>
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
  </script>
</body>
</html>