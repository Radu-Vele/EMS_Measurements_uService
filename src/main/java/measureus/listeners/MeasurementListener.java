package measureus.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import measureus.dtos.MeasurementMessageDto;
import lombok.RequiredArgsConstructor;
import measureus.services.MeasurementService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MeasurementListener {
    private final MeasurementService measurementService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = {"${spring.rabbitmq.measurements_queue}"})
    public void onMeasurementDataReceived(String measurementEntry, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            MeasurementMessageDto measurementMessageDto = objectMapper.readValue(measurementEntry, MeasurementMessageDto.class);
            measurementService.processNewMeasurement(measurementMessageDto);
        } catch (JsonProcessingException e) {
            channel.basicNack(tag, false, false);
        }
    }
}
