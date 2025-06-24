<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="bean.BusinessTripBean.*, java.util.*" %>
<%
  String type = "出張費"; // gán cố định vì file này chỉ dùng cho 出張費
  BusinessTripBean bt = (BusinessTripBean) request.getAttribute("businessTripBean");
  Step1Data s1 = bt.getStep1Data();
  List<Step2Detail> s2List = bt.getStep2List();
  List<Step3Detail> s3List = bt.getStep3List();
  int totalAmount = bt.getTotalStep2Amount() + bt.getTotalStep3Amount();

  Map<String, List<String>> receiptMap = (Map<String, List<String>>) request.getAttribute("receiptMap");
%>

<style>
	.memo-block {
	  padding: 6px 12px;
	  background-color: #f9f9f9;
	  border-left: 4px solid #ccc;
	  font-size: 0.95em;
	  margin: 4px 0;
	}
</style>
  <!-- Step1 -->
  <div class="form-section">
    <h3 style="color: var(--primary-color)">基本情報</h3>
    <table>
      <tr><th>出張期間</th><td><%= s1.getStartDate() %> ～ <%= s1.getEndDate() %></td></tr>
      <tr><th>PJコード</th><td><%= s1.getProjectCode() %></td></tr>
      <tr><th>出張報告</th><td><%= s1.getReport() %></td></tr>
      <tr><th>合計日数</th><td><%= s1.getTotalDays() %>日間</td></tr>
    </table>
  </div>

  <!-- Step2 -->
  <div class="form-section">
    <h3 style="color: var(--primary-color)">日当・宿泊費</h3>
    <table>
      <tr>
        <th>地域区分</th><th>出徒区分</th><th>負担者</th><th>宿泊先</th>
        <th>日当</th><th>宿泊費</th><th>日数</th><th>合計</th>
      </tr>
      <% for (Step2Detail s2 : s2List) { %>
        <tr>
          <td><%= s2.getRegionType() %></td>
          <td><%= s2.getTripType() %></td>
          <td><%= s2.getBurden() %></td>
          <td><%= s2.getHotel() %></td>
          <td><%= s2.getDailyAllowance() %></td>
          <td><%= s2.getHotelFee() %></td>
          <td><%= s2.getDays() %></td>
          <td><%= s2.getExpenseTotal() %>円</td>
        </tr>
      <% } %>
    </table>
    <% for (Step2Detail s2 : s2List) { %>
      <div class="memo-block"><strong>摘要:</strong> <%= s2.getMemo() %></div>
    <% } %>

    <% 
      List<String> step2Files = receiptMap != null ? receiptMap.getOrDefault("step2", Collections.emptyList()) : Collections.emptyList();
      if (!step2Files.isEmpty()) {
    %>
    <div class="form-section">
      <h4>日当・宿泊費 領収書ファイル:</h4>
      <ul>
        <% for (String file : step2Files) {
             String original = file.substring(file.indexOf("_") + 1);
        %>
          <li><a href="<%= request.getContextPath() + "/" + file %>" target="_blank"><%= original %></a></li>
        <% } %>
      </ul>
    </div>
    <% } %>
  </div>

  <!-- Step3 -->
  <div class="form-section">
    <h3 style="color: var(--primary-color)">交通費</h3>
    <table>
      <tr>
        <th>訪問先</th><th>出発</th><th>到着</th><th>交通機関</th>
        <th>金額</th><th>区分</th><th>負担者</th><th>合計</th>
      </tr>
      <% for (Step3Detail s3 : s3List) { %>
        <tr>
          <td><%= s3.getTransProject() %></td>
          <td><%= s3.getDeparture() %></td>
          <td><%= s3.getArrival() %></td>
          <td><%= s3.getTransport() %></td>
          <td><%= s3.getFareAmount() %></td>
          <td><%= s3.getTransTripType() %></td>
          <td><%= s3.getTransBurden() %></td>
          <td><%= s3.getTransExpenseTotal() %>円</td>
        </tr>
      <% } %>
    </table>
    <% for (Step3Detail s3 : s3List) { %>
      <div class="memo-block"><strong>摘要:</strong> <%= s3.getTransMemo() %></div>
    <% } %>

    <% 
      List<String> step3Files = receiptMap != null ? receiptMap.getOrDefault("step3", Collections.emptyList()) : Collections.emptyList();
      if (!step3Files.isEmpty()) {
    %>
    <div class="form-section">
      <h4>交通費 領収書ファイル:</h4>
      <ul>
        <% for (String file : step3Files) {
             String original = file.substring(file.indexOf("_") + 1);
        %>
          <li><a href="<%= request.getContextPath() + "/" + file %>" target="_blank"><%= original %></a></li>
        <% } %>
      </ul>
    </div>
    <% } %>
  </div>

  <div class="confirmPage-total">総合計金額: <%= totalAmount %> 円</div>
</div>

</div> <!-- close page-container -->