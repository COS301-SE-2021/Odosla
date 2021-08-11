package cs.superleague.integration;

import cs.superleague.analytics.AnalyticsService;
import cs.superleague.analytics.AnalyticsServiceImpl;
import cs.superleague.payment.PaymentService;
import cs.superleague.shopping.ShoppingService;
import cs.superleague.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

@Repository("serviceSelector")
public class ServiceSelector {

    private static ServiceSelector singleton;

    @Value("${paymentService}")
    private String paymentService = "";

    @Value("${shoppingService}")
    private String shoppingService = "";

    @Value("${userService}")
    private String userService = "";

    @Value("${env.ANALYTICS_SERVICE}")
    private String analyticsService = "";

    private PaymentService payment;
    private ShoppingService shopping;
    private UserService user;
    private AnalyticsService analytics;

    @Autowired
    public ServiceSelector() {
        singleton = this;
    }

    @Autowired
    public void setServiceSelector(ApplicationContext context) {
        singleton = this;
        singleton.payment = (PaymentService) context.getBean(paymentService);
        singleton.shopping=(ShoppingService) context.getBean(shoppingService);
        singleton.user=(UserService) context.getBean(userService);
        singleton.analytics = (AnalyticsService) context.getBean(analyticsService);
    }

    public static PaymentService getPaymentService() {
        return singleton.payment;
    }

    public static ShoppingService getShoppingService(){
        return singleton.shopping;
    }

    public static UserService getUserService(){
        return singleton.user;
    }

}
