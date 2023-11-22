package measureus.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class HourlyConsumptionDto {
    int hour;
    double value;
}
