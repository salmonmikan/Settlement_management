package listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppConfig implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // set timeout 30 phút (30 x 60 giây)
        sce.getServletContext().setSessionTimeout(30);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // không cần xử lý gì ở đây
    }
}