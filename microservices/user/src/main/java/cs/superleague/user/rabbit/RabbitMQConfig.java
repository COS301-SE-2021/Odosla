package cs.superleague.user.rabbit;

import cs.superleague.user.UserService;
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
    private final UserService userService;

    @Value("${spring.rabbitmq.host}")
    private String hostName;
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;

    public RabbitMQConfig(@Lazy UserService userService) {
        this.userService = userService;
    }

    //
    // EXCHANGE
    //
    @Bean
    Exchange UserExchange() {
        return ExchangeBuilder.directExchange("UserEXCHANGE")
                .durable(true)
                .build();
    }

    //
    // QUEUE DEFINITIONS
    //
    @Bean
    Queue SaveDriverToRepoQueue() {
        return new Queue("Q_SaveDriverToRepo", true);
    }

    @Bean
    Queue SaveShopperToRepoQueue() {
        return new Queue("Q_SaveShopperToRepo", true);
    }

    @Bean
    Queue RemoveProblemFromRepoQueue() {
        return new Queue("Q_RemoveProblemFromRepo", true);
    }

    @Bean
    Queue SaveCustomerToRepoQueue() {
        return new Queue("Q_SaveCustomerToRepoTest", true);
    }
    //
    // BINDING
    //
    @Bean
    Binding SaveDriverToRepoBinding() {
        //return new Binding("CatQueue", Binding.DestinationType.QUEUE, "CatExchange", "CATKEY", null);
        return BindingBuilder
                .bind(SaveDriverToRepoQueue())
                .to(UserExchange())
                .with("RK_SaveDriverToRepo")
                .noargs();
    }

    @Bean
    Binding SaveShopperToRepoBinding() {
        //return new Binding("CatQueue", Binding.DestinationType.QUEUE, "CatExchange", "CATKEY", null);
        return BindingBuilder
                .bind(SaveDriverToRepoQueue())
                .to(UserExchange())
                .with("RK_SaveShopperToRepo")
                .noargs();
    }

    @Bean
    Binding RemoveProblemFromRepoBinding() {
        //return new Binding("CatQueue", Binding.DestinationType.QUEUE, "CatExchange", "CATKEY", null);
        return BindingBuilder
                .bind(RemoveProblemFromRepoQueue())
                .to(UserExchange())
                .with("RK_RemoveProblemFromRepo")
                .noargs();
    }

    @Bean
    Binding SaveCustomerToRepoBinding() {
        return BindingBuilder
                .bind(SaveDriverToRepoQueue())
                .to(UserExchange())
                .with("RK_SaveCustomerToRepoTest")
                .noargs();
    }

    //
    // FACTORY
    //
    @Bean
    ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(hostName);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);

        return cachingConnectionFactory;
    }

    //
    // LISTENER
    //
    @Bean
    MessageListenerContainer messageListenerContainer() {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setConnectionFactory(connectionFactory());
        simpleMessageListenerContainer.setQueues(SaveDriverToRepoQueue(), SaveShopperToRepoQueue(), RemoveProblemFromRepoQueue(), SaveCustomerToRepoQueue());                                               // <------- add all queues to listen to here
        simpleMessageListenerContainer.setMessageListener(new UserListener(userService));
        return simpleMessageListenerContainer;
    }

}