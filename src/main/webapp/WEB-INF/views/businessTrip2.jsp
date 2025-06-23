<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="bean.TripCategory" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.gson.Gson" %>
<%
  List<TripCategory> categoryList = (List<TripCategory>) request.getAttribute("tripCategoryList");
  String jsonCategoryList = new Gson().toJson(categoryList);
  String startDateStr = (String) session.getAttribute("tripStartDate");
  String endDateStr = (String) session.getAttribute("tripEndDate");
  long diffDays = (long) session.getAttribute("numOfDays");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>出張費申請 - 日当・宿泊費明細</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
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
  <h2>日当・宿泊費申請</h2>

  <form action="<%= request.getContextPath() %>/businessTrip" method="post" enctype="multipart/form-data">
    <input type="hidden" name="step" value="2">

    <div id="allowance-container">
      <div class="form-section allowance-block" style="position: relative;">
        <button type="button" class="remove-btn" onclick="removeBlock(this)">×</button>

        <!-- 出張区分 -->
        <div class="form-group">
          <label>出張区分</label>
          <select name="tripType[]" onchange="updateRegionType(this);calcTotal(this)">
            <option value="">選択してください</option>
            <%
              java.util.Set<String> tripTypes = new java.util.HashSet<>();
              for (TripCategory c : categoryList) {
                  if (tripTypes.add(c.getTripType())) {
            %>
            <option value="<%= c.getTripType() %>"><%= c.getTripType() %></option>
            <%   }
              }
            %>
          </select>
        </div>

        <!-- 地域区分 -->
        <div class="form-group">
          <label>地域区分</label>
          <select name="regionType[]" onchange="updateAllowanceFields(this);calcTotal(this)">
            <option value="">選択してください</option>
          </select>
        </div>

        <div class="form-group">
          <label>宿泊費</label>
          <input type="number" name="hotelFee[]" readonly>
        </div>

        <div class="form-group">
          <label>日当</label>
          <input type="number" name="dailyAllowance[]" readonly>
        </div>

        <div class="form-group">
          <label>早朝深夜</label>
          <input type="number" name="nightAllowance[]" onchange="calcTotal(this)" >
        </div>

        <div class="form-group">
          <label>負担者</label>
          <select name="burden[]" onchange="handleBurdenChange(this)">
            <option value="">選択してください</option>
            <option value="会社">会社</option>
            <option value="自己">自己</option>
          </select>
        </div>

        <div class="form-group">
          <label>日数</label>
          <input type="number" name="days[]" min="1" onchange="calcTotal(this)">
        </div>

        <div class="form-group">
          <label>合計</label>
          <input type="number" name="expenseTotal[]" readonly>
        </div>

        <div class="form-group">
          <label>摘要</label>
          <textarea name="memo[]" placeholder="メモなど"></textarea>
        </div>

        <div class="form-group">
          <label>領収書添付（日当・宿泊費）</label>
          <input type="file" name="receiptStep2_0[]" multiple class="fileInput">
          <small style="color: gray;">(Ctrlキーを押しながら複数ファイルを選択)</small>
          <ul class="fileList"></ul>
        </div>

        <div style="text-align: center;">
          <button type="button" class="plus-btn" onclick="addAllowanceBlock()">＋</button>
        </div>
      </div>
    </div>

    <div class="btn-section">
      <button type="button" onclick="window.location.href='<%= request.getContextPath() %>/businessTripStep2Back'">戻る</button>
      <button type="submit">次へ</button>
    </div>
  </form>
</div>

<div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>

<!-- JavaScript -->
<script>
  const tripCategoryList = <%= jsonCategoryList %>;
  const diffDays = <%= diffDays %>;

  function updateRegionType(select) {
    const block = select.closest(".allowance-block");
    const regionSelect = block.querySelector("select[name='regionType[]']");
    const selectedTripType = select.value;

    regionSelect.innerHTML = '<option value="">選択してください</option>';
    const regions = tripCategoryList
      .filter(tc => tc.tripType === selectedTripType)
      .map(tc => tc.regionType);
    const uniqueRegions = [...new Set(regions)];

    uniqueRegions.forEach(region => {
      const opt = document.createElement("option");
      opt.value = region;
      opt.textContent = region;
      regionSelect.appendChild(opt);
    });

    updateAllowanceFields(regionSelect);
  }

  function updateAllowanceFields(regionSelect) {
    const block = regionSelect.closest(".allowance-block");
    const tripType = block.querySelector("select[name='tripType[]']").value;
    const regionType = regionSelect.value;

    const match = tripCategoryList.find(tc => tc.tripType === tripType && tc.regionType === regionType);
    if (match) {
      block.querySelector("input[name='hotelFee[]']").value = match.hotelFee;
      block.querySelector("input[name='dailyAllowance[]']").value = match.dailyAllowance;
    } else {
      block.querySelector("input[name='hotelFee[]']").value = "";
      block.querySelector("input[name='dailyAllowance[]']").value = "";
    }
  }

  function handleBurdenChange(select) {
    calcTotal(select);
  }

  function calcTotal(elem) {
    const block = elem.closest(".allowance-block");
    const days = parseInt(block.querySelector("input[name='days[]']").value || 0);
    const burden = block.querySelector("select[name='burden[]']").value;
    const hotelFee = parseInt(block.querySelector("input[name='hotelFee[]']").value || 0);
    const daily = parseInt(block.querySelector("input[name='dailyAllowance[]']").value || 0);

    const total = (burden === "自己") ? (hotelFee + daily) * days : daily * days;
    block.querySelector("input[name='expenseTotal[]']").value = total;
  }

  function removeBlock(btn) {
    const blocks = document.querySelectorAll(".allowance-block");
    if (blocks.length > 1) {
      btn.closest(".allowance-block").remove();
    } else {
      alert("最低1つの明細が必要です");
    }
  }

  function addAllowanceBlock() {
    const container = document.getElementById("allowance-container");
    const template = document.querySelector(".allowance-block");
    const clone = template.cloneNode(true);

    clone.querySelectorAll("input, textarea, select").forEach(el => {
      if (el.tagName === "SELECT") {
        el.selectedIndex = 0;
      } else {
        el.value = "";
      }
    });

    const fileList = clone.querySelector(".fileList");
    if (fileList) fileList.innerHTML = "";
    const fileInputs = clone.querySelectorAll("input[type='file']");
    const blockCount = document.querySelectorAll(".allowance-block").length;
    fileInputs.forEach(fileInput => {
      fileInput.name = `receiptStep2_${blockCount}[]`;
      fileInput.classList.add("fileInput");
    });

    clone.querySelector(".remove-btn").classList.remove("d-none");
    container.appendChild(clone);
  }

  window.onload = function () {
    const blocks = document.querySelectorAll(".allowance-block");
    blocks.forEach(block => {
      const daysInput = block.querySelector("input[name='days[]']");
      if (!daysInput.value || parseInt(daysInput.value) <= 0) {
        daysInput.value = diffDays;
      }
    });
  };
</script>
</body>
</html>
