package com.kol_room.dto;

import io.agora.signal.Signal;
import io.agora.signal.Signal.LoginSession;
import io.agora.signal.Signal.LoginSession.Channel;
import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class User {
	public User(LoginSession session, String account) {
		super();
		this.session = session;
		this.account = account;
	}
	
	private Signal.LoginSession session;
	private Channel channel;
	private String account;

}
