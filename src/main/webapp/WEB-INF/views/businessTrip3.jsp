<%@ page contentType="text/html; charset=UTF-8" %>
<%
String staffName = (String) session.getAttribute("staffName");
if (staffName == null) {
  response.sendRedirect(request.getContextPath() + "/login.jsp");
  return;
}
%>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>出張費申請 - 交通費明細</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <script src="<%= request.getContextPath() %>/static/js/script.js"></script>
  <style>
    .remove-btn { position: absolute; top: 1px; right: 1px; background: none; border: none; font-size: 1.2rem; color: #888; cursor: pointer; }
    .d-none { display: none; }
  </style>
  <script>
    function addTransBlock() {
      const container = document.getElementById("trans-container");
      const blocks = document.querySelectorAll(".trans-block");
      const index = blocks.length;

      const template = document.querySelector(".trans-block");
      const clone = template.cloneNode(true);

      clone.querySelectorAll("input, textarea, select").forEach(el => el.value = "");
      clone.querySelector(".remove-btn").classList.remove("d-none");

      const fileInput = clone.querySelector("input[type='file']");
      fileInput.name = `receiptStep3_${index}[]`;
      fileInput.setAttribute("multiple", true);

      const fileList = clone.querySelector(".fileList");
      fileList.innerHTML = "";
      fileInput.addEventListener("change", function(e) {
        fileList.innerHTML = "";
        Array.from(e.target.files).forEach(file => {
          const li = document.createElement("li");
          li.textContent = file.name;
          fileList.appendChild(li);
        });
      });

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
      const amountInput = block.querySelector("input[name='fareAmount[]']");
      const type = block.querySelector("select[name='transTripType[]']").value;
      const burden = block.querySelector("select[name='burden[]']").value;
      const totalInput = block.querySelector("input[name='expenseTotal[]']");

      const amount = parseInt(amountInput.value || 0);
      let total = 0;
      if (burden === "自己") {
        const multiplier = (type === "往復") ? 2 : 1;
        total = amount * multiplier;
      }
      totalInput.value = total;
    }

    window.onload = function() {
      const firstBlock = document.querySelector(".trans-block");
      if (firstBlock) {
        const fileInput = firstBlock.querySelector("input[type='file']");
        const fileList = firstBlock.querySelector(".fileList");

        fileInput.addEventListener("change", function(e) {
          fileList.innerHTML = "";
          Array.from(e.target.files).forEach(file => {
            const li = document.createElement("li");
            li.textContent = file.name;
            fileList.appendChild(li);
          });
        });
      }
    };
  </script>
</head>
<body>
  <div class="page-container">
    <h2>交通費申請</h2>

    <form action="<%= request.getContextPath() %>/businessTrip" method="post" enctype="multipart/form-data">
      <input type="hidden" name="step" value="3">
      <input type="hidden" name="startDateHidden" value="<%= request.getParameter("startDateHidden") %>">
      <input type="hidden" name="endDateHidden" value="<%= request.getParameter("endDateHidden") %>">
      <input type="hidden" name="totalDays" value="<%= request.getParameter("totalDays") %>">

      <div style="display: flex; flex-direction: column; gap: 10px" id="trans-container">
        <div class="form-section trans-block">
          <button type="button" class="remove-btn d-none" onclick="removeBlock(this)">×</button>

          <div class="form-group"><label>訪問先</label><input type="text" name="transProject[]" placeholder="例:株式会社AAA"></div>
          <div class="form-group"><label>出発</label><input type="text" name="departure[]" placeholder="例:東京"></div>
          <div class="form-group"><label>到着</label><input type="text" name="arrival[]" placeholder="例:大阪"></div>
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

          <div class="form-group"><label>金額（税込）</label><input type="number" name="fareAmount[]" step="100" onchange="calcFareTotal(this)"></div>
          <div class="form-group"><label>区分</label>
            <select name="transTripType[]" onchange="calcFareTotal(this)">
              <option value="">選択してください</option>
              <option value="片道">片道</option>
              <option value="往復">往復</option>
            </select>
          </div>
          <div class="form-group"><label>負担者</label>
            <select name="burden[]" onchange="calcFareTotal(this)">
              <option value="">選択してください</option>
              <option value="会社">会社</option>
              <option value="自己">自己</option>
            </select>
          </div>
          <div class="form-group"><label>合計</label><input type="number" name="expenseTotal[]" readonly></div>
          <div class="form-group"><label>摘要</label><textarea name="transMemo[]" placeholder="メモなど"></textarea></div>

          <div class="form-group">
            <label>領収書添付（交通費）</label>
            <input type="file" name="receiptStep3_0[]" multiple class="fileInput">
            <small style="color: gray;">(Ctrlキーを押しながら複数ファイルを選択するか、または一つずつ追加して一括送信可)</small>
            <ul class="fileList"></ul>
          </div>

          <div style="text-align: center;">
            <button type="button" class="plus-btn" onclick="addTransBlock()">＋</button>
          </div>
        </div>
      </div>

      <div class="btn-section">
        <button type="button" onclick="window.location.href='<%= request.getContextPath() %>/businessTripStep3Back'">戻る</button>
        <button type="submit">確認</button>
      </div>
    </form>
  </div>
  <div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>
</body>
</html>