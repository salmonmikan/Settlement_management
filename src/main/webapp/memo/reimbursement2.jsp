<%@ page contentType="text/html; charset=UTF-8" %>
<%
  String startDate = request.getParameter("startDateHidden");
  String endDate = request.getParameter("endDateHidden");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>立替金精算書</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <script src="<%= request.getContextPath() %>/static/js/script.js"></script>
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
  </style>
</head>
<body>
  <div class="page-container">
    <h2>立替金精算書</h2>

    <form action="<%= request.getContextPath() %>/advancePayment" method="post" enctype="multipart/form-data">
      <!-- ✅ step input đặt đúng vị trí -->
      <input type="hidden" name="step" value="2">

      <div style="display: flex; flex-direction: column; gap: 10px" id="allowance-container">
        <div class="form-section allowance-block" style="position: relative;">
          <button type="button" class="remove-btn" onclick="removeBlock(this)">×</button>
          <div class="form-group">
            <label>勘定科目</label>
            <textarea name="memo[]" placeholder="入力してください。"></textarea>
          </div>

          <div class="form-group">
            <label>摘要</label>
            <textarea name="memo[]" placeholder="メモなど"></textarea>
          </div>
          
          <div class="form-group">
            <label>金額</label>
            <input type="number" name="expenseTotal[]" readonly>
          </div>
          
          <div class="form-group">
            <label>領収書添付</label>
            <input type="file" name="receiptDaily[]" multiple class="fileInput">
            <small style="color: gray;">(Ctrlキーを押しながら複数ファイルを選択するか、または一つずつ追加して一括送信可)</small>
            <ul class="fileList"></ul>
          </div>

          <div style="text-align: center;">
            <button type="button" class="plus-btn" onclick="addAllowanceBlock()">＋</button>
          </div>
        </div>
      </div>

      <div class="btn-section">
        <button type="button" onclick="window.location.href='<%= request.getContextPath() %>/advancePaymentStep1Back'">戻る</button>
        <button type="submit">次へ</button>
      </div>
    </form>
  </div>

  <div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>

  <script>
    function addAllowanceBlock() {
      const container = document.getElementById("allowance-container");
      const template = document.querySelector(".allowance-block");
      const clone = template.cloneNode(true);

      // Clear all input/select values
      clone.querySelectorAll("input, textarea").forEach(el => el.value = "");
      clone.querySelectorAll("select").forEach(sel => sel.selectedIndex = 0);

      // Reset event listeners for file inputs
      const fileInput = clone.querySelector(".fileInput");
      const fileList = clone.querySelector(".fileList");
      fileInput.addEventListener("change", function(e) {
        const newFiles = Array.from(e.target.files);
        fileList.innerHTML = "";
        newFiles.forEach(file => {
          const li = document.createElement("li");
          li.textContent = file.name;
          fileList.appendChild(li);
        });
        e.target.value = "";
      });

      container.appendChild(clone);
    }

    function removeBlock(btn) {
      const blocks = document.querySelectorAll(".allowance-block");
      if (blocks.length > 1) {
        btn.closest(".allowance-block").remove();
      } else {
        alert("最低1つの明細が必要です");
      }
    }

    window.onload = function() {
      const firstBlock = document.querySelector(".allowance-block");
      if (firstBlock) {
        const daysInput = firstBlock.querySelector("input[name='days[]']");
        if (daysInput.value === "" || parseInt(daysInput.value) <= 1) {
          daysInput.value = diffDays;
        }
        updateAllowanceAndHotel(firstBlock.querySelector("select[name='regionType[]']"));
      }
    };
  </script>
</body>
</html>