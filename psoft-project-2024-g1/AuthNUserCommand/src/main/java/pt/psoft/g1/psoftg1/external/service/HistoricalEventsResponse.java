package pt.psoft.g1.psoftg1.external.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
public class HistoricalEventsResponse {
    private String year;
    private String month;
    private String day;
    @Getter
    private String event;
}
