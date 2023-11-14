package measureus.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class DeviceChangeDto {
    @JsonProperty("deviceId")
    private String deviceId;
    @JsonProperty("maxHourlyConsumption")
    private Double maxHourlyConsumption;
}
