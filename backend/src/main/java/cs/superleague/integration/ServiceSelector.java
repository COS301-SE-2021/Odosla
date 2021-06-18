package cs.superleague.integration;

import cs.superleague.payment.PaymentService;
import cs.superleague.shopping.ShoppingService;
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

    private PaymentService payment;
    private ShoppingService shopping;

    @Autowired
    public ServiceSelector() {
        singleton = this;
    }

    @Autowired
    public void setServiceSelector(ApplicationContext context) {
        singleton = this;
        singleton.payment = (PaymentService) context.getBean(paymentService);
        singleton.shopping=(ShoppingService) context.getBean(shoppingService);

    }

    public static PaymentService getPaymentService() {
        return singleton.payment;
    }

    public static ShoppingService getShoppingService(){
        return singleton.shopping;
    }

}