
// === Trip Step 1 ===
document.getElementById("step3Form").addEventListener("submit", function (e) {
  const step = document.getElementById("stepHidden").value;
  if (step !== "3") {
    alert("step が不正です。フォーム送信が中止されました。");
    e.preventDefault();
  }
});
function setHiddenFields() {
	
	
  const startInput = document.getElementById("startDate");
  const endInput = document.getElementById("endDate");
  const start = new Date(startInput.value);
  const end = new Date(endInput.value);
  if (end < start) {
    alert("終了日は開始日より後の日付を選択してください。");
    return false;
  }
  const totalDays = Math.floor((end - start) / (1000 * 60 * 60 * 24)) + 1;
  document.getElementById("totalDays").value = totalDays;
  document.getElementById("startDateHidden").value = startInput.value;
  document.getElementById("endDateHidden").value = endInput.value;
  return true;
}

function setHiddenFields() {
    const start = document.getElementById("startDate").value;
    const end = document.getElementById("endDate").value;

    document.getElementById("startDateHidden").value = start;
    document.getElementById("endDateHidden").value = end;

    if (start && end) {
        const diffTime = new Date(end) - new Date(start);
        const days = Math.floor(diffTime / (1000 * 60 * 60 * 24)) + 1;
        document.getElementById("totalDays").value = days;
    }
    return true;
}

// === Trip Step 2 ===
function updateAllowanceAndHotel(elem) {
  const block = elem.closest(".allowance-block");
  const tripType = block.querySelector("select[name='tripType[]']").value;
  const region = block.querySelector("select[name='regionType[]']").value;
  const rankType = sessionPosition;
  const isManager = !(rankType.includes("一般社員"));

  let hotelFee = 0, dailyFee = 0;
  if (tripType === "短期出張") {
    if (region === "物価高水準地域") {
      hotelFee = 10000;
      dailyFee = isManager ? 4000 : 2200;
    } else if (region === "上記以外") {
      hotelFee = 8000;
      dailyFee = isManager ? 4000 : 2200;
    } else if (region === "会社施設・縁故先宿泊") {
      hotelFee = 0;
      dailyFee = 5000;
    }
  } else if (tripType === "長期出張") {
    hotelFee = 0;
    dailyFee = (region === "会社施設・縁故先宿泊") ? 3500 : 1000;
  } else if (tripType === "研修出張") {
    hotelFee = 0;
    dailyFee = 1000;
  }

  block.setAttribute('data-hotel-fee', hotelFee);
  block.querySelector("input[name='dailyAllowance[]']").value = dailyFee;
  handleBurdenChange(block.querySelector("select[name='burden[]']"));
}

function calcTotal(elem) {
  const block = elem.closest(".allowance-block");
  const burden = block.querySelector("select[name='burden[]']").value;
  const days = parseInt(block.querySelector("input[name='days[]']").value || 0);
  const hotelFee = parseInt(block.querySelector("input[name='hotelFee[]']").value || 0);
  const daily = parseInt(block.querySelector("input[name='dailyAllowance[]']").value || 0);
  const total = (burden === "自己") ? (hotelFee + daily) * days : daily * days;
  block.querySelector("input[name='expenseTotal[]']").value = total;
}

function handleBurdenChange(select) {
  const block = select.closest(".allowance-block");
  const burden = select.value;
  const hotelFeeInput = block.querySelector("input[name='hotelFee[]']");
  const calculated = parseInt(block.getAttribute('data-hotel-fee') || 0);

  if (burden === "自己") {
    hotelFeeInput.value = calculated;
  } else {
    hotelFeeInput.value = "";
  }

  calcTotal(select);
}

/*function addAllowanceBlock() {
  const container = document.getElementById("allowance-container");
  const blocks = document.querySelectorAll(".allowance-block");
  const index = blocks.length;

  const template = document.querySelector(".allowance-block");
  const clone = template.cloneNode(true);

  clone.querySelectorAll("input, textarea").forEach(el => el.value = "");
  clone.querySelectorAll("select").forEach(sel => sel.selectedIndex = 0);

  const fileInput = clone.querySelector(".fileInput");
  fileInput.name = `receiptStep2_${index}[]`;
  fileInput.setAttribute("multiple", true);

  container.appendChild(clone);

  fileInput.addEventListener("change", function(e) {
    const fileList = clone.querySelector(".fileList");
    fileList.innerHTML = "";
    Array.from(e.target.files).forEach(file => {
      const li = document.createElement("li");
      li.textContent = file.name;
      fileList.appendChild(li);
    });
  });

  updateAllowanceAndHotel(clone.querySelector("select[name='regionType[]']"));
}*/
function addAllowanceBlock() {
    const container = document.getElementById("allowance-container");
    const newBlock = container.firstElementChild.cloneNode(true);

    const currentCount = container.children.length;

    // Cập nhật name cho input file
    const fileInput = newBlock.querySelector("input[type='file']");
    if (fileInput) {
        fileInput.name = "receiptStep2_" + currentCount + "[]";
    }

    // Xóa dữ liệu trong block mới
    newBlock.querySelectorAll("input, textarea").forEach(input => {
        if (input.type !== "hidden") input.value = "";
    });

    newBlock.querySelector(".remove-btn").classList.remove("d-none");

    container.appendChild(newBlock);
}

function removeAllowanceBlock(btn) {
  const blocks = document.querySelectorAll(".allowance-block");
  if (blocks.length > 1) {
    btn.closest(".allowance-block").remove();
  } else {
    alert("最低1つの明細が必要です");
  }
}

function initializeAllowanceBlock(diffDays) {
  const firstBlock = document.querySelector(".allowance-block");
  if (firstBlock) {
    const daysInput = firstBlock.querySelector("input[name='days[]']");
    if (daysInput.value === "" || parseInt(daysInput.value) <= 1) {
      daysInput.value = diffDays;
    }
    updateAllowanceAndHotel(firstBlock.querySelector("select[name='regionType[]']"));

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
}


// === Trip Step 3 ===
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

function removeTransBlock(btn) {
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

function initializeTransBlock() {
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

    const fareInput = firstBlock.querySelector("input[name='fareAmount[]']");
    if (fareInput && fareInput.value) {
      calcFareTotal(fareInput);
    }
  }
}
