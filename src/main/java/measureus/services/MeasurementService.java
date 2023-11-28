package measureus.services;
import measureus.dtos.HourlyConsumptionDto;
import measureus.entities.Device;
import measureus.entities.Measurement;
import lombok.RequiredArgsConstructor;
import measureus.dtos.MeasurementMessageDto;
import measureus.messaging.WebSocketHandler;
import measureus.repositories.DeviceRepository;
import measureus.repositories.MeasurementRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;

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
            } // TODO: should the websocket message block our transaction? Nope, fix it.
        }

        this.measurementRepository.save(newMeasurement);
    }

    public List<HourlyConsumptionDto> getHourlyConsumption(long dayTimestamp, String userId) {
        UUID userUuid = UUID.fromString(userId);
        long HOUR_IN_MILLIS = 3600000;
        long DAY_IN_MILLIS = 86400000;
        List<HourlyConsumptionDto> hourlyConsumptionDtoList = new ArrayList<>();
        long from = dayTimestamp;
        long to = from + HOUR_IN_MILLIS;
        for(int i = 0; i < 24; i++) {
            // get hour consumption
            double consumption = this.measurementRepository.getUserConsumptionBetween(userUuid, from, to)
                    .orElse(0.0);
            hourlyConsumptionDtoList.add(HourlyConsumptionDto.builder()
                    .hour(i)
                    .value(consumption)
                    .build());
            from = to;
            to += HOUR_IN_MILLIS;
        }
        return hourlyConsumptionDtoList;
    }
}
