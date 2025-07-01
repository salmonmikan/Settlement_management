package listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import dao.EmployeeDAO;

@WebListener
public class SystemInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            EmployeeDAO dao = new EmployeeDAO();
            dao.initAdminAccount();
            System.out.println("adminアカウントの検索が終了！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
