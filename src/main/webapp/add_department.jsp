<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta charset="UTF-8">
<title>部署登録</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/style.css">
<style>
.container {
    max-width: 500px;
    margin: 40px auto;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 8px;
    background-color: #f9f9f9;
}
h2 {
    text-align: center;
    margin-bottom: 20px;
}
form {
    display: flex;
    flex-direction: column;
    gap: 15px;
}
label {
    font-weight: bold;
}
input[type="text"] {
    padding: 8px;
    font-size: 1rem;
}
.button-row {
    display: flex;
    justify-content: space-between;
}
button {
    padding: 8px 16px;
    font-size: 1rem;
}
</style>
</head>
<body>

<div class="container">
    <h2>部署新規登録</h2>
    <form action="department" method="post">
        <!-- 確認画面へ送るためのaction -->
        <input type="hidden" name="action" value="confirm_create">

        <div>
            <label for="department_id">部署ID：</label>
            <input type="text" id="department_id" name="department_id" required>
        </div>

        <div>
            <label for="department_name">部署名：</label>
            <input type="text" id="department_name" name="department_name" required>
        </div>

        <div class="button-row">
            <button type="button" onclick="history.back();">戻る</button>
            <button type="submit">確認</button>
        </div>
    </form>
</div>

</body>
</html>
