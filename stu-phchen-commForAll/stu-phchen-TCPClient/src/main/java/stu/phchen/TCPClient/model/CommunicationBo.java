package stu.phchen.TCPClient.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunicationBo {
    private String myName;
    private String oppisiteName;
    private String messageContent;
}
