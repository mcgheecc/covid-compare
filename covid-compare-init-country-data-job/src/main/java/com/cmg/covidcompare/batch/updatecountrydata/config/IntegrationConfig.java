package com.cmg.covidcompare.batch.updatecountrydata.config;

import com.cmg.covidcompare.domain.CountryData;
import com.cmg.covidcompare.domain.CountryDate;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.batch.core.step.item.SimpleChunkProcessor;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.integration.chunk.ChunkHandler;
import org.springframework.batch.integration.chunk.ChunkMessageChannelItemWriter;
import org.springframework.batch.integration.chunk.ChunkProcessorChunkHandler;
import org.springframework.batch.integration.chunk.RemoteChunkHandlerFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.amqp.inbound.AmqpInboundChannelAdapter;
import org.springframework.integration.amqp.outbound.AmqpOutboundEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessagingTemplate;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;
import org.springframework.scheduling.support.PeriodicTrigger;

@Configuration
public class IntegrationConfig {

    private static final String CHUNKING_REQUESTS = "chunking.requests";
    private static final String CHUNKING_REPLIES = "chunking.replies";

    @Bean
    public ChunkHandler chunkHandler(TaskletStep step1) throws Exception {
        RemoteChunkHandlerFactoryBean factoryBean = new RemoteChunkHandlerFactoryBean();

        factoryBean.setChunkWriter(chunkWriter());
        factoryBean.setStep(step1);

        return factoryBean.getObject();
    }

    @Bean
    public ChunkMessageChannelItemWriter chunkWriter() {
        ChunkMessageChannelItemWriter chunkWriter = new ChunkMessageChannelItemWriter();
        chunkWriter.setMessagingOperations(messageTemplate());
        chunkWriter.setReplyChannel(inboundReplies());
        chunkWriter.setMaxWaitTimeouts(10);
        return chunkWriter;
    }

    @Bean
    public MessagingTemplate messageTemplate() {
        MessagingTemplate messagingTemplate = new MessagingTemplate(outboundRequests());
        messagingTemplate.setReceiveTimeout(60000000L);

        return messagingTemplate;
    }

    @Bean
    @ServiceActivator(inputChannel = "outboundRequests")
    public AmqpOutboundEndpoint amqpOutboundEndpoint(AmqpTemplate template) {
        AmqpOutboundEndpoint endpoint = new AmqpOutboundEndpoint(template);

        endpoint.setExpectReply(false);
        endpoint.setOutputChannel(inboundReplies());

        endpoint.setRoutingKey(CHUNKING_REQUESTS);

        return endpoint;
    }

    @Bean
    public MessageChannel outboundRequests() {
        return new DirectChannel();
    }

    @Bean
    public Queue requestQueue() {
        return new Queue(CHUNKING_REQUESTS, false);
    }

    @Bean
    @Profile("slave")
    public AmqpInboundChannelAdapter inboundRequestsAdapter(SimpleMessageListenerContainer listenerContainer) {
        AmqpInboundChannelAdapter adapter = new AmqpInboundChannelAdapter(listenerContainer);
        adapter.setOutputChannel(inboundRequests());
        adapter.afterPropertiesSet();
        return adapter;
    }

    @Bean
    public MessageChannel inboundRequests() {
        return new DirectChannel();
    }

    @Bean
    @Profile("slave")
    @ServiceActivator(inputChannel = "inboundRequests", outputChannel = "outboundReplies")
    public ChunkProcessorChunkHandler<CountryDate> chunkProcessorChunkHandler(ItemProcessor<CountryDate, CountryData> itemProcessor,
        @Qualifier("updateCountryDataWriter") ItemWriter<CountryData> itemWriter) throws Exception {
        SimpleChunkProcessor<CountryDate, CountryData> chunkProcessor = new SimpleChunkProcessor<>(itemProcessor, itemWriter);
        chunkProcessor.afterPropertiesSet();

        ChunkProcessorChunkHandler<CountryDate> chunkHandler = new ChunkProcessorChunkHandler<>();
        chunkHandler.setChunkProcessor(chunkProcessor);
        chunkHandler.afterPropertiesSet();
        return chunkHandler;
    }

    @Bean
    public QueueChannel outboundReplies() {
        return new QueueChannel();
    }

    @Bean
    @Profile("slave")
    @ServiceActivator(inputChannel = "outboundReplies")
    public AmqpOutboundEndpoint amqpOutboundEndpointReplies(AmqpTemplate template) {
        AmqpOutboundEndpoint endpoint = new AmqpOutboundEndpoint(template);

        endpoint.setExpectReply(false);

        endpoint.setRoutingKey(CHUNKING_REPLIES);

        return endpoint;
    }

    @Bean
    public Queue replyQueue() {
        return new Queue(CHUNKING_REPLIES, false);
    }

    @Bean
    public QueueChannel inboundReplies() {
        return new QueueChannel();
    }

    @Bean
    @Profile("master")
    public AmqpInboundChannelAdapter inboundRepliesAdapter(SimpleMessageListenerContainer listenerContainer) {
        AmqpInboundChannelAdapter adapter = new AmqpInboundChannelAdapter(listenerContainer);

        adapter.setOutputChannel(inboundReplies());

        adapter.afterPropertiesSet();

        return adapter;
    }

    @Bean
    @Profile("slave")
    public SimpleMessageListenerContainer requestContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container =
            new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames(CHUNKING_REQUESTS);
        container.setAutoStartup(false);
        return container;
    }

    @Bean
    @Profile("master")
    public SimpleMessageListenerContainer replyContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container =
            new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames(CHUNKING_REPLIES);
        container.setAutoStartup(false);

        return container;
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata defaultPoller() {
        PollerMetadata pollerMetadata = new PollerMetadata();
        pollerMetadata.setTrigger(new PeriodicTrigger(10));
        return pollerMetadata;
    }
}

