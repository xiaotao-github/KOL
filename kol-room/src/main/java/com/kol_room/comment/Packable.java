package com.kol_room.comment;

public interface Packable {
    ByteBuf marshal(ByteBuf out);
}
