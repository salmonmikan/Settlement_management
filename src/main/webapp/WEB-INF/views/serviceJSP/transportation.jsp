<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String staffName = (String) session.getAttribute("staffName");
if (staffName == null) {
  response.sendRedirect(request.getContextPath() + "/login.jsp");
  return;
}
%>
<% Boolean editMode = (Boolean) request.getAttribute("editMode"); %>
<% Integer applicationId = (Integer) request.getAttribute("applicationId");
Integer successCount = (Integer) request.getAttribute("successCount");
%>

<input type="hidden" name="editMode" value="<%= editMode != null && editMode ? "true" : "false" %>">
<input type="hidden" name="applicationId" value="<%= applicationId != null ? applicationId : "" %>">
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>交通費申請</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
  <script src="<%= request.getContextPath() %>/static/js/script.js"></script>
  <style>
    .remove-btn { position: absolute; top: 1px; right: 1px; background: none; border: none; font-size: 1.2rem; color: #888; cursor: pointer; }
    .d-none { display: none; }
  </style>

</head>
<body>
  <div class="page-container">
    <h2>交通費申請</h2>
    <% if (successCount != null && successCount > 0) { %>
    <p style="color: red;text-align:center"><%= successCount %> 件の申請を登録しました。</p>
<% } %>
    <form action="<%= request.getContextPath() %>/transportationRequest" method="post">
      <input type="hidden" name="editMode" value="<%= editMode != null && editMode ? "true" : "false" %>">
	  <% if (editMode != null && editMode) { %>
	    <input type="hidden" name="applicationId" value="<%= applicationId %>">
	  <% } %>
      <div style="display: flex; flex-direction: column; gap: 10px" id="trans-container">
        <div class="form-section trans-block">
          <button type="button" class="remove-btn d-none" onclick="removeBlock(this)">×</button>
          <div class="form-group">
          <label>PJコード</label>
          <select name="projectCode[]" required>
            <option value="">選択してください</option>
            <c:forEach var="p" items="${projectList}">
              <option value="${p.id}">${p.id}：${p.name}</option>
            </c:forEach>
          </select>
        </div>
<!--          <div class="form-group"><label>訪問月・日</label><input type="text" name="date[]" placeholder="例:yyyy-mm-dd"></div>-->
          <div class="form-group">
          <label>訪問月・日</label>
          <div style="display: flex; gap: 1rem;">
            <input type="date" name="date[]" id="date" required>
          </div>
        </div>
          <div class="form-group"><label>出発</label><input type="text" name="departure[]" placeholder="例:東京駅"></div>
          <div class="form-group"><label>到着</label><input type="text" name="arrival[]" placeholder="例:大阪駅"></div>
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
           
            <ul class="fileList"></ul>
          </div>

          <div style="text-align: center;">
            <button type="button" class="plus-btn" onclick="addTransBlock()">＋</button>
          </div>
        </div>
      </div>

      <div class="btn-section">
        <button type="button" onclick="window.location.href='<%= request.getContextPath() %>/menu'">戻る</button>
        <button type="submit">確認</button>
      </div>
    </form>
  </div>
  <div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>
  
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
	  const tripType = block.querySelector("select[name='transTripType[]']").value;
	  const burden = block.querySelector("select[name='burden[]']").value;
	  const totalInput = block.querySelector("input[name='expenseTotal[]']");

	  const amount = parseInt(amountInput.value || 0);

	  if (burden === "自己") {
	    const multiplier = (tripType === "往復") ? 2 : 1;
	    totalInput.value = amount * multiplier;
	  } else {
	    totalInput.value = 0;
	  }
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

    //  Gọi để set 合計 ban đầu nếu có dữ liệu
    const fareInput = firstBlock.querySelector("input[name='fareAmount[]']");
    if (fareInput && fareInput.value) {
      calcFareTotal(fareInput);
    }
  }
};
</script>
</body>
</html>