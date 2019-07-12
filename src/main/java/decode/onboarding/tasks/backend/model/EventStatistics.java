package decode.onboarding.tasks.backend.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventStatistics {

    private Long count;
    private Double averageDuration;
    private Double hourUtilization;
}
