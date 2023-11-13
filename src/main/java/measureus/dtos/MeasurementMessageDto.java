package measureus.dtos;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class MeasurementMessageDto {
    @JsonProperty("timestamp")
    private Long timestamp;
    @JsonProperty("device_id")
    private String deviceId;
    @JsonProperty("measurement_value")
    private Double measurementValue;
}
