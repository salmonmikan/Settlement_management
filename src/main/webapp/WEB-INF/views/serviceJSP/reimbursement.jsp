<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>立替金精算書</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/style.css">
<style>
    /* Các style của bạn có thể giữ nguyên ở đây */
    .remove-btn { position: absolute; top: 1px; right: 1px; background: none; border: none; font-size: 1.5rem; color: #888; cursor: pointer; font-weight: bold; }
    .fileList { list-style-type: none; padding-left: 0; margin-top: 8px; }
    #reimbursement-template { display: none; }
</style>
</head>
<body>

<div class="page-container">
    <h2>立替金精算書</h2>
    <form action="<%=request.getContextPath()%>/reimbursement" method="post" enctype="multipart/form-data">
        <input type="hidden" id="filesToDelete" name="filesToDelete" value="">

        <div id="reimbursement-container" style="display: flex; flex-direction: column; gap: 10px">
            
            <%-- ================================================================== --%>
            <%-- PHẦN 1: DÙNG JSP ĐỂ HIỂN THỊ LẠI DỮ LIỆU KHI BẤM "BACK" --%>
            <%-- ================================================================== --%>
            <%-- Vòng lặp này chỉ chạy khi server gửi về đối tượng reimbursement có chứa details --%>
            <c:forEach var="detail" items="${reimbursement.details}" varStatus="loop">
                <div class="form-section reimbursement-block" style="position: relative;">
                    <button type="button" class="remove-btn" onclick="removeReimbursementBlock(this)">×</button>
                    
                    <div class="form-group"><label>日付</label><input type="date" name="date[]" value="${detail.date}" required></div>
                    <div class="form-group"><label>訪問先</label><input type="text" name="destinations[]" value="${detail.destinations}" /></div>
                    <div class="form-group">
                        <label>PJコード</label>
                        <select name="projectCode[]" required>
                            <option value="">選択してください</option>
                            <c:forEach var="p" items="${projectList}">
                                <option value="${p.id}" ${p.id == detail.projectCode ? 'selected' : ''}>${p.id}：${p.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group"><label>勘定科目</label><input type="text" name="accountingItem[]" value="${detail.accountingItem}" /></div>
                    <div class="form-group"><label>摘要</label><textarea name="report[]">${detail.report}</textarea></div>
                    <div class="form-group"><label>金額</label><input type="number" name="amount[]" value="${detail.amount}" required></div>
                    <div class="form-group">
                        <label>領収書添付</label>
                        <%-- Tên file sẽ được JS đặt lại chính xác ở dưới --%>
                        <input type="file" name="receipt_reimbursement_${loop.index}" multiple class="fileInput" onchange="handleFileSelection(this)">
                        <ul class="fileList">
                            <%-- Hiển thị các file đã được tải lên trước đó --%>
                            <c:forEach var="file" items="${detail.temporaryFiles}">
                                <li data-file-type="existing" data-unique-name="${file.uniqueStoredName}">
                                    <a href="${pageContext.request.contextPath}/uploads/${file.uniqueStoredName}" target="_blank">${file.originalFileName}</a>
                                    <%-- Nút xóa file cũ cần hàm JS riêng --%>
                                    <button type="button" class="file-delete-btn" onclick="deleteExistingFile(this)">×</button>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </c:forEach>
        </div>

        <div style="text-align: center; margin-top: 10px;">
            <button type="button" class="plus-btn" onclick="addReimbursementBlock()">＋</button>
        </div>

        <div class="btn-section">
            <button type="button" class="back-btn" onclick="window.location.href='<%=request.getContextPath()%>/home'">戻る</button>
            <button type="submit" class="next-btn">確認</button>
        </div>
    </form>
</div>

<div class="footer">&copy; 2025 ABC株式会社 - All rights reserved.</div>

<%-- ================================================================== --%>
<%-- PHẦN 2: TEMPLATE CHO JAVASCRIPT ĐỂ THÊM BLOCK MỚI --%>
<%-- ================================================================== --%>
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
            <input type="file" name="receipt_reimbursement_" multiple class="fileInput" onchange="handleFileSelection(this)">
            <ul class="fileList"></ul>
        </div>
    </div>
</template>


<script>
// ==================================================================
// PHẦN 3: JAVASCRIPT ĐƯỢC CẬP NHẬT ĐỂ TƯƠNG THÍCH
// ==================================================================
document.addEventListener("DOMContentLoaded", () => {
    const container = document.getElementById("reimbursement-container");
    // CHỈ thêm block đầu tiên nếu server KHÔNG render sẵn block nào
    if (container.children.length === 0) {
        addReimbursementBlock();
    } else {
        // Nếu server ĐÃ render sẵn các block, chỉ cần chạy reindex để đảm bảo mọi thứ đúng
        reindexBlocks();
    }
});

function reindexBlocks() {
    const container = document.getElementById("reimbursement-container");
    const blocks = container.querySelectorAll(".reimbursement-block");
    blocks.forEach((block, index) => {
        const fileInput = block.querySelector(".fileInput");
        if (fileInput) {
            fileInput.name = 'receipt_reimbursement_' + index;
        }
    });
    updateRemoveButtons();
}

function updateRemoveButtons() {
    const container = document.getElementById("reimbursement-container");
    const blocks = container.querySelectorAll(".reimbursement-block");
    const showRemoveButton = blocks.length > 1;
    blocks.forEach(block => {
        const removeBtn = block.querySelector('.remove-btn');
        if (removeBtn) {
            removeBtn.style.display = showRemoveButton ? 'block' : 'none';
        }
    });
}

function addReimbursementBlock() {
    const container = document.getElementById("reimbursement-container");
    const template = document.getElementById("reimbursement-template");
    const clone = template.content.cloneNode(true);
    container.appendChild(clone);
    reindexBlocks();
}

function removeReimbursementBlock(btn) {
    const container = document.getElementById("reimbursement-container");
    if (container.children.length > 1) {
        btn.closest(".reimbursement-block").remove();
        reindexBlocks();
    } else {
        alert("最低1つの明細が必要です。");
    }
}

function handleFileSelection(input) {
    // Lấy phần tử <ul> để hiển thị danh sách file
    const list = input.closest('.form-group').querySelector('.fileList');

    // Bước 1: Xóa các preview cũ của những file MỚI CHỌN (có thẻ li không chứa data-file-type)
    // Nó sẽ không xóa các file đã được lưu từ server (khi bấm back)
    list.querySelectorAll('li:not([data-file-type="existing"])').forEach(li => li.remove());

    // Bước 2: Lặp qua tất cả các file người dùng vừa chọn trong ô input
    Array.from(input.files).forEach(file => {
        // Tạo một dòng mới <li>
        const li = document.createElement('li');

        // Tạo link xem trước file <a>
        const a = document.createElement('a');
        a.href = URL.createObjectURL(file);
        a.textContent = file.name;
        a.target = '_blank';
        li.appendChild(a);

        // =======================================================
        // === PHẦN SỬA ĐỔI BẮT ĐẦU TỪ ĐÂY ===
        // =======================================================

        // Tạo nút xóa '×'
        const deleteBtn = document.createElement('button');
        deleteBtn.type = 'button'; // Quan trọng: để không submit form
        deleteBtn.textContent = '×';
        deleteBtn.className = 'file-delete-btn'; // Dùng lại class CSS cũ cho đồng bộ
        
        // Định nghĩa hành động khi bấm nút xóa
        deleteBtn.onclick = function() {
            // Xóa tất cả các file đã chọn trong input này
            input.value = ''; 
            
            // Chạy lại chính hàm này để cập nhật lại danh sách hiển thị (giờ đã rỗng)
            // Đây là cách làm sạch sẽ, tái sử dụng logic có sẵn.
            handleFileSelection(input); 
        };

        // Thêm nút xóa vào dòng <li>
        li.appendChild(deleteBtn);

        // =======================================================
        // === PHẦN SỬA ĐỔI KẾT THÚC TẠI ĐÂY ===
        // =======================================================

        // Thêm dòng <li> hoàn chỉnh vào danh sách <ul>
        list.appendChild(li);
    });
}

function deleteExistingFile(btn) {
    const li = btn.closest('li');
    const uniqueName = li.dataset.uniqueName;
    const filesToDeleteInput = document.getElementById("filesToDelete");
    const currentDeleteList = filesToDeleteInput.value ? filesToDeleteInput.value.split(',') : [];
    if (!currentDeleteList.includes(uniqueName)) {
        currentDeleteList.push(uniqueName);
        filesToDeleteInput.value = currentDeleteList.join(',');
    }
    li.remove();
}
</script>

</body>
</html>