package com.sinco.messager.mobile.jpush;

import com.sinco.messager.mobile.MobileMessage;
import com.sinco.messager.mobile.MobileMessageType;

/**
 * 消息类型
 * @author james
 *
 */
public class MessageMobileMessage extends MobileMessage{

	public MessageMobileMessage(String message) {
		super(message);
	}

	@Override
	public MobileMessageType getMessageType() {
		return MobileMessageType.MESSAGE;
	}
}
