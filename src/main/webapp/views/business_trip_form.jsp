<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>出張費申請画面 - 入力フォーム</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
  <script>
	  const contextPath = "<%= request.getContextPath() %>";
  </script>
  <script src="<%= request.getContextPath() %>/js/businessTripForm.js"></script>
  
</head>
<body>
  <div class="page-container">
    <h2>出張費申請 - 入力フォーム</h2>

    <form action="submitBusinessTripExpense" method="post" enctype="multipart/form-data">
      <div class="grid-container">

        <!-- 出張情報 -->
        <div class="form-section">
          <h3>出張情報</h3>
		  <%
			  String position = (String) session.getAttribute("position"); // session có thể set khi login
			  String rankType = "社員"; // mặc định
			  if (position != null && (position.contains("部長") || position.contains("課長") || position.contains("役員"))) {
			    rankType = "管理";
			  }
			%>
		  <input type="hidden" name="rankType" value="<%= rankType %>">
		  <input type="hidden" name="rankType" value="<%= rankType %>">
		  <div class="form-group date-range">
            <label>出張期間</label>
            <div>
              <input type="date" name="startDate" required>
              <span>～</span>
              <input type="date" name="endDate" required>
            </div>
          </div>
		  <div class="form-group">
			  <label>地域区分</label>
			  <select name="regionType" required>
			    <option value="">選択してください</option>
			    <option value="物価高水準地域">物価高水準地域</option>
			    <option value="上記以外">上記以外</option>
			  </select>
			</div>	
          
          <div class="form-group">
            <label>出張区分</label>
            <select name="tripType" required>
			  <option value="">選択してください</option>
			  <option value="短期">短期</option>
			  <option value="長期">長期</option>
			</select>
          </div>
          <div class="form-group">
            <label>PJコード</label>
		    <select name="projectCode">
			    <option value="PJ101">PJ101：A社向け新規提案プロジェクト</option>
			    <option value="PJ102">PJ102：年間契約更新キャンペーン</option>
			    <option value="PJ103">PJ103：大阪展示会出展準備</option>
			    <option value="PJ104">PJ104：代理店開拓プロジェクト</option>
			    <option value="PJ105">PJ105：顧客満足度向上活動</option>
		    </select>
          </div>
          <div class="form-group">
            <label>出張報告</label>
            <textarea class="hokoku-text" name="tripReport" placeholder="業務内容や目的を入力してください"></textarea>
          </div>
        </div>

        <!-- 日当・宿泊費明細 -->
        <div class="form-section">
          <h3>日当・宿泊費明細</h3>
          <!-- <div class="form-group">
            <label>日付</label>
            <input type="date" name="allowanceDate">
          </div> -->
          <div class="form-group">
            <label>宿泊先</label>
            <input type="text" name="transProject" placeholder="例:APAホテル">
          </div>
          <div class="form-group">
            <div style="display: flex; gap: 1rem;">
              <div style="flex: 1;">
            	<label>宿泊費</label>
            	<input type="text" name="hotelFee" readonly>
              </div>
              <div style="flex: 1;">
                <label>日当</label>
            	<input type="number" name="dailyAllowance" step="100" readonly>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div style="display: flex; gap: 1rem;">
              <div style="flex: 1;">
                <label>日数</label>
            	<input type="number" name="days" min="1">
              </div>
              <div style="flex: 1;">
                <label>負担者</label>
	            <select name="burden">
	              <option value="会社">会社</option>
	              <option value="自己">自己</option>
	            </select>
              </div>
            </div>
          </div>
          <div class="form-group">
            <div style="display: flex; gap: 1rem;">
         
              <div style="flex: 1;">
                 <label>合計</label>
            	 <input type="number" name="expenseTotal" step="100" readonly>
              </div>
            </div>
          </div>
          
          <div class="form-group">
            <label>摘要</label>
            <textarea name="allowanceMemo" placeholder="メモなど"></textarea>
          </div>
          <div class="form-group">
            <label>領収書添付（日当・宿泊費）</label>
            <input type="file" name="receiptDaily" multiple>
          </div>
        </div>

        <!-- 交通費明細 -->
        <div class="form-section">
          <h3>交通費明細</h3>
          <!-- <div class="form-group">
            <label>日付</label>
            <input type="date" name="transDate">
          </div> -->
          <div class="form-group">
            <label>訪問先</label>
            <input type="text" name="transProject" placeholder="例:株式会社AAA">
          </div>
          <div class="form-group">
            <div style="display: flex; gap: 1rem;">
              <div style="flex: 1;">
                <label>出発</label>
                <input type="text" name="departure" placeholder="例: 東京">
              </div>
              <div style="flex: 1;">
                <label>到着</label>
                <input type="text" name="arrival" placeholder="例: 大阪">
              </div>
            </div>
          </div>
          
          <div class="form-group">
            <div style="display: flex; gap: 1rem;">
              <div style="flex: 1;">
                <label>区分</label>
	            <select name="transTripType">
	              <option value="片道">片道</option>
	              <option value="往復">往復</option>
	            </select>
              </div>
              <div style="flex: 1;">
                <label>交通機関</label>
	            <select name="transport">
	              <option value="新幹線">新幹線</option>
	              <option value="電車">電車</option>
	              <option value="タクシー">タクシー</option>
	              <option value="飛行機">飛行機</option>
	              <option value="自家用車">自家用車</option>
	              <option value="レンタカー">レンタカー</option>
	              <option value="他の">他の</option>
	            </select>
              </div>
            </div>
          </div>
          
      
          <div class="form-group">
            <div style="display: flex; gap: 1rem;">
              <div style="flex: 1;">
                <label>負担者</label>
	            <select name="burden">
	              <option value="会社">会社</option>
	              <option value="自己">自己</option>
	            </select>
              </div>
              <div style="flex: 1;">
                 <label>金額（税込）</label>
            	 <input type="number" name="expenseTotal" step="100" readonly>
              </div>
            </div>
          </div>
          <div class="form-group">
            <label>合計</label>
            <input type="number" name="expenseTotal" step="100" readonly>
          </div>

          <div class="form-group">
            <label>摘要</label>
            <textarea name="transMemo" placeholder="メモなど"></textarea>
          </div>
          <div class="form-group">
            <label>領収書添付（交通費）</label>
            <input type="file" name="receiptTrans">
          </div>
        </div>
      </div>

      <div class="btn-section">
       
        <button type="reset">リセット</button>
        <button type="submit">確認</button>
      </div>
    </form>
  </div>

  <div class="footer">
    &copy; 2025 ABC株式会社 - All rights reserved.
  </div>
</body>
</html>