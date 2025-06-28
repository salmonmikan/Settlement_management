<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>立替金精算書</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/static/css/style.css">
<script src="<%=request.getContextPath()%>/static/js/script.js"></script>
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

		<form action="<%=request.getContextPath()%>/reimbursement"
			method="post">

			<div style="display: flex; flex-direction: column; gap: 10px"
				id="allowance-container">
				<div class="form-section allowance-block"
					style="position: relative;">
					<button type="button" class="remove-btn"
						onclick="removeBlock(this)">×</button>
					<div class="form-group">
						<label>訪問期間</label>
						<div style="display: flex; gap: 1rem;">
							<input type="date" name="date[]" id="date" required>
						</div>
					</div>

					<div class="form-group">
						<label>訪問先</label>
						<textarea name="destinations[]" placeholder="SESKY株式会社"></textarea>
					</div>

					<div class="form-group">
						<label>PJコード</label> <select name="projectCode[]" required>
							<option value="">選択してください</option>
							<c:forEach var="p" items="${projectList}">
								<option value="${p.id}">${p.id}：${p.name}</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group">
						<label>報告書</label>
						<textarea name="report[]" class="hokoku-text"
							placeholder="業務内容や目的を入力してください"></textarea>
					</div>

					<div class="form-group">
						<label>勘定科目</label>
						<textarea name="accountingItem[]" placeholder="入力してください。"></textarea>
					</div>

					<div class="form-group">
						<label>摘要</label>
						<textarea name="memo[]" placeholder="メモなど"></textarea>
					</div>

					<div class="form-group">
						<label>金額</label> <input type="number" name="amount[]">
					</div>

					<div class="form-group">
						<label>領収書添付</label> <input type="file" name="receiptDaily[]"
							multiple class="fileInput"> <small style="color: gray;">(Ctrlキーを押しながら複数ファイルを選択するか、または一つずつ追加して一括送信可)</small>
						<ul class="fileList"></ul>
					</div>

					<div style="text-align: center;">
						<button type="button" class="plus-btn"
							onclick="addAllowanceBlock()">＋</button>
					</div>
				</div>
			</div>

			<div class="btn-section">
				<button type="button"
					onclick="window.location.href='<%=request.getContextPath()%>/menu'">戻る</button>

				<button type="submit">確認</button>
			</div>
		</form>
	</div>

	<div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>

	<script>
    function addAllowanceBlock() {
      const container = document.getElementById("allowance-container");
      const template = document.querySelector(".allowance-block");
      const clone = template.cloneNode(true);

      // XÓA input name="step" nếu bị clone nhầm
      clone.querySelectorAll("input[name='step']").forEach(el => el.remove());

      // Xóa dữ liệu (trừ input file)
      clone.querySelectorAll("input, textarea").forEach(el => {
        if (el.type !== "file") el.value = "";
      });
      clone.querySelectorAll("select").forEach(sel => sel.selectedIndex = 0);

      // Reset file input event
      const fileInput = clone.querySelector(".fileInput");
      const fileList = clone.querySelector(".fileList");
      if (fileInput && fileList) {
        fileInput.addEventListener("change", function (e) {
          const newFiles = Array.from(e.target.files);
          fileList.innerHTML = "";
          newFiles.forEach(file => {
            const li = document.createElement("li");
            li.textContent = file.name;
            fileList.appendChild(li);
          });
          e.target.value = "";
        });
      }

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
  </script>
</body>
</html>
