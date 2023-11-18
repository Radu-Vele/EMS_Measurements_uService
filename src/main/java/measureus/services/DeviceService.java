package measureus.services;

import lombok.RequiredArgsConstructor;
import measureus.dtos.DeviceChangeDto;
import measureus.entities.Device;
import measureus.repositories.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;
    public void processDeviceChange(DeviceChangeDto deviceChangeDto) {
        if (deviceChangeDto.getMaxHourlyConsumption() == -1.0) { // deleted device
            this.deviceRepository.deleteById(UUID.fromString(deviceChangeDto.getDeviceId()));
            return;
        }
        Device changedDevice = Device.builder()
                .id(UUID.fromString(deviceChangeDto.getDeviceId()))
                .maxHourlyEnergyConsumption(deviceChangeDto.getMaxHourlyConsumption())
                .build();

        if (deviceChangeDto.getOwnerId() != null) {
            changedDevice.setOwnerId(UUID.fromString(deviceChangeDto.getOwnerId()));
        }
        else {
            changedDevice.setOwnerId(null);
        }

        this.deviceRepository.save(changedDevice);
    }
}
