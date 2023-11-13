package measureus.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private Long timestamp;

    private Double measuredValue;
}
