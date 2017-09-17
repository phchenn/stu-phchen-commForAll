package stu.phchen.TCPClient.constant;

import java.util.Arrays;
import java.util.List;

public class MessageContent {
     /**
     * @Fields DEFAULT_MAX_FRAME_LENGTH : 默认报文最大长度
     */
     public static final int DEFAULT_MAX_FRAME_LENGTH = Integer.MAX_VALUE ;
     
     
     /**
     * @Fields DEFAULT_LENGTH_FIELD_OFFSET : 默认长度字段偏移量
     */
     public static final int DEFAULT_LENGTH_FIELD_OFFSET = 1 ;
     
     
     /**
     * @Fields DEFAULT_LENGTH_FIELD_LENGTH : 默认长度字段长度
     */
     public static final int DEFAULT_LENGTH_FIELD_LENGTH = 4 ;
     
     /**
      * @Fields HEARTBEAT : 心跳包，整包内容
      */
      public static final byte HEARTBEAT = 0x01 ;
      
      public static final byte CERTIFICATION = 0x02;
      
      public static final byte COMMUNICATION = 0x03;
      
      public static final byte TO_COMMUNICATION = 0x04;
      /**
       * @Fields CLIENT_SERVER : 支持 客户端--->服务端 报文类型集合
       */
      public static final List<Byte> SERVER_CLIENT = Arrays.asList(HEARTBEAT,TO_COMMUNICATION);
}