package measureus.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String description;

    private String address;

    @Column(nullable = false)
    private Long maxHourlyEnergyConsumption;
}
