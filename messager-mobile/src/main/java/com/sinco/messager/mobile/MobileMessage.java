package com.sinco.messager.mobile;

import java.util.Map;

import com.google.common.collect.Maps;


/**
 * 移动渠道接口
 * @author james
 *
 */
public abstract class MobileMessage {

	private String message;
	
	/**
	 * 扩展字段 
	 */
	private Map<String, String> exts=Maps.newHashMap();
	
	/**
	 * 得到消息的类型
	 * @return
	 */
	public abstract MobileMessageType getMessageType();
	
	/**
	 * 标题
	 */
	private String title;
	
	
	public MobileMessage(String message){
		this.message=message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, String> getExts() {
		return exts;
	}

	public void setExts(Map<String, String> exts) {
		this.exts = exts;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
