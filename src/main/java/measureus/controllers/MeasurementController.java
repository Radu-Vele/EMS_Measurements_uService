package measureus.controllers;

import lombok.RequiredArgsConstructor;
import measureus.dtos.HourlyConsumptionDto;
import measureus.services.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/measurements")
public class MeasurementController {
    private final MeasurementService measurementService;
    @GetMapping("/getConsumptionForDay")
    public ResponseEntity<List<HourlyConsumptionDto>> getConsumptionForDay(
            @RequestParam long dayTimestamp, @RequestParam String userId) {
        return new ResponseEntity<>(this.measurementService.getHourlyConsumption(dayTimestamp, userId), HttpStatus.OK);
    }
}
