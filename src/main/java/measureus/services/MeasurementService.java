package measureus.services;
import measureus.entities.Measurement;
import lombok.RequiredArgsConstructor;
import measureus.dtos.MeasurementMessageDto;
import measureus.repositories.MeasurementRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MeasurementService {
    private final MeasurementRepository measurementRepository;

    private double getPastHourConsumption(Measurement measurement) {
        long HOUR_IN_SEC = 3600;
        Long oneHourAgoTimeStamp = measurement.getTimestamp() - HOUR_IN_SEC;
        double pastHourConsumption = measurement.getMeasuredValue();
        return pastHourConsumption + this.measurementRepository.getConsumptionInPastHour(measurement.getDeviceId(),
                oneHourAgoTimeStamp,
                measurement.getTimestamp()).orElse(0.0);
    }

    public void processNewMeasurement(MeasurementMessageDto measurementMessageDto) {
        System.out.println(measurementMessageDto);
        Measurement newMeasurement = Measurement.builder()
                .measuredValue(measurementMessageDto.getMeasurementValue())
                .timestamp(measurementMessageDto.getTimestamp())
                .deviceId(UUID.fromString(measurementMessageDto.getDeviceId()))
                .build();
        newMeasurement.setPastHourConsumption(getPastHourConsumption(newMeasurement));
        this.measurementRepository.save(newMeasurement);
    }
}
