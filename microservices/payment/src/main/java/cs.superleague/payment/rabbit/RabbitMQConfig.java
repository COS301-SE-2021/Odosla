package cs.superleague.payment.rabbit;

import cs.superleague.payment.PaymentService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class RabbitMQConfig {
    private final PaymentService paymentService;

    @Value("${spring.rabbitmq.host}")
    private String hostName;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;

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
    Queue SaveOrderToRepoQueue() {
        return new Queue("Q_SaveOrderToRepo", true);
    }


    //
    // BINDING
    //
    @Bean
    Binding binding(){
        //return new Binding("CatQueue", Binding.DestinationType.QUEUE, "CatExchange", "CATKEY", null);
        return BindingBuilder
                .bind(SaveOrderToRepoQueue())
                .to(PaymentExchange())
                .with("RK_SaveOrderToRepo")
                .noargs();
    }

    //
    // FACTORY
    //
    @Bean
    ConnectionFactory connectionFactory(){
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(hostName);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);

        return cachingConnectionFactory;
    }

    //
    // LISTENER
    //
    @Bean
    MessageListenerContainer messageListenerContainer(){
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory());
        simpleMessageListenerContainer.setQueues(SaveOrderToRepoQueue());                                               // <------- add all queues to listen to here
        simpleMessageListenerContainer.setMessageListener(new PaymentsListener(paymentService));
        return simpleMessageListenerContainer;
    }

}
