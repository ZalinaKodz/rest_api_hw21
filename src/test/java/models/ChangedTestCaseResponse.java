package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangedTestCaseResponse {
    private int id;
    private String name;
    private boolean automated;
    private Date createdDate;
    private String statusName;
    private String statusColor;

}
