<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>立替金精算書</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/style.css">
<style>
    /* Giữ nguyên các style của bạn cho đẹp */
    .remove-btn {
        position: absolute;
        top: 1px;
        right: 1px;
        background: none;
        border: none;
        font-size: 1.5rem;
        color: #888;
        cursor: pointer;
        font-weight: bold;
    }
    .fileList {
        list-style-type: none;
        padding-left: 0;
        margin-top: 8px;
    }
    .fileList li {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 4px;
        border-bottom: 1px solid #eee;
        word-break: break-word;
    }
    .fileList a {
        color: #0056b3;
        text-decoration: underline;
    }
    .file-delete-btn {
        background: none;
        border: none;
        color: #d9534f;
        cursor: pointer;
        font-weight: bold;
        font-size: 1.2rem;
        padding: 0 5px;
    }
    /* Thêm style để ẩn template đi */
    #reimbursement-template {
        display: none;
    }
</style>
</head>
<body>

<div class="page-container">
    <h2>立替金精算書</h2>
    <form action="<%=request.getContextPath()%>/reimbursement" method="post" enctype="multipart/form-data">
        <input type="hidden" id="filesToDelete" name="filesToDelete" value="">

        <%-- Container chính, ban đầu sẽ trống. JS sẽ thêm các block vào đây --%>
        <div id="reimbursement-container" style="display: flex; flex-direction: column; gap: 10px">
        </div>

        <div style="text-align: center; margin-top: 10px;">
            <button type="button" class="plus-btn" onclick="addReimbursementBlock()">＋ 追加</button>
        </div>

        <div class="btn-section">
            <button type="button" class="back-btn" onclick="window.location.href='<%=request.getContextPath()%>/home'">戻る</button>
            <button type="submit" class="next-btn">確認</button>
        </div>
    </form>
</div>

<div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>

<%-- 
  TEMPLATE - KHUNG SƯỜN MẪU CHO MỘT BLOCK
  Đây là cách làm đúng. Chúng ta sẽ sao chép từ template này thay vì từ một block đang tồn tại.
--%>
<template id="reimbursement-template">
    <div class="form-section reimbursement-block" style="position: relative;">
        <button type="button" class="remove-btn" onclick="removeReimbursementBlock(this)">×</button>
        
        <div class="form-group"><label>日付</label><input type="date" name="date[]" required></div>
        <div class="form-group"><label>訪問先</label><input type="text" name="destinations[]" placeholder="例: ABC株式会社" /></div>
        <div class="form-group">
            <label>PJコード</label>
            <select name="projectCode[]" required>
                <option value="">選択してください</option>
                <c:forEach var="p" items="${projectList}">
                    <option value="${p.id}">${p.id}：${p.name}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group"><label>勘定科目</label><input type="text" name="accountingItem[]" placeholder="例: 交通費" /></div>
        <div class="form-group"><label>摘要</label><textarea name="report[]" placeholder="内容や目的を入力してください"></textarea></div>
        <div class="form-group"><label>金額</label><input type="number" name="amount[]" required></div>
        <div class="form-group">
            <label>領収書添付</label>
            <%-- Tên sẽ được đặt động bằng JavaScript. Để trống hoặc đặt tên mẫu --%>
            <input type="file" name="receipt_reimbursement_" multiple class="fileInput" onchange="handleFileSelection(this)">
            <ul class="fileList"></ul>
        </div>
    </div>
</template>


<script>
// Chờ cho toàn bộ trang được tải xong
document.addEventListener("DOMContentLoaded", () => {
    // Tự động thêm block đầu tiên khi trang được tải
    addReimbursementBlock();
});

// Hàm cập nhật lại chỉ số (index) của tất cả các block
// Rất quan trọng khi xóa một block ở giữa
function reindexBlocks() {
    const container = document.getElementById("reimbursement-container");
    const blocks = container.querySelectorAll(".reimbursement-block");
    
    blocks.forEach((block, index) => {
        const fileInput = block.querySelector(".fileInput");
        if (fileInput) {
            // === DÒNG SỬA ĐỔI DUY NHẤT LÀ ĐÂY ===

            // Code cũ (Gây lỗi trên JSP):
            // fileInput.name = `receipt_reimbursement_${index}`; 

            // Code mới (Hoạt động chính xác):
            // Sử dụng phép nối chuỗi truyền thống để tránh xung đột với JSP EL
            fileInput.name = 'receipt_reimbursement_' + index;

            // =======================================
        }
    });

    // Cập nhật trạng thái của nút xóa
    updateRemoveButtons();
}

// Hàm cập nhật trạng thái các nút xóa
function updateRemoveButtons() {
    const container = document.getElementById("reimbursement-container");
    const blocks = container.querySelectorAll(".reimbursement-block");
    // Nếu chỉ có 1 block, ẩn nút xóa đi. Nếu có nhiều hơn, hiện tất cả.
    const showRemoveButton = blocks.length > 1;
    blocks.forEach(block => {
        const removeBtn = block.querySelector('.remove-btn');
        if (removeBtn) {
            removeBtn.style.display = showRemoveButton ? 'block' : 'none';
        }
    });
}


// HÀM THÊM BLOCK MỚI (ĐÃ VIẾT LẠI HOÀN TOÀN)
function addReimbursementBlock() {
    const container = document.getElementById("reimbursement-container");
    const template = document.getElementById("reimbursement-template");

    // 1. Sao chép nội dung từ template
    const clone = template.content.cloneNode(true);

    // 2. Thêm block mới vào container
    container.appendChild(clone);

    // 3. Cập nhật lại toàn bộ chỉ số.
    // Hàm này sẽ tự động đặt tên đúng cho block vừa thêm vào.
    reindexBlocks();
}


// HÀM XÓA BLOCK (ĐÃ VIẾT LẠI)
function removeReimbursementBlock(btn) {
    const container = document.getElementById("reimbursement-container");
    if (container.children.length > 1) {
        // Tìm và xóa block cha của nút được bấm
        btn.closest(".reimbursement-block").remove();
        // Sau khi xóa, cập nhật lại toàn bộ chỉ số để chúng tuần tự trở lại
        reindexBlocks();
    } else {
        alert("最低1つの明細が必要です。");
    }
}


// Hàm xử lý hiển thị file đã chọn (giữ nguyên)
function handleFileSelection(input) {
    const list = input.closest('.form-group').querySelector('.fileList');
    list.innerHTML = ""; // Xóa danh sách file preview cũ

    Array.from(input.files).forEach(file => {
        const li = document.createElement('li');
        const a = document.createElement('a');
        a.href = URL.createObjectURL(file);
        a.textContent = file.name;
        a.target = '_blank';
        li.appendChild(a);
        list.appendChild(li);
    });
}
</script>

</body>
</html>