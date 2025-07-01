
    // =========================================================================================
    // PHẦN 1: SỬA LỖI TÍNH TOÁN TỔNG CỘNG (合計)
    // =========================================================================================
    function calcFareTotal(elem) {
        const block = elem.closest('.transportation-block');
        if (!block) return;

        const amountInput = block.querySelector("input[name='fareAmount[]']");
        const tripTypeSelect = block.querySelector("select[name='transTripType[]']");
        const burdenSelect = block.querySelector("select[name='burden[]']");
        const totalInput = block.querySelector("input[name='expenseTotal[]']");

        const amount = parseInt(amountInput.value || 0);
        const tripType = tripTypeSelect.value;
        const burden = burdenSelect.value;
        
        let total = 0;
        
        // === SỬA LỖI LOGIC TẠI ĐÂY ===
        // Tổng tiền chỉ được tính khi người chịu là "Bản thân" (自己)
        if (burden === "自己") {
            const multiplier = (tripType === "往復") ? 2 : 1;
            total = amount * multiplier;
        }
        
        totalInput.value = total;
    }

    // =========================================================================================
    // PHẦN 2: CÁC HÀM KHÁC GIỮ NGUYÊN
    // =========================================================================================
    
    // Các hàm xử lý file (giữ nguyên từ lần trước)
    function handleFileSelection(input) {
        if (!input._customFiles) { input._customFiles = []; }
        Array.from(input.files).forEach(file => {
            if (!input._customFiles.some(f => f.name === file.name && f.size === file.size)) {
                input._customFiles.push(file);
            }
        });
        updateFileInputAndPreview(input);
    }
    function deleteNewFile(button, fileName, input) {
        input._customFiles = input._customFiles.filter(f => f.name !== fileName);
        updateFileInputAndPreview(input);
    }
    function updateFileInputAndPreview(input) {
        const list = input.closest('.form-group').querySelector('.fileList');
        const dataTransfer = new DataTransfer();
        list.querySelectorAll('li:not([data-file-type="existing"])').forEach(li => li.remove());
        input._customFiles.forEach(file => {
            dataTransfer.items.add(file);
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
            deleteBtn.onclick = function() { deleteNewFile(this, file.name, input); };
            li.appendChild(deleteBtn);
            list.appendChild(li);
        });
        input.files = dataTransfer.files;
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

    // Các hàm logic chính (giữ nguyên từ lần trước)
    document.addEventListener("DOMContentLoaded", () => {
        const container = document.getElementById("transportation-container");
        if (container.children.length === 0) { addTransportationBlock(); } 
        else {
            reindexBlocks();
            container.querySelectorAll('.transportation-block').forEach(block => {
                const triggerElement = block.querySelector("input[name='fareAmount[]']");
                if (triggerElement) calcFareTotal(triggerElement);
            });
        }
        container.addEventListener('change', function(event) {
            const target = event.target;
            if (target.matches("input[name='fareAmount[]']") || 
                target.matches("select[name='transTripType[]']") ||
                target.matches("select[name='burden[]']")) {
                calcFareTotal(target);
            }
        });
    });

    function reindexBlocks() {
        const container = document.getElementById("transportation-container");
        const blocks = container.querySelectorAll(".transportation-block");
        blocks.forEach((block, index) => {
            const fileInput = block.querySelector(".fileInput");
            if (fileInput) { fileInput.name = 'receipt_transportation_' + index; }
        });
        updateRemoveButtons();
    }
    function updateRemoveButtons() {
        const container = document.getElementById("transportation-container");
        const blocks = container.querySelectorAll(".transportation-block");
        const showRemoveButton = blocks.length > 1;
        blocks.forEach(block => {
            const removeBtn = block.querySelector('.remove-btn');
            if (removeBtn) { removeBtn.style.display = showRemoveButton ? 'block' : 'none'; }
        });
    }
    function addTransportationBlock() {
        const container = document.getElementById("transportation-container");
        const template = document.getElementById("transportation-template");
        const clone = template.content.cloneNode(true);
        container.appendChild(clone);
        reindexBlocks();
    }
	
	function addTransportationBlock() {
	    const container = document.getElementById('transportation-container');
	    const template = document.getElementById('transportation-template').innerHTML;

	    // Tính index mới dựa trên số block hiện tại
	    const newIndex = container.querySelectorAll('.transportation-block').length;

	    // Thay tất cả {{index}} trong template bằng số thực tế
	    const processedHTML = template.replace(/{{index}}/g, newIndex);

	    // Tạo block mới và thêm vào DOM
	    const wrapper = document.createElement('div');
	    wrapper.innerHTML = processedHTML;
	    const newBlock = wrapper.firstElementChild;
	    container.appendChild(newBlock);
	}
    function removeTransportationBlock(btn) {
        const container = document.getElementById("transportation-container");
        if (container.children.length > 1) {
            btn.closest(".transportation-block").remove();
            reindexBlocks();
        } else {
            alert("最低1つの明細が必要です。");
        }
    }