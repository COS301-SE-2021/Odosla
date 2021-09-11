package cs.superleague.recommendation.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //
    // EXCHANGE
    //
    @Bean
    Exchange RecommendationExchange(){
        return ExchangeBuilder.directExchange("RecommendationEXCHANGE")
                .durable(true)
                .build();
    }

    //
    // QUEUE DEFINITIONS
    //
    @Bean
    Queue AddRecommendationQueue() {
        return new Queue("Q_AddRecommendation", true);
    }


    //
    // BINDING
    //
    @Bean
    Binding binding(){
        //return new Binding("CatQueue", Binding.DestinationType.QUEUE, "CatExchange", "CATKEY", null);
        return BindingBuilder
                .bind(AddRecommendationQueue())
                .to(RecommendationExchange())
                .with("RK_AddRecommendation")
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
        simpleMessageListenerContainer.setQueues(AddRecommendationQueue());                                               // <------- add all queues to listen to here
        simpleMessageListenerContainer.setMessageListener(new AddRecommendationListener());
        return simpleMessageListenerContainer;
    }

}
