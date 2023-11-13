package measureus.services;
import measureus.dtos.MeasurementMessageDto;
import org.springframework.stereotype.Service;

@Service
public class MeasurementService {

    public void processNewMeasurement(MeasurementMessageDto measurementMessageDto) {
        System.out.println(measurementMessageDto);
    }
}
