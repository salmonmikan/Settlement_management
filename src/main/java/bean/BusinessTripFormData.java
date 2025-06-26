// Filename: src/main/java/bean/BusinessTripFormData.java
package bean;

import java.io.Serializable;
import bean.BusinessTripBean.BusinessTripBean;

public class BusinessTripFormData implements Serializable {
    private static final long serialVersionUID = 1L;

    private BusinessTripBean businessTripBean;

    public BusinessTripBean getBusinessTripBean() {
        return businessTripBean;
    }

    public void setBusinessTripBean(BusinessTripBean businessTripBean) {
        this.businessTripBean = businessTripBean;
    }
}