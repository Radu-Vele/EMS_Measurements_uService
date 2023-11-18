package measureus.services;
import measureus.entities.Device;
import measureus.entities.Measurement;
import lombok.RequiredArgsConstructor;
import measureus.dtos.MeasurementMessageDto;
import measureus.messaging.WebSocketHandler;
import measureus.repositories.DeviceRepository;
import measureus.repositories.MeasurementRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MeasurementService {
    private final MeasurementRepository measurementRepository;
    private final Map<UUID, WebSocketSession> usersLoggedIn = new HashMap<>();
    private final DeviceRepository deviceRepository;
    private final WebSocketHandler webSocketHandler;

    private double getPastHourConsumption(Measurement measurement) {
        long HOUR_IN_MILLISECONDS = 3600000;
        long oneHourAgoTimestamp = measurement.getTimestamp() - HOUR_IN_MILLISECONDS;
        double pastHourConsumption = measurement.getMeasuredValue();
        return pastHourConsumption + this.measurementRepository.getConsumptionInPastHour(measurement.getDeviceId(),
                oneHourAgoTimestamp,
                measurement.getTimestamp()).orElse(0.0);
    }

    public void processNewMeasurement(MeasurementMessageDto measurementMessageDto) {
        System.out.println(measurementMessageDto);
        Measurement newMeasurement = Measurement.builder()
                .measuredValue(measurementMessageDto.getMeasurementValue())
                .timestamp(measurementMessageDto.getTimestamp())
                .deviceId(UUID.fromString(measurementMessageDto.getDeviceId()))
                .build();
        double pastHourConsumption = getPastHourConsumption(newMeasurement);
        newMeasurement.setPastHourConsumption(pastHourConsumption);

        Device correspondingDevice = this.deviceRepository
                .findById(newMeasurement.getDeviceId())
                .orElseThrow(() -> new IllegalArgumentException("The device id of the measurement is not in the database"));

        if (pastHourConsumption > correspondingDevice.getMaxHourlyEnergyConsumption()) {
            if (webSocketHandler.getUserIdSessionMap().containsKey(correspondingDevice.getOwnerId().toString())) {
                WebSocketSession userIdSession = webSocketHandler.getUserIdSessionMap().get(correspondingDevice.getOwnerId().toString());
                this.webSocketHandler.sendMessageToSession(userIdSession, String
                        .format("The device with id %s exceeded its hourly consumption\n max: %f actual: %f",
                                correspondingDevice.getId(),
                                correspondingDevice.getMaxHourlyEnergyConsumption(),
                                pastHourConsumption));
            }
        }

        this.measurementRepository.save(newMeasurement);
    }
}
