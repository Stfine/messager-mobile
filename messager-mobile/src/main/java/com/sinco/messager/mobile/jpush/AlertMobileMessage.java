package com.sinco.messager.mobile.jpush;

import java.util.Map;

import com.google.common.collect.Maps;
import com.sinco.messager.mobile.MobileMessage;
import com.sinco.messager.mobile.MobileMessageType;

public class AlertMobileMessage extends MobileMessage{

	public AlertMobileMessage(String message) {
		super(message);
	}
	
	/**
	 * 标题
	 */
	private String title;
	
	/**
	 * 通知的声音
	 */
	private String sound;
	
	/**
	 * 角标数
	 */
	private Integer badge;
	

	public Integer getBadge() {
		return badge;
	}

	public void setBadge(Integer badge) {
		this.badge = badge;
	}

	public String getSound() {
		return sound;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public MobileMessageType getMessageType() {
		return MobileMessageType.ALERT;
	}

}
