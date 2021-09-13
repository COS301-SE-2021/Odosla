package cs.superleague.notification.rabbit;

import cs.superleague.notification.NotificationService;
import cs.superleague.notification.NotificationService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class RabbitMQConfig {
    private final NotificationService notificationService;

    public RabbitMQConfig(@Lazy NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    //
    // EXCHANGE
    //
    @Bean
    Exchange PaymentExchange(){
        return ExchangeBuilder.directExchange("NotificationEXCHANGE")
                .durable(true)
                .build();
    }

    //
    // QUEUE DEFINITIONS
    //
    @Bean
    Queue SendDirectEmailQueue() {
        return new Queue("Q_SendDirectEmailNotification", true);
    }


    //
    // BINDING
    //
    @Bean
    Binding SendDirectEmailBinding(){
        //return new Binding("CatQueue", Binding.DestinationType.QUEUE, "CatExchange", "CATKEY", null);
        return BindingBuilder
                .bind(SendDirectEmailQueue())
                .to(PaymentExchange())
                .with("RK_SendDirectEmailNotification")
                .noargs();
    }

    //
    // FACTORY
    //
    @Bean
    ConnectionFactory connectionFactory(){
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("localhost");
        cachingConnectionFactory.setUsername("guest");
        cachingConnectionFactory.setPassword("guest");

        return cachingConnectionFactory;
    }

    //
    // LISTENER
    //
    @Bean
    MessageListenerContainer messageListenerContainer(){
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory());
        simpleMessageListenerContainer.setQueues(SendDirectEmailQueue());                                               // <------- add all queues to listen to here
        simpleMessageListenerContainer.setMessageListener(new NotificationListener(notificationService));
        return simpleMessageListenerContainer;
    }

}
