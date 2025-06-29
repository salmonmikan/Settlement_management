
function initializeStep2(diffDays, positionId) {
    const firstBlock = document.querySelector(".allowance-block");
    if (!firstBlock) return;
    setupAllowanceBlock(firstBlock, diffDays, positionId);
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

function validateTotalDays(maxDays) {
    const allBlocks = document.querySelectorAll(".allowance-block");
    let total = 0;

    allBlocks.forEach(block => {
        const daysInput = block.querySelector("input[name='days[]']");
        total += parseInt((daysInput && daysInput.value) || 0);
    });

    allBlocks.forEach(block => {
        const daysInput = block.querySelector("input[name='days[]']");
        if (!daysInput) return;

        let error = block.querySelector(".days-error");
        if (total > maxDays) {
            if (!error) {
                error = document.createElement("div");
                error.className = "days-error";
                error.style.color = "red";
                error.style.fontSize = "0.85rem";
                daysInput.parentNode.appendChild(error);
            }
            error.textContent = `※合計日数が出張期間を超えています（最大 ${maxDays} 日）`;
            daysInput.style.borderColor = "red";
        } else {
            if (error) error.remove();
            daysInput.style.borderColor = "";
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