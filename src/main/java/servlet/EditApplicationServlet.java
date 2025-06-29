package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.BusinessTripBean;
import bean.Step1Data;
import bean.Step2Detail;
import bean.Step3Detail;
import dao.BusinessTripDAO;

@WebServlet("/editApplication")
public class EditApplicationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        int appId = Integer.parseInt(request.getParameter("id"));

        HttpSession session = request.getSession();

        try {
            if ("出張費".equals(type)) {
                BusinessTripDAO dao = new BusinessTripDAO();

                // Load từng phần của dữ liệu
                Step1Data s1 = dao.loadStep1Data(appId);
                List<Step2Detail> step2List = dao.loadStep2List(appId);
                List<Step3Detail> step3List = dao.loadStep3List(appId);

                // Tính lại tổng tiền
                int total2 = step2List.stream().mapToInt(d -> Integer.parseInt(d.getExpenseTotal())).sum();
                int total3 = step3List.stream().mapToInt(d -> Integer.parseInt(d.getTransExpenseTotal())).sum();

                // Đặt vào session
                BusinessTripBean bt = new BusinessTripBean();
                bt.setStep1Data(s1);
                bt.setStep2List(step2List);
                bt.setStep3List(step3List);
                bt.setTotalStep2Amount(total2);
                bt.setTotalStep3Amount(total3);
                session.setAttribute("businessTripBean", bt);

                // chuyển hướng về step1 input
                response.sendRedirect("businessTripInputStep1");

            } else if ("交通費".equals(type)) {
                // TODO: xử lý tương ứng

            } else if ("立替金".equals(type)) {
                // TODO: xử lý tương ứng
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", "編集データの取得に失敗しました。");
            response.sendRedirect("applicationMain");
        }
    }
}