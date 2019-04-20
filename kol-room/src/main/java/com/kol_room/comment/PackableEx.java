package com.kol_room.comment;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
