<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>出張費申請 - 交通費明細</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
  <style>
    .remove-btn {
      position: absolute;
      top: 1px;
      right: 1px;
      background: none;
      border: none;
      font-size: 1.2rem;
      color: #888;
      cursor: pointer;
    }
    .d-none {
      display: none;
    }
  </style>
  <script>
    function addTransBlock() {
      const container = document.getElementById("trans-container");
      const template = document.querySelector(".trans-block");
      const clone = template.cloneNode(true);
      clone.querySelectorAll("input, textarea, select").forEach(el => el.value = "");
      clone.querySelector(".remove-btn").classList.remove("d-none");
      container.appendChild(clone);
    }

    function removeBlock(btn) {
      const block = btn.closest(".trans-block");
      const allBlocks = document.querySelectorAll(".trans-block");
      if (allBlocks.length > 1) {
        block.remove();
      } else {
        alert("最低1つの明細が必要です");
      }
    }

    function calcFareTotal(elem) {
      const block = elem.closest('.trans-block');
      const amountInput = block.querySelector("input[name='fareAmount[]']") || block.querySelector("input[name='expenseTotal[]']");
      const type = block.querySelector("select[name='transTripType[]']").value;
      const burden = block.querySelector("select[name='burden[]']").value;
      const totalInput = block.querySelectorAll("input[name='expenseTotal[]']")[1] || block.querySelector("input[name='expenseTotal[]']");

      const amount = parseInt(amountInput.value || 0);

      let total = 0;
      if (burden === "自己") {
        const multiplier = (type === "往復") ? 2 : 1;
        total = amount * multiplier;
      }

      totalInput.value = total;
    }
  </script>
</head>
<body>
  <div class="page-container">
    <h2>出張費申請 - 交通費明細</h2>

    <form action="businessTripConfirm.jsp" method="post" enctype="multipart/form-data">
      <input type="hidden" name="startDateHidden" value="<%= request.getParameter("startDateHidden") %>">
      <input type="hidden" name="endDateHidden" value="<%= request.getParameter("endDateHidden") %>">
      <input type="hidden" name="totalDays" value="<%= request.getParameter("totalDays") %>">

      <div style="display: flex; flex-direction: column; gap: 10px" id="trans-container">
        <div class="form-section trans-block">
          <button type="button" class="remove-btn d-none" onclick="removeBlock(this)">×</button>

          <div class="form-group">
            <label>訪問先</label>
            <input type="text" name="transProject[]" placeholder="例:株式会社AAA" required>
          </div>

          <div class="form-group">
            <label>出発</label>
            <input type="text" name="departure[]" placeholder="例:東京" required>
          </div>

          <div class="form-group">
            <label>到着</label>
            <input type="text" name="arrival[]" placeholder="例:大阪" required>
          </div>

          <div class="form-group">
            <label>交通機関</label>
            <select name="transport[]">
              <option value="">選択してください</option>
              <option value="新幹線">新幹線</option>
              <option value="電車">電車</option>
              <option value="タクシー">タクシー</option>
              <option value="飛行機">飛行機</option>
              <option value="自家用車">自家用車</option>
              <option value="レンタカー">レンタカー</option>
              <option value="他の">他の</option>
            </select>
          </div>

          <div class="form-group">
            <label>金額（税込）</label>
            <input type="number" name="fareAmount[]" step="100" onchange="calcFareTotal(this)">
          </div>

          <div class="form-group">
            <label>区分</label>
            <select name="transTripType[]" onchange="calcFareTotal(this)">
              <option value="">選択してください</option>
              <option value="片道">片道</option>
              <option value="往復">往復</option>
            </select>
          </div>

          <div class="form-group">
            <label>負担者</label>
            <select name="burden[]" onchange="calcFareTotal(this)">
              <option value="">選択してください</option>
              <option value="会社">会社</option>
              <option value="自己">自己</option>
            </select>
          </div>

          <div class="form-group">
            <label>合計</label>
            <input type="number" name="expenseTotal[]" readonly>
          </div>

          <div class="form-group">
            <label>摘要</label>
            <textarea name="transMemo[]" placeholder="メモなど"></textarea>
          </div>

          <div class="form-group">
            <label>領収書添付（交通費）</label>
            <input type="file" name="receiptTrans[]" multiple>
          </div>

          <div style="text-align: center;">
            <button type="button" class="plus-btn" onclick="addTransBlock()">＋</button>
          </div>
        </div>
      </div>

      <div class="btn-section">
        <button type="button" onclick="window.location.href='businessTrip2.jsp'">戻る</button>
        <button type="submit">確認</button>
      </div>
    </form>
  </div>

  <div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>
</body>
</html>