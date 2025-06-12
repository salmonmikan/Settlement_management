<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商品マスタ</title>
</head>
<body>
<h3 style="text-align:center">商品マスタメンナテンスメニュー</h3>
<div style="text-align:center">
<a href="<%= request.getContextPath() %>/list?no=1">商品一覧</a><br>
<a href="<%= request.getContextPath() %>/web/mod.jsp?no=3">商品登録</a> <br>
<a href="<%= request.getContextPath() %>/list?no=2">商品変更・削除</a><br>
</div>
</body>
</html>