//package cs.superleague.integration;
//
//import cs.superleague.delivery.DeliveryService;
//import cs.superleague.analytics.AnalyticsService;
//import cs.superleague.importer.ImporterService;
//import cs.superleague.notification.NotificationService;
//import cs.superleague.payment.PaymentService;
//import cs.superleague.recommendation.RecommendationService;
//import cs.superleague.shopping.ShoppingService;
//import cs.superleague.user.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Repository;
//
//@Repository("serviceSelector")
//public class ServiceSelector {
//
//    private static ServiceSelector singleton;
//
//    @Value("${notificationService}")
//    private String notificationService = "";
//
//    @Value("${paymentService}")
//    private String paymentService = "";
//
//    @Value("${shoppingService}")
//    private String shoppingService = "";
//
//    @Value("${userService}")
//    private String userService = "";
//
//    @Value("${deliveryService}")
//    private String deliveryService = "";
//
//    @Value("${env.ANALYTICS_SERVICE}")
//    private String analyticsService = "";
//
//    @Value("${env.IMPORTER_SERVICE}")
//    private String importerService = "";
//
//    @Value("${recommendationService}")
//    private String recommendationService = "";
//
//    private NotificationService notification;
//    private PaymentService payment;
//    private ShoppingService shopping;
//    private UserService user;
//    private DeliveryService delivery;
//    private AnalyticsService analytics;
//    private ImporterService importer;
//    private RecommendationService recommendation;
//
//    @Autowired
//    public ServiceSelector() {
//        singleton = this;
//    }
//
//    @Autowired
//    public void setServiceSelector(ApplicationContext context) {
//        singleton = this;
//        singleton.notification = (NotificationService) context.getBean(notificationService);
//        singleton.payment = (PaymentService) context.getBean(paymentService);
//        singleton.shopping=(ShoppingService) context.getBean(shoppingService);
//        singleton.user=(UserService) context.getBean(userService);
//        singleton.delivery=(DeliveryService) context.getBean(deliveryService);
//        singleton.analytics = (AnalyticsService) context.getBean(analyticsService);
//        singleton.importer= (ImporterService) context.getBean(importerService);
//        singleton.recommendation = (RecommendationService) context.getBean(recommendationService);
//    }
//
//    public static PaymentService getPaymentService() {
//        return singleton.payment;
//    }
//
//    public static ShoppingService getShoppingService(){
//        return singleton.shopping;
//    }
//
//    public static UserService getUserService(){
//        return singleton.user;
//    }
//
//    public static NotificationService getNotificationService(){
//        return singleton.notification;
//    }
//
//    public static DeliveryService getDeliveryService(){
//        return singleton.delivery;
//    }
//
//    public static AnalyticsService getAnalyticsService(){
//        return singleton.analytics;
//    }
//
//    public static ImporterService getImporterService(){
//        return singleton.importer;
//    }
//
//    public static RecommendationService getRecommendationService(){
//        return singleton.recommendation;
//    }
//}
