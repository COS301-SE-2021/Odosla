package cs.superleague.integration;

import cs.superleague.notification.NotificationService;
import cs.superleague.notification.dataclass.Notification;
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

//    @Value("${env.NOTIFICATION_SERVICE}")
//    private String notificationService = "";

    @Value("${paymentService}")
    private String paymentService = "";

    @Value("${shoppingService}")
    private String shoppingService = "";

    @Value("${env.USER_SERVICE}")
    private String userService = "";

//    private NotificationService notification;
    private PaymentService payment;
    private ShoppingService shopping;
    private UserService user;
    @Autowired
    public ServiceSelector() {
        singleton = this;
    }

    @Autowired
    public void setServiceSelector(ApplicationContext context) {
        singleton = this;
//        singleton.notification = (NotificationService) context.getBean(notificationService);
        singleton.payment = (PaymentService) context.getBean(paymentService);
        singleton.shopping=(ShoppingService) context.getBean(shoppingService);
        singleton.user=(UserService) context.getBean(userService);

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

//    public static NotificationService getNotificationService(){
//        return singleton.notification;
//    }
}
