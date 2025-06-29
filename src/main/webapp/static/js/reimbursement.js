
    // =========================================================================================
    // PHẦN 1: LOGIC XỬ LÝ FILE ĐƯỢC LẤY TỪ TRANSPORTATION.JSP
    // =========================================================================================
    
    // Hàm này được gọi khi người dùng chọn file
    function handleFileSelection(input) {
        // Bước 1: Khởi tạo mảng tạm để quản lý file nếu chưa có
        if (!input._customFiles) {
            input._customFiles = [];
        }

        // Bước 2: Thêm các file mới chọn vào mảng tạm
        Array.from(input.files).forEach(file => {
            // Tránh thêm file trùng tên
            if (!input._customFiles.some(f => f.name === file.name && f.size === file.size)) {
                input._customFiles.push(file);
            }
        });

        // Bước 3: Cập nhật lại ô input và danh sách hiển thị
        updateFileInputAndPreview(input);
    }

    // Hàm xóa một file mới được chọn (chưa lên server)
    function deleteNewFile(button, fileName, input) {
        // Lọc ra khỏi mảng tạm file có tên tương ứng
        input._customFiles = input._customFiles.filter(f => f.name !== fileName);
        
        // Cập nhật lại ô input và danh sách hiển thị
        updateFileInputAndPreview(input);
    }
    
    // Hàm "trung tâm": Cập nhật cả input thật và danh sách preview
    function updateFileInputAndPreview(input) {
        const list = input.closest('.form-group').querySelector('.fileList');
        const dataTransfer = new DataTransfer();

        // Xóa các preview file mới cũ đi
        list.querySelectorAll('li:not([data-file-type="existing"])').forEach(li => li.remove());

        // Từ mảng tạm, tạo lại danh sách preview và điền vào DataTransfer
        input._customFiles.forEach(file => {
            // Thêm file vào đối tượng DataTransfer để cập nhật input
            dataTransfer.items.add(file);

            // Tạo các phần tử để hiển thị
            const li = document.createElement('li');
            const a = document.createElement('a');
            a.href = URL.createObjectURL(file);
            a.textContent = file.name;
            a.target = '_blank';
            li.appendChild(a);

            const deleteBtn = document.createElement('button');
            deleteBtn.type = 'button';
            deleteBtn.textContent = '×';
            // Dùng lại class CSS cũ cho đồng bộ
            deleteBtn.className = 'file-delete-btn'; 
            
            // Nút xóa giờ sẽ gọi hàm deleteNewFile với tên file cụ thể
            deleteBtn.onclick = function() {
                deleteNewFile(this, file.name, input);
            };

            li.appendChild(deleteBtn);
            list.appendChild(li);
        });

        // Cập nhật file trong ô input thật bằng các file trong DataTransfer
        input.files = dataTransfer.files;
    }
    
    // Hàm xóa file đã có trên server (giữ nguyên, không thay đổi)
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


    // =========================================================================================
    // PHẦN 2: CÁC HÀM LOGIC CHÍNH CỦA REIMBURSEMENT (GIỮ NGUYÊN)
    // =========================================================================================
    document.addEventListener("DOMContentLoaded", () => {
        const container = document.getElementById("reimbursement-container");
        if (container.children.length === 0) {
            addReimbursementBlock();
        } else {
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