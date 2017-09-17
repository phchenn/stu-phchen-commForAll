package stu.phchen.TCPClient.model;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientPushMessage {
    @JSONField(serialize=false)
    private int type;
    private byte[] data;
}
