<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>支払い管理 - ABC株式会社</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <script src="<%= request.getContextPath() %>/static/js/script.js"></script>
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
	
	td:nth-child(5) {
  		text-align: right;
	}
	
  </style>
</head>
<body>
  <div class="page-container">
    <h2>申請一覧</h2>

    <div class="action-toolbar">
      <div class="spacer"></div>
      <button id="paidBtn" disabled>支払済</button>
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
              <th>ステータス</th>
            </tr>
          </thead>
          <tbody>
            <tr class="clickable-row" data-id="102">
    <td><input type="checkbox" class="row-check" name="appIds" value="102"></td>
    <td>102</td><td>交通費</td><td>2025/05/14 12:41:54</td><td>1,200円</td><td>未支払い</td>
  </tr>
  <tr class="clickable-row" data-id="104">
    <td><input type="checkbox" class="row-check" name="appIds" value="104"></td>
    <td>104</td><td>立換金</td><td>2025/05/24 11:43:26</td><td>12,000円</td><td>未支払い</td>
  </tr>
  <tr class="clickable-row" data-id="102">
    <td><input type="checkbox" class="row-check" name="appIds" value="102"></td>
    <td>102</td><td>交通費</td><td>2025/05/14 12:41:54</td><td>1,200円</td><td>未支払い</td>
  </tr>
  <tr class="clickable-row" data-id="104">
    <td><input type="checkbox" class="row-check" name="appIds" value="104"></td>
    <td>104</td><td>立換金</td><td>2025/05/24 11:43:26</td><td>12,000円</td><td>未支払い</td>
  </tr>
          </tbody>
        </table>
      </div>
      <div class="btn-section">
        <button type="button" onclick="history.back()">戻る</button>
      </div>
    </form>
  </div>

  <div class="footer">
    &copy; 2025 ABC株式会社 - All rights reserved.
  </div>

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
        window.location.href = `detail.jsp?id=${id}`;
      });
    });
  </script>
</body>
</html>