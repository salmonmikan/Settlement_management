/**
 * 
 */
// 更新用
function editRow(rowId) {
	// 他の編集中の行があればキャンセル
	//	  document.querySelectorAll('tr').forEach(tr => {
	//	    if (tr.department_id !== `row-${rowId}` && tr.querySelector("input[name='department_name']")) {
	//	      const cancelId = tr.department_id.replace("row-", "");
	//	      cancelRow(cancelId);
	//	    }
	//	  });
	 
    const row = document.getElementById("row-" + rowId);
    if (!row) {
        console.warn("対象の行が見つかりませんでした:", rowId);
        return;
    }

    row.querySelectorAll('td[data-editable]').forEach(td => {
    	let val = td.innerText;
    	td.dataset.original = val; // cancelRow時、値保存用
    	

    	let input = document.createElement("input");
    	input.type = "text";
    	input.name = "department_name_new" // 別にdepartment_nameでもOKだと思う
    	input.size = 6;
    	input.className = "form-control";
    	input.value = val;

    	td.innerHTML = "";  // 中身を一度空にする
    	td.appendChild(input);
    });
    
	//表示するボタンの切り替え
    const editBtn = document.getElementById("edit-btn-" + rowId);
    const saveBtn = document.getElementById("save-btn-" + rowId);
    editBtn.style.display = 'none';
    saveBtn.style.display = 'inline-block';
    const deleteBtn = document.getElementById("delete-btn-" + rowId);
    const cancelBtn = document.getElementById("cancel-btn-" + rowId);
    deleteBtn.style.display = 'none';
    cancelBtn.style.display = 'inline-block';
}

// 新規追加
function addRow() {
	  const table = document.getElementById("dept-table-body");
	  const newId = `new-${Date.now()}`;

	  // 新規行HTML（formは<td>の中に入れる形）
	  const newRow = document.createElement('tr');
	  newRow.id = `row-${newId}`;
	  newRow.innerHTML = `
	    <td><input type="text" size="6" class="form-control dept-id-input" name="dept-id-input"/></td>
	    <td><input type="text" size="6" class="form-control dept-name-input" name="dept-name-input"/></td>
	    <td>
	      <form method="POST" action="departmentControl" style="display:inline;">
	        <input type="hidden" name="department_id" />
	        <input type="hidden" name="department_name" />
	        <input type="hidden" name="action" value="insert" />
	        <button type="button" class="btn btn-sm btn-success" style="padding: 3px 8px;" onclick="submitNewRow(this)">保存</button>
	      </form>
	      <button type="button" class="btn btn-sm btn-secondary" style="padding: 3px 8px;" onclick="this.closest('tr').remove()">取消</button>
	    </td>
	  `;
	  table.appendChild(newRow);
	}
// 新規追加用送信機能
function submitNewRow(button) {
	  const form = button.closest('form');
	  const row = button.closest('tr');

	  // 入力値を取得
	  const deptId = row.querySelector('.dept-id-input').value.trim();
	  console.log(deptId);
	  const deptName = row.querySelector('.dept-name-input').value.trim();
	  console.log(deptName);

	  // hiddenフィールドにセット
	  form.querySelector('input[name="department_id"]').value = deptId;
	  form.querySelector('input[name="department_name"]').value = deptName;

	  // フォーム送信
	  form.submit();
	}
	
function submitRow(button) {
    const form = button.closest('form');
    const row = button.closest('tr');
    const name = row.querySelector('.dept-name input').value;
    console.log(name);
	//formのpostにnameを入れる
    form.querySelector('input[name="department_name"]').value = name;
    

    let actionInput = form.querySelector('input[name="action"]');
    if (!actionInput) {
      actionInput = document.createElement('input');
      actionInput.type = 'hidden';
      actionInput.name = 'action';
      form.appendChild(actionInput);
    }
    actionInput.value = 'update';
    form.submit();
  }

function cancelRow(rowId) {
	  const row = document.getElementById("row-" + rowId);
	  const td = row.querySelector('.dept-name');
	  const original = td.dataset.original;

	  td.innerHTML = original; // テキストとして戻す

	  // ボタン表示を元に戻す
	  document.getElementById("edit-btn-" + rowId).style.display = 'inline-block';
	  document.getElementById("save-btn-" + rowId).style.display = 'none';
	  document.getElementById("delete-btn-" + rowId).style.display = 'inline-block';
	  document.getElementById("cancel-btn-" + rowId).style.display = 'none';
	}