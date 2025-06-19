<%@page import="bean.DepartmentBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html lang="ja">
<head>
<title>部署管理</title>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/static/css/style.css">
<!--    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">-->
</head>
<%
ArrayList<DepartmentBean> department = (ArrayList<DepartmentBean>) session.getAttribute("department");
%>
<body style="display: flex; justify-content: center;">
	<div class="page-container"
		style="display: flex; flex-direction: row; justify-content: center; align-items: center;">
		<!-- Sidebar -->
		<div class="staff-dashboard-wrapper" style="height: 50vh;">
			<div class="sidebar">
				<div class="menu-block">
					<h3>メニュー</h3>
					<ul>
						<li><a href="#">申請一覧</a></li>
						<li><a href="#">パスワード変更</a></li>
					</ul>
				</div>

				<div class="welcome-message">ようこそ、田中 さん！</div>
			</div>
			<!-- Main content -->
			<div class="staff-main-content" style="width: 500px;">
				<div class="panel">

					<!-- 管理画面 -->
					<div class="col-10 p-4">
						<h3>部署管理</h3>
						<div class="action-toolbar">
							<div class="spacer"></div>
							<button class="" onclick="addRow()">＋ 新規追加</button>
						</div>
							<table class="table table-bordered">
								<colgroup>
									<!-- 列幅を固定 -->
									<col style="width: 150px;">
									<col style="width: 150px;">
									<col style="width: 150px;">
								</colgroup>
								<thead>
									<tr>
										<th>部署ID</th>
										<th>部署名</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody id="dept-table-body">
									<%
									for (DepartmentBean bean : department) {
									%>
									<tr id="row-<%=bean.getDepartment_id()%>">
										<td><%=bean.getDepartment_id()%></td>
										<td data-editable class="dept-name"><%=bean.getDepartment_name()%></td>
										<td>
											<form method="POST" action="departmentControl" style="display: inline;">
		<!--										js側での処理-->
												<button id="edit-btn-<%=bean.getDepartment_id()%>" type="button" class="btn btn-sm" style="padding: 3px 8px;"
													onclick="editRow('<%=bean.getDepartment_id()%>')">編集</button>
													
		<!--										servlet側での処理-->
												
												 <input type="hidden" name="department_id" value="<%=bean.getDepartment_id()%>">
												 <input type="hidden" name="department_name" value="<%=bean.getDepartment_name()%>">
		<!--										  更新(編集押下時)と削除(デフォルト)-->
												 <button id="save-btn-<%=bean.getDepartment_id()%>" type="button" name="action" value="update" class="btn btn-sm"
												  	onclick="submitRow(this)" style="padding: 3px 8px; display: none;">更新</button>
												 <button id="delete-btn-<%=bean.getDepartment_id()%>" type="submit" name="action" value="delete" class="btn btn-sm"
												  	onclick="return confirm('この部署を削除しますか？')" style="padding: 3px 8px;">削除</button>
												
												
												
												<button id="cancel-btn-<%=bean.getDepartment_id()%>" type="button" class="btn btn-sm" style="display: none; padding: 3px 8px"
													onclick="cancelRow('<%=bean.getDepartment_id()%>')">取消</button>
											</form>
										</td>
									</tr>
									<%
									}
									%>
								</tbody>
							</table>
						
					</div>
				</div>
			</div>
		</div>
	</div>

	<script>
	console.log(11);
		function editRow(rowId) {
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
            
<!--            表示するボタンの切り替え-->
            const editBtn = document.getElementById("edit-btn-" + rowId);
            const saveBtn = document.getElementById("save-btn-" + rowId);
            editBtn.style.display = 'none';
            saveBtn.style.display = 'inline-block';
            const deleteBtn = document.getElementById("delete-btn-" + rowId);
            const cancelBtn = document.getElementById("cancel-btn-" + rowId);
            deleteBtn.style.display = 'none';
            cancelBtn.style.display = 'inline-block';
        }


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
<!--            formのpostにnameを入れる-->
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
    </script>
</body>
</html>
