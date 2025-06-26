document.addEventListener("DOMContentLoaded", () => {
  const checkboxes = document.querySelectorAll('.row-check');
  const editBtn = document.getElementById('editBtn');
  const deleteBtn = document.getElementById('deleteBtn');
  const modal = document.getElementById('submitModal');

  const selectAll = document.getElementById('selectAll');
  if (selectAll) {
    selectAll.addEventListener('change', function () {
      checkboxes.forEach(cb => cb.checked = this.checked);
      updateToolbarState();
    });
  }

  checkboxes.forEach(cb => cb.addEventListener('change', updateToolbarState));

  function updateToolbarState() {
    const checked = document.querySelectorAll('.row-check:checked');
    if (editBtn) editBtn.disabled = (checked.length !== 1);
    if (deleteBtn) deleteBtn.disabled = (checked.length === 0);
  }

  const statusFilter = document.getElementById('statusFilter');
  if (statusFilter) {
    statusFilter.addEventListener('change', function () {
      const selected = this.value;
      const rows = document.querySelectorAll('#applicationTable tbody tr');
      rows.forEach(row => {
        const status = row.getAttribute('data-status');
        row.style.display = (!selected || selected === status) ? '' : 'none';
      });
    });
  }

  document.querySelectorAll('.clickable-row').forEach(row => {
    row.addEventListener('click', function (e) {
      if (e.target.tagName === 'INPUT') return;
      const id = this.dataset.id;
      const mode = document.body.dataset.mode || "staff"; // new
      const base = (mode === "approver") ? "approvalDetail" : "applicationDetail";
      window.location.href = `${base}?id=${id}`;
    });
  });

  window.confirmSubmit = function () {
    const checked = document.querySelectorAll('.row-check:checked');
    if (checked.length === 0) {
      Swal.fire({
        title: '注意',
        text: '提出する申請を選択してください。',
        confirmButtonText: 'OK',
        confirmButtonColor: '#00a1bb'
      });
      return false;
    }

    for (const cb of checked) {
      const row = cb.closest('tr');
      const status = row.getAttribute('data-status');
      if (status !== '未提出') {
        Swal.fire({
          title: '注意',
          text: '未提出の申請のみ提出可能です。',
          confirmButtonText: 'OK',
          confirmButtonColor: '#00a1bb'
        });
        return false;
      }
    }

    modal.classList.remove('hidden');
    return false;
  }

  window.closeModal = function () {
    modal.classList.add('hidden');
  }

  window.submitForm = function () {
    document.querySelector('form').submit();
  }

  window.addEventListener("pageshow", function () {
    document.querySelectorAll('.row-check').forEach(cb => cb.checked = false);
    if (modal) modal.classList.add('hidden');
    updateToolbarState();
  });
});