package measureus.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import measureus.dtos.DeviceChangeDto;
import measureus.services.DeviceService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class DevicesChangeListener {
    private final ObjectMapper objectMapper;
    private final DeviceService deviceService;

    @RabbitListener(queues = {"${spring.rabbitmq.devices_change_queue}"})
    public void onDevicesChangeReceived(String deviceChangeEntry, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            DeviceChangeDto deviceChangeDto = objectMapper.readValue(deviceChangeEntry, DeviceChangeDto.class);
            this.deviceService.processDeviceChange(deviceChangeDto);
        } catch (JsonProcessingException e) {
            channel.basicNack(tag, false, false);
        }
    }
}
