package cs.superleague.recommendation.rabbit;

import cs.superleague.recommendation.RecommendationService;
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

    private final RecommendationService recommendationService;

    public RabbitMQConfig(@Lazy RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

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

    @Bean
    Queue RemoveRecommendationQueue() {
        return new Queue("Q_RemoveRecommendation", true);
    }

    //
    // BINDING
    //
    @Bean
    Binding bindingAddRecommendation(){
        //return new Binding("CatQueue", Binding.DestinationType.QUEUE, "CatExchange", "CATKEY", null);
        return BindingBuilder
                .bind(AddRecommendationQueue())
                .to(RecommendationExchange())
                .with("RK_AddRecommendation")
                .noargs();
    }

    @Bean
    Binding bindingRemoveRecommendation(){
        return BindingBuilder
                .bind(RemoveRecommendationQueue())
                .to(RecommendationExchange())
                .with("RK_RemoveRecommendation")
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
        simpleMessageListenerContainer.setQueues(AddRecommendationQueue(), RemoveRecommendationQueue());                                               // <------- add all queues to listen to here
        simpleMessageListenerContainer.setMessageListener(new RecommendationListener(recommendationService));
        return simpleMessageListenerContainer;
    }

}
