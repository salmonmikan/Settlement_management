<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>申請一覧 - ABC株式会社</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
  <style>
  	/* application_list layout fix */
	.page-container {
	  display: flex;
	  flex-direction: column;
	  flex: 1;
	}
	
	form {
	  display: flex;
	  flex-direction: column;
	}
	
	.table-area {
	  flex: 1;
	  padding-bottom: 1rem;
	}
	
	.btn-section {
	  margin-top: auto; /* đẩy nút xuống nếu còn trống */
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
            <tr class="clickable-row" data-id="101" data-status="未提出">
              <td><input type="checkbox" class="row-check" name="appIds" value="101"></td>
              <td>101</td><td>出張費</td><td>2025/05/07 11:43:26</td><td>28,000円</td><td>未提出</td>
            </tr>
            <tr class="clickable-row" data-id="102" data-status="提出済">
              <td><input type="checkbox" class="row-check" name="appIds" value="102"></td>
              <td>102</td><td>交通費</td><td>2025/05/14 12:41:54</td><td>1,200円</td><td>提出済</td>
            </tr>
            <tr class="clickable-row" data-id="103" data-status="差戻し">
              <td><input type="checkbox" class="row-check" name="appIds" value="103"></td>
              <td>103</td><td>出張費</td><td>2025/05/21 11:43:26</td><td>15,500円</td><td class="status-returned">差戻し</td>
            </tr>
            <tr class="clickable-row" data-id="104" data-status="承認済">
              <td><input type="checkbox" class="row-check" name="appIds" value="104"></td>
              <td>104</td><td>立換金</td><td>2025/05/24 11:43:26</td><td>12,000円</td><td>承認済</td>
            </tr>
            <tr class="clickable-row" data-id="101" data-status="未提出">
              <td><input type="checkbox" class="row-check" name="appIds" value="101"></td>
              <td>101</td><td>出張費</td><td>2025/05/07 11:43:26</td><td>28,000円</td><td>未提出</td>
            </tr>
            <tr class="clickable-row" data-id="102" data-status="提出済">
              <td><input type="checkbox" class="row-check" name="appIds" value="102"></td>
              <td>102</td><td>交通費</td><td>2025/05/14 12:41:54</td><td>1,200円</td><td>提出済</td>
            </tr>
            <tr class="clickable-row" data-id="103" data-status="差戻し">
              <td><input type="checkbox" class="row-check" name="appIds" value="103"></td>
              <td>103</td><td>出張費</td><td>2025/05/21 11:43:26</td><td>15,500円</td><td class="status-returned">差戻し</td>
            </tr>
            <tr class="clickable-row" data-id="104" data-status="承認済">
              <td><input type="checkbox" class="row-check" name="appIds" value="104"></td>
              <td>104</td><td>立換金</td><td>2025/05/24 11:43:26</td><td>12,000円</td><td>承認済</td>
            </tr>
            <tr class="clickable-row" data-id="101" data-status="未提出">
              <td><input type="checkbox" class="row-check" name="appIds" value="101"></td>
              <td>101</td><td>出張費</td><td>2025/05/07 11:43:26</td><td>28,000円</td><td>未提出</td>
            </tr>
            <tr class="clickable-row" data-id="102" data-status="提出済">
              <td><input type="checkbox" class="row-check" name="appIds" value="102"></td>
              <td>102</td><td>交通費</td><td>2025/05/14 12:41:54</td><td>1,200円</td><td>提出済</td>
            </tr>
            <tr class="clickable-row" data-id="103" data-status="差戻し">
              <td><input type="checkbox" class="row-check" name="appIds" value="103"></td>
              <td>103</td><td>出張費</td><td>2025/05/21 11:43:26</td><td>15,500円</td><td class="status-returned">差戻し</td>
            </tr>
            <tr class="clickable-row" data-id="104" data-status="承認済">
              <td><input type="checkbox" class="row-check" name="appIds" value="104"></td>
              <td>104</td><td>立換金</td><td>2025/05/24 11:43:26</td><td>12,000円</td><td>承認済</td>
            </tr>
            
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