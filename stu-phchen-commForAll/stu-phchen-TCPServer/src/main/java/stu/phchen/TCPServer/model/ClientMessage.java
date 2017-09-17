package stu.phchen.TCPServer.model;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientMessage{
    @JSONField(serialize=false)
    private int type;
    private byte[] data;
}
