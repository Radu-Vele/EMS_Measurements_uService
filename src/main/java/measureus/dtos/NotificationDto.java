package measureus.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class NotificationDto {
    private UUID deviceId;
    private Double maxHourlyEnergyConsumption;
    private Double pastHourEnergyConsumption;
    private String destination;
}
