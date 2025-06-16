<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>出張費申請 - 確認画面</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>
  <div class="page-container">
    <h2>出張費申請内容 - 確認</h2>

    <div class="grid-container">

      <!-- 出張情報 -->
      <div class="form-section">
        <h3>出張情報</h3>
        <p><strong>出張期間：</strong>2025/07/01 ～ 2025/07/03</p>
        <p><strong>出張区分：</strong>短期</p>
        <p><strong>PJコード：</strong>PJ102：年間契約更新キャンペーン</p>
        <p><strong>業務報告：</strong>顧客との契約更新打合せ・書類確認 顧客との契約更新打合せ・書類確認 顧客との契約更新打合せ・書類確認 顧客との契約更新打合せ・書類確認 顧客との契約更新打合せ・書類確認</p>
      </div>

      <!-- 宿泊費 -->
      <div class="form-section">
        <h3>宿泊・日当</h3>日数
        <p><strong>宿泊先：</strong>APAホテル</p>
        <p><strong>宿泊費：</strong>10,000円</p>
        <p><strong>日当：</strong>4,000円</p>
        <p><strong>日数：</strong>2泊</p>
        <p><strong>負担者：</strong>会社</p>
        <p><strong>摘要：</strong>宿泊＋日当精算</p>
        <p><strong>合計：</strong>2万8千円</p>
        <p><strong>領収書：</strong>✔ 添付済（image.png）</p>
      </div>

      <!-- 交通費 -->
      <div class="form-section">
        <h3>交通費</h3>
        <p><strong>訪問先：</strong>株式会社A</p>
        <p><strong>出発 → 到着：</strong>東京 → 大阪（往復）</p>
        <p><strong>交通機関：</strong>新幹線</p>
        <p><strong>負担者：</strong>会社</p>
        <p><strong>摘要：</strong>移動費用精算</p>
        <p><strong>合計：</strong>2万円</p>
        <p><strong>領収書：</strong>✔ 添付済（receipt.png）</p>
      </div>
    </div>
    <div class="total-summary-box">
	  <div class="total-summary-text">
	    合計金額：<strong>4万8千円</strong>
	  </div>
	</div>

    <div class="btn-section">
      <button onclick="history.back()">戻る</button>
      <button type="submit">登録</button>
    </div>
  </div>
  <div class="footer">
    &copy; 2025 ABC株式会社 - All rights reserved.
  </div>
</body>
</html>