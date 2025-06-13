<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>出張費申請内容の確認</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
  <style>
    .confirm-container {
      max-width: 800px;
      margin: 2rem auto;
      background: #fff;
      padding: 2rem;
      border-radius: 10px;
      box-shadow: 0 4px 8px rgba(0,0,0,0.05);
    }
    h2, h3 {
      color: var(--primary-color);
      margin-bottom: 1rem;
    }
    .confirm-section { margin-bottom: 2rem; }
    .confirm-item { margin-bottom: 0.5rem; }
    .confirm-label { font-weight: 600; display: inline-block; min-width: 120px; color: #333; }
    .btn-section { text-align: center; margin-top: 2rem; }
  </style>
</head>
<body>
  <div class="page-container">
    <div class="confirm-container">
      <h2>出張費申請内容の確認</h2>
      <p>以下の内容で申請を行います。問題なければ「送信」を押してください。</p>

      <div class="confirm-section">
        <h3>出張情報</h3>
        <div class="confirm-item"><span class="confirm-label">出張期間：</span>2025/06/10 ～ 2025/06/12</div>
        <div class="confirm-item"><span class="confirm-label">出張区分：</span>短期</div>
        <div class="confirm-item"><span class="confirm-label">プロジェクト：</span>aaaaa</div>
        <div class="confirm-item"><span class="confirm-label">出張報告：</span>qqqq</div>
      </div>

      <div class="confirm-section">
        <h3>日当・宿泊費明細</h3>
        <div class="confirm-item"><span class="confirm-label">日付：</span>2025/06/11</div>
        <div class="confirm-item"><span class="confirm-label">日数：</span>2</div>
        <div class="confirm-item"><span class="confirm-label">日当：</span>4,000円</div>
        <div class="confirm-item"><span class="confirm-label">宿泊費：</span>10,000円</div>
        <div class="confirm-item"><span class="confirm-label">深夜手当：</span>0円</div>
        <div class="confirm-item"><span class="confirm-label">負担者：</span>会社</div>
        <div class="confirm-item"><span class="confirm-label">金額（合計）：</span>14,000円</div>
        <div class="confirm-item"><span class="confirm-label">摘要：</span>メモなど</div>
        <div class="confirm-item"><span class="confirm-label">領収書：</span>あり</div>
      </div>

      <div class="confirm-section">
        <h3>交通費明細</h3>
        <div class="confirm-item"><span class="confirm-label">日付：</span>2025/06/11</div>
        <div class="confirm-item"><span class="confirm-label">訪問先：</span>株式会社AAA</div>
        <div class="confirm-item"><span class="confirm-label">出発：</span>東京</div>
        <div class="confirm-item"><span class="confirm-label">到着：</span>大阪</div>
        <div class="confirm-item"><span class="confirm-label">交通機関：</span>新幹線</div>
        <div class="confirm-item"><span class="confirm-label">区分：</span>片道</div>
        <div class="confirm-item"><span class="confirm-label">負担者：</span>会社</div>
        <div class="confirm-item"><span class="confirm-label">金額：</span>--- 円</div>
        <div class="confirm-item"><span class="confirm-label">摘要：</span>---</div>
        <div class="confirm-item"><span class="confirm-label">領収書：</span>なし</div>
      </div>

      <div class="btn-section">
        <button type="button" onclick="history.back()">戻る</button>
        <button type="submit">送信</button>
      </div>
    </div>
  </div>
</body>
</html>
