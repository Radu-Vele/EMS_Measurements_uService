package measureus.repositories;

import measureus.entities.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MeasurementRepository extends JpaRepository<Measurement, UUID> {

    @Query("SELECT SUM(m.measuredValue) FROM Measurement m WHERE (m.timestamp BETWEEN :from AND :to) AND m.deviceId = :deviceId")
    Optional<Double> getConsumptionInPastHour(@Param("deviceId") UUID deviceId,
                                              @Param("from") long from,
                                              @Param("to") long to);
}
