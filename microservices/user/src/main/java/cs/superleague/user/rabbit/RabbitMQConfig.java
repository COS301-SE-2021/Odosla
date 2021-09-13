package cs.superleague.user.rabbit;

import cs.superleague.user.UserService;
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
    private final UserService userService;

    public RabbitMQConfig(@Lazy UserService userService) {
        this.userService = userService;
    }

    //
    // EXCHANGE
    //
    @Bean
    Exchange UserExchange(){
        return ExchangeBuilder.directExchange("UserEXCHANGE")
                .durable(true)
                .build();
    }

    //
    // QUEUE DEFINITIONS
    //
    @Bean
    Queue SaveDriverQueue() {
        return new Queue("Q_SaveDriver", true);
    }

    @Bean
    Queue SaveShopperQueue() {
        return new Queue("Q_SaveShopper", true);
    }

    //
    // BINDING
    //
    @Bean
    Binding SaveDriverbinding(){
        //return new Binding("CatQueue", Binding.DestinationType.QUEUE, "CatExchange", "CATKEY", null);
        return BindingBuilder
                .bind(SaveDriverQueue())
                .to(UserExchange())
                .with("RK_SaveDriver")
                .noargs();
    }

    @Bean
    Binding SaveShopperbinding(){
        //return new Binding("CatQueue", Binding.DestinationType.QUEUE, "CatExchange", "CATKEY", null);
        return BindingBuilder
                .bind(SaveDriverQueue())
                .to(UserExchange())
                .with("RK_SaveShopper")
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
        simpleMessageListenerContainer.setQueues(SaveDriverQueue(), SaveShopperQueue());                                               // <------- add all queues to listen to here
        simpleMessageListenerContainer.setMessageListener(new UserListener(userService));
        return simpleMessageListenerContainer;
    }

}
