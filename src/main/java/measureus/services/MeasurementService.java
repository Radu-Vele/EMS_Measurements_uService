package measureus.services;
import measureus.entities.Measurement;
import lombok.RequiredArgsConstructor;
import measureus.dtos.MeasurementMessageDto;
import measureus.repositories.MeasurementRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeasurementService {
    private final MeasurementRepository measurementRepository;

    public void processNewMeasurement(MeasurementMessageDto measurementMessageDto) {
        System.out.println(measurementMessageDto);
        this.measurementRepository.save(Measurement.builder()
                .measuredValue(measurementMessageDto.getMeasurementValue())
                .timestamp(measurementMessageDto.getTimestamp())
                .build());
    }
}
