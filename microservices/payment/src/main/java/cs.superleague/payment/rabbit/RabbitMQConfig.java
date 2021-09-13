package cs.superleague.payment.rabbit;

import cs.superleague.payment.PaymentService;
import org.hibernate.annotations.LazyCollection;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class RabbitMQConfig {
    private final PaymentService paymentService;

    public RabbitMQConfig(@Lazy PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    //
    // EXCHANGE
    //
    @Bean
    Exchange PaymentExchange(){
        return ExchangeBuilder.directExchange("PaymentEXCHANGE")
                .durable(true)
                .build();
    }

    //
    // QUEUE DEFINITIONS
    //
    @Bean
    Queue SaveOrderQueue() {
        return new Queue("Q_SaveOrder", true);
    }


    //
    // BINDING
    //
    @Bean
    Binding binding(){
        //return new Binding("CatQueue", Binding.DestinationType.QUEUE, "CatExchange", "CATKEY", null);
        return BindingBuilder
                .bind(SaveOrderQueue())
                .to(PaymentExchange())
                .with("RK_SaveOrder")
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
        simpleMessageListenerContainer.setQueues(SaveOrderQueue());                                               // <------- add all queues to listen to here
        simpleMessageListenerContainer.setMessageListener(new SaveOrderListener(paymentService));
        return simpleMessageListenerContainer;
    }

}
