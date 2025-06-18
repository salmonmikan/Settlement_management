<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>部署管理</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
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
                    <td data-editable><input type='text' name='id' class='form-control'/></td>
                    <td data-editable><input type='text' name='name' class='form-control'/></td>
                    <td>
                        <button class="btn btn-sm btn-success" onclick="saveRow('${newId}')">保存</button>
                        <button class="btn btn-sm btn-secondary" onclick="this.closest('tr').remove()">取消</button>
                    </td>
                </tr>
            `);
        }
    </script>
</head>
<body style="
    display: flex;
    justify-content: center;
">
<div class="page-container" style="
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
">
      <!-- Sidebar -->
      <div class="staff-dashboard-wrapper">
      <div class="sidebar">
		  <div class="menu-block">
		    <h3>メニュー</h3>
		    <ul>
		      <li><a href="#">パスワード変更</a></li>
		      <li><a href="#">申請一覧</a></li>
		    </ul>
		  </div>
		  
		  <div class="welcome-message">
		    ようこそ、田中 さん！
		  </div>
	  </div>
	        <!-- Main content -->
      <div class="staff-main-content" style="min-width: 50vw;">
<!--        <div class="panel">-->
<!--          <h4>申請する</h4>-->
<!--          <div class="btn-section">-->
<!--            <a href="#">交通費申請</a>-->
<!--            <a href="<%= request.getContextPath() %>/businessTrip">出張費申請</a>-->
<!--            <a href="#">立替金申請</a>-->
<!--          </div>-->
<!--        </div>-->
    <div class="panel">
		
        <!-- 管理画面 -->
        <div class="col-10 p-4">
            <h3>部署管理</h3>
                <div class="action-toolbar">
			      <div class="spacer"></div>
			      <button class="" onclick="addRow()">＋ 新規追加</button>
			      <button id="editBtn" disabled>編集</button>
			      <button id="deleteBtn" disabled>削除</button>
			    </div>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>部署ID</th>
                    <th>部署名</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="dept-table-body">
                <c:forEach var="dept" items="${departments}">
                    <tr id="row-${dept.id}">
                        <td data-editable>${dept.id}</td>
                        <td data-editable>${dept.name}</td>
                        <td>
                            <button id="edit-btn-${dept.id}" class="btn btn-sm btn-warning" onclick="editRow('${dept.id}')">編集</button>
                            <button id="save-btn-${dept.id}" class="btn btn-sm btn-success" style="display:none" onclick="saveRow('${dept.id}')">保存</button>
                            <button class="btn btn-sm btn-danger" onclick="deleteRow('${dept.id}')">削除</button>
                        </td>
                    </tr>
                </c:forEach>
                <tr id="row-1">
                    <td data-editable>D001</td>
                    <td data-editable>管理部</td>
                    <td>
                        <button id="edit-btn-1" class="btn btn-sm" onclick="editRow('1')">編集</button>
                        <button id="save-btn-1" class="btn btn-sm" style="display:none" onclick="saveRow('1')">保存</button>
                        <button class="btn btn-sm btn-danger" onclick="deleteRow('1')">削除</button>
                    </td>
                </tr>
                <tr id="row-2">
                    <td data-editable>D002</td>
                    <td data-editable>営業部</td>
                    <td>
                        <button id="edit-btn-2" class="btn btn-sm" onclick="editRow('2')">編集</button>
                        <button id="save-btn-2" class="btn btn-sm" style="display:none" onclick="saveRow('2')">保存</button>
                        <button class="btn btn-sm btn-danger" onclick="deleteRow('2')">削除</button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    </div>
    </div>
</div>
</body>
</html>
