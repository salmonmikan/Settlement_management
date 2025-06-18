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
<script>
        function editRow(rowId) {
            document.querySelectorAll(`#row-${rowId} td[data-editable]`).forEach(td => {
                const val = td.innerText;
                td.innerHTML = `<input type='text' class='form-control' value='${val}'/>`;
            });
            document.getElementById(`edit-btn-${rowId}`).style.display = 'none';
            document.getElementById(`save-btn-${rowId}`).style.display = 'inline-block';
        }

        function saveRow(rowId) {
            const id = document.querySelector(`#row-${rowId} input[name='id']`).value;
            const name = document.querySelector(`#row-${rowId} input[name='name']`).value;
            // 実際にはここでフォームPOSTなどを行う
            alert(`保存: ID=${id}, Name=${name}`);
        }

        function deleteRow(id) {
            if (confirm("この部署を削除しますか？")) {
                // 実際にはここでフォームPOSTなどを行う
                alert(`削除: ID=${id}`);
            }
        }

        function addRow() {
            const table = document.getElementById("dept-table-body");
            const newId = `new-${Date.now()}`;
            table.insertAdjacentHTML('beforeend', `
                <tr id="row-${newId}">
                    <td data-editable><input type='text'size="10" name='id' class='form-control'/></td>
                    <td data-editable><input type='text'size="10" name='name' class='form-control'/></td>
                    <td>
                        <button class="btn btn-sm btn-success" style="padding=0;padding: 3px 8px;" onclick="saveRow('${newId}')">保存</button>
                        <button class="btn btn-sm btn-secondary" style="padding=0;padding: 3px 8px;" onclick="this.closest('tr').remove()">取消</button>
                    </td>
                </tr>
            `);
        }
    </script>
</head>
<%
ArrayList<DepartmentBean> department = (ArrayList<DepartmentBean>) session.getAttribute("department");
%>
<body style="display: flex; justify-content: center;">
	<div class="page-container"
		style="display: flex; flex-direction: row; justify-content: center; align-items: center;">
		<!-- Sidebar -->
		<div class="staff-dashboard-wrapper">
			<div class="sidebar" style="height: 50vw">
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
			<div class="staff-main-content" style="min-width: 50vw;">
				<div class="panel">

					<!-- 管理画面 -->
					<div class="col-10 p-4">
						<h3>部署管理</h3>
						<div class="action-toolbar">
							<div class="spacer"></div>
							<button class="" onclick="addRow()">＋ 新規追加</button>
							<!--			      <button id="editBtn" disabled>編集</button>-->
							<!--			      <button id="deleteBtn" disabled>削除</button>-->
						</div>
						<table class="table table-bordered">
							<colgroup>
								<col>
								<!-- 1列目の幅を150pxに固定 -->
								<col>
								<!-- 2列目は自動 -->
								<col style="width: 150px;">
								<!-- 3列目の幅を200pxに固定 -->
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
								<tr>
									<td><%=bean.getDepartment_id()%></td>
									<td><%=bean.getDepartment_name()%></td>
									<td>
										<button id="edit-btn-1" class="btn btn-sm" style="padding=0;padding: 3px 8px;" onclick="editRow('1')">編集</button>
				                        <button id="save-btn-1" class="btn btn-sm" style="display:none" onclick="saveRow('1')">保存</button>
				                        
				                
								            <input type="hidden" name="department_id" value="<%= bean.getDepartment_id() %>">
								            <button id="save-btn-1" type="submit" style="padding=0;padding: 3px 8px;" class="btn btn-sm">削除</button>
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
</body>
</html>
