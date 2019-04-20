package com.kol_room.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Microphones{
      String room_id;
//      第一个麦克风
      UserDetail firstPhone;
//      第二个麦克风，下面以此类推
      UserDetail secondPhone;
      UserDetail thirdPhone;
      UserDetail fourthPhone;
      UserDetail fifthPhone;
      UserDetail sixthPhone;
      UserDetail seventhPhone;
      UserDetail eighthPhone;
}
