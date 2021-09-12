package cs.superleague.shopping.rabbit;

import cs.superleague.shopping.ShoppingService;
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

    private final ShoppingService shoppingService;

    public RabbitMQConfig(@Lazy ShoppingService shoppingService) {
        this.shoppingService = shoppingService;
    }

    //
    // EXCHANGE
    //
    @Bean
    Exchange ShoppingExchange(){
        return ExchangeBuilder.directExchange("ShoppingEXCHANGE")
                .durable(true)
                .build();
    }

    //
    // QUEUE DEFINITIONS
    //
    @Bean
    Queue SaveStoreToRepoQueue() {
        return new Queue("Q_SaveStoreToRepo", true);
    }

    @Bean
    Queue SaveItemToRepoQueue() {
        return new Queue("Q_SaveItemToRepo", true);
    }

    //
    // BINDING
    //
    @Bean
    Binding bindingSaveStoreToRepo(){

        return BindingBuilder
                .bind(SaveStoreToRepoQueue())
                .to(ShoppingExchange())
                .with("RK_SaveStoreToRepo")
                .noargs();
    }

    @Bean
    Binding bindingSaveItemToRepo(){

        return BindingBuilder
                .bind(SaveItemToRepoQueue())
                .to(ShoppingExchange())
                .with("RK_SaveItemToRepo")
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
        simpleMessageListenerContainer.setQueues(SaveStoreToRepoQueue(), SaveItemToRepoQueue());
        simpleMessageListenerContainer.setMessageListener(new ShoppingListener(shoppingService));
        return simpleMessageListenerContainer;
    }

}
