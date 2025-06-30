
function initializeStep2(diffDays, positionId) {
    // Dùng querySelectorAll để lấy TẤT CẢ các block đã có sẵn khi tải trang
    const allBlocks = document.querySelectorAll(".allowance-block");
    
    // Duyệt qua từng block và gán sự kiện cho nó
    allBlocks.forEach(block => {
        setupAllowanceBlock(block, diffDays, positionId);
    });

    // Sau khi gán sự kiện xong, chạy kiểm tra một lần để đảm bảo trạng thái đúng ngay từ đầu
    validateTotalDays(diffDays);
}

function setupAllowanceBlock(block, diffDays, positionId) {
    // Gán 日数 và thêm ghi chú + kiểm tra tổng ngày
    const daysInput = block.querySelector("input[name='days[]']");
    if (daysInput) {
		const isFirstBlock = block === document.querySelector(".allowance-block");
		daysInput.value = isFirstBlock ? diffDays : 0;

        // Thêm ghi chú nếu chưa có
        if (!block.querySelector(".days-note")) {
            const note = document.createElement("small");
            note.textContent = "※実際の日数に合わせて調整してください。";
            note.className = "days-note";
            note.style.color = "gray";
            daysInput.parentNode.appendChild(note);
        }

        daysInput.addEventListener('input', () => {
            calculateTotal(block);
            validateTotalDays(diffDays);
        });
    }

    // Gán 日当 theo chức vụ
    const dailyAllowanceInput = block.querySelector("input[name='dailyAllowance[]']");
    if (dailyAllowanceInput) {
        const dailyFee = (positionId === 'P0004') ? 2200 : 4000;
        dailyAllowanceInput.value = dailyFee;
    }

    // Gán sự kiện dropdown tính phí khách sạn
    ['regionType[]', 'tripType[]', 'burden[]'].forEach(name => {
        const sel = block.querySelector(select[name='${name}']);
        if (sel) {
            sel.addEventListener('change', () => calculateHotelFee(sel));
        }
    });

    // Gán sự kiện file input
    const fileInput = block.querySelector(".fileInput");
    if (fileInput) {
        fileInput.addEventListener("change", () => handleFileSelection(fileInput));
    }
}

function calculateHotelFee(elem) {
    const block = elem.closest(".allowance-block");
    const region = block.querySelector("select[name='regionType[]']").value;
    const tripType = block.querySelector("select[name='tripType[]']").value;
    const burden = block.querySelector("select[name='burden[]']").value;
    const hotelFeeInput = block.querySelector("input[name='hotelFee[]']");

    if (region && tripType && burden) {
        let baseHotelFee = 0;
        if (tripType === "短期出張") {
            if (region === "東京") baseHotelFee = 10000;
            else if (region === "東京以外") baseHotelFee = 8000;
        }

        hotelFeeInput.value = (burden === "自己") ? baseHotelFee : 0;

        calculateTotal(block);
    }
}

function calculateTotal(block) {
    const totalInput = block.querySelector("input[name='expenseTotal[]']");
    const hotelFee = parseInt(block.querySelector("input[name='hotelFee[]']").value || 0);
    const dailyFee = parseInt(block.querySelector("input[name='dailyAllowance[]']").value || 0);
    const days = parseInt(block.querySelector("input[name='days[]']").value || 0);

    totalInput.value = (hotelFee + dailyFee) * days;
}

function handleFileSelection(fileInput) {
    const block = fileInput.closest('.form-group');
    const fileListElement = block.querySelector(".fileList");
    fileListElement.innerHTML = "";

    Array.from(fileInput.files).forEach(file => {
        const li = document.createElement("li");
        const a = document.createElement("a");
        a.href = URL.createObjectURL(file);
        a.textContent = file.name;
        a.target = "_blank";
        li.appendChild(a);
        fileListElement.appendChild(li);
    });
}

function initializeStep2(diffDays, positionId) {
    // Dùng querySelectorAll để lấy TẤT CẢ các block đã có sẵn khi tải trang
    const allBlocks = document.querySelectorAll(".allowance-block");
    
    // Duyệt qua từng block và gán sự kiện cho nó
    allBlocks.forEach(block => {
        setupAllowanceBlock(block, diffDays, positionId);
    });

    // Sau khi gán sự kiện xong, chạy kiểm tra một lần để đảm bảo trạng thái đúng ngay từ đầu
    validateTotalDays(diffDays);
}


/**
 * HÀM KIỂM TRA TỔNG SỐ NGÀY (PHIÊN BẢN CẢI TIẾN)
 */
function validateTotalDays(maxDays) {
    let totalDays = 0;
    const allBlocks = document.querySelectorAll(".allowance-block");

    // 1. Tính tổng số ngày từ tất cả các block
    allBlocks.forEach(block => {
        const daysInput = block.querySelector("input[name='days[]']");
        if (daysInput) {
            totalDays += parseInt(daysInput.value || 0);
        }
    });

    // 2. Dựa vào tổng số ngày, quyết định hiển thị hoặc xóa lỗi cho TẤT CẢ các block
    allBlocks.forEach(block => {
        const daysInput = block.querySelector("input[name='days[]']");
        if (!daysInput) return;
        
        let errorDiv = block.querySelector(".days-error");

        if (totalDays > maxDays) {
            // Nếu tổng số ngày KHÔNG HỢP LỆ -> Hiển thị lỗi
            daysInput.style.borderColor = "red";
            if (!errorDiv) {
                errorDiv = document.createElement("div");
                errorDiv.className = "days-error";
                errorDiv.style.color = "red";
                errorDiv.style.fontSize = "0.85rem";
                // Đặt thông báo lỗi ngay dưới thẻ input
                daysInput.parentNode.insertBefore(errorDiv, daysInput.nextSibling);
            }
            errorDiv.textContent = `※合計日数が出張期間を超えています（最大 ${maxDays} 日）`;
        } else {
            // Nếu tổng số ngày HỢP LỆ -> Xóa lỗi
            daysInput.style.borderColor = ""; // Xóa viền đỏ
            if (errorDiv) {
                errorDiv.remove(); // Xóa thông báo lỗi nếu có
            }
        }
    });
}


function addAllowanceBlock() {
    const container = document.getElementById("allowance-container");
    const template = container.querySelector(".allowance-block");
    if (!template) return;

    const clone = template.cloneNode(true);
    const newIndex = container.children.length;

    // Reset nội dung input
    clone.querySelectorAll("input[type='text'], input[type='number'], textarea").forEach(el => el.value = "");
    clone.querySelectorAll("select").forEach(sel => sel.selectedIndex = 0);

    const fileInput = clone.querySelector(".fileInput");
    if (fileInput) {
        fileInput.name = "receipt_allowance_" + newIndex;
        fileInput.value = "";
    }

    const fileList = clone.querySelector(".fileList");
    if (fileList) fileList.innerHTML = "";

    const removeBtn = clone.querySelector('.remove-btn');
    if (removeBtn) removeBtn.style.display = 'block';

    container.appendChild(clone);
    setupAllowanceBlock(clone, diffDays, positionId);
}

function removeAllowanceBlock(btn) {
    btn.closest(".allowance-block").remove();
    validateTotalDays(diffDays); // cập nhật lại nếu xóa block
}
/**
 * HÀM TÍNH TOÁN TRUNG TÂM CHO STEP 3
 * Tính toán lại tổng tiền cho một block chi phí di chuyển.
 * @param {HTMLElement} block - Phần tử div.trans-block cần tính toán.
 */
function updateTransBlockCalculations(block) {
    if (!block) return;

    // Lấy giá trị từ các input/select
    const amount = parseInt(block.querySelector("input[name='fareAmount[]']").value || 0);
    const tripType = block.querySelector("select[name='transTripType[]']").value;
    const burden = block.querySelector("select[name='transBurden[]']").value;
    const totalInput = block.querySelector("input[name='expenseTotal[]']");

    let total = 0;
    // Chỉ tính tổng nếu người trả là "自己" (Tự túc)
    if (burden === "自己") {
        const multiplier = (tripType === "往復") ? 2 : 1; // Nếu là khứ hồi thì nhân 2
        total = amount * multiplier;
    }
    
    totalInput.value = total;
}

/**
 * Hàm khởi tạo cho các block của Step 3 khi trang tải xong.
 */
function initializeTransStep3() {
    const allBlocks = document.querySelectorAll(".trans-block");
    allBlocks.forEach(block => {
        // Gán sự kiện cho các input/select đã có sẵn (trường hợp back lại)
        const amountInput = block.querySelector("input[name='fareAmount[]']");
        const tripTypeSelect = block.querySelector("select[name='transTripType[]']");
        const burdenSelect = block.querySelector("select[name='transBurden[]']");

        amountInput.addEventListener('input', () => updateTransBlockCalculations(block));
        tripTypeSelect.addEventListener('change', () => updateTransBlockCalculations(block));
        burdenSelect.addEventListener('change', () => updateTransBlockCalculations(block));
        
        // Gán sự kiện cho file input
        const fileInput = block.querySelector(".fileInput");
        fileInput.addEventListener('change', () => handleFileSelection(fileInput));

        // Tính toán lại lần đầu cho các block đã có dữ liệu
        updateTransBlockCalculations(block);
    });
}

/**
 * Hàm thêm một block chi phí di chuyển mới.
 */
function addTransBlock() {
    const container = document.getElementById("trans-container");
    const template = container.querySelector(".trans-block");
    if (!template) return;

    const clone = template.cloneNode(true);
    const newIndex = container.children.length;

    // Reset các giá trị trong block mới
    clone.querySelectorAll("input[type='text'], input[type='number'], textarea").forEach(el => el.value = "");
    clone.querySelectorAll("select").forEach(sel => sel.selectedIndex = 0);

    // Xử lý file input
    const fileInput = clone.querySelector(".fileInput");
    if (fileInput) { 
        fileInput.name = "receipt_transport_" + newIndex;
        fileInput.value = "";
    }
    const fileList = clone.querySelector(".fileList");
    if (fileList) { fileList.innerHTML = ""; }
  
    // Hiển thị nút xóa
    const removeBtn = clone.querySelector('.remove-btn');
    if (removeBtn) { removeBtn.style.display = 'block'; }
    
    container.appendChild(clone);
    
    // ★ QUAN TRỌNG: Gán lại các sự kiện cho block vừa được thêm vào
    initializeTransStep3();
}

/**
 * Hàm xóa một block chi phí di chuyển.
 */
function removeTransBlock(btn) {
    const container = btn.closest('#trans-container');
    if (container.children.length > 1) {
        btn.closest(".trans-block").remove();
    } else {
        alert("最低1つの明細が必要です。");
    }
}

// =========================================================================================
//  BỘ LOGIC XỬ LÝ FILE ĐÍNH KÈM (Thêm vào cuối file businessTrip.js)
// =========================================================================================

/**
 * Hàm này được gọi bởi thuộc tính onchange của thẻ input file.
 * Nó quản lý việc thêm file mới vào một danh sách tạm.
 */
function handleFileSelection(input) {
    // Khởi tạo mảng tạm để quản lý file nếu chưa có
    if (!input._customFiles) {
        input._customFiles = [];
    }

    // Thêm các file mới chọn vào mảng tạm, tránh thêm file trùng lặp
    Array.from(input.files).forEach(file => {
        if (!input._customFiles.some(f => f.name === file.name && f.size === file.size)) {
            input._customFiles.push(file);
        }
    });

    // Cập nhật lại ô input và danh sách hiển thị
    updateFileInputAndPreview(input);
}

/**
 * Xóa một file vừa mới được chọn (chưa upload lên server).
 * @param {HTMLElement} button - Nút 'x' được bấm.
 * @param {string} fileName - Tên file cần xóa.
 * @param {HTMLInputElement} input - Thẻ input file tương ứng.
 */
function deleteNewFile(button, fileName, input) {
    // Lọc ra khỏi mảng tạm file có tên tương ứng
    input._customFiles = input._customFiles.filter(f => f.name !== fileName);
    
    // Cập nhật lại giao diện
    updateFileInputAndPreview(input);
}

/**
 * Hàm trung tâm: Cập nhật cả thẻ <input> và danh sách preview <ul>.
 * @param {HTMLInputElement} input - Thẻ input file cần cập nhật.
 */
function updateFileInputAndPreview(input) {
    const list = input.closest('.form-group').querySelector('.fileList');
    const dataTransfer = new DataTransfer();

    // Xóa các preview file mới cũ (có thẻ li không chứa data-file-type)
    list.querySelectorAll('li:not([data-file-type="existing"])').forEach(li => li.remove());

    // Từ mảng tạm, tạo lại danh sách preview và điền vào DataTransfer
    if (input._customFiles) {
        input._customFiles.forEach(file => {
            // Thêm file vào đối tượng DataTransfer để cập nhật giá trị của thẻ input
            dataTransfer.items.add(file);

            // Tạo các phần tử HTML để hiển thị preview
            const li = document.createElement('li');
            const a = document.createElement('a');
            a.href = URL.createObjectURL(file);
            a.textContent = file.name;
            a.target = '_blank';
            li.appendChild(a);

            const deleteBtn = document.createElement('button');
            deleteBtn.type = 'button';
            deleteBtn.textContent = '×';
            deleteBtn.className = 'file-delete-btn';
            
            // Nút xóa giờ sẽ gọi hàm deleteNewFile với tên file cụ thể
            deleteBtn.onclick = function() {
                deleteNewFile(this, file.name, input);
            };

            li.appendChild(deleteBtn);
            list.appendChild(li);
        });
    }

    // Cập nhật file trong ô input thật bằng các file trong DataTransfer
    input.files = dataTransfer.files;
}

/**
 * Xử lý việc "xóa" một file đã tồn tại trên server.
 * Nó không thực sự xóa file, mà thêm tên file vào một danh sách để server xử lý.
 * @param {HTMLElement} btn - Nút 'x' được bấm.
 */
function deleteExistingFile(btn) {
    const li = btn.closest('li');
    const uniqueName = li.dataset.uniqueName;
    const filesToDeleteInput = document.getElementById("filesToDelete");

    // Nếu không tìm thấy thẻ input ẩn, báo lỗi và dừng lại
    if (!filesToDeleteInput) {
        console.error("Không tìm thấy thẻ input với id='filesToDelete'.");
        return;
    }

    const currentDeleteList = filesToDeleteInput.value ? filesToDeleteInput.value.split(',') : [];
    if (!currentDeleteList.includes(uniqueName)) {
        currentDeleteList.push(uniqueName);
        filesToDeleteInput.value = currentDeleteList.join(',');
    }
    li.remove();
}