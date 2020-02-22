package com.sinco.messager.mobile;

import com.sinco.messager.MessageHandler;

/**
 * 移动消息推送接口
 * @author james
 *
 */
public interface MobileMessageHandler extends MessageHandler{
	/**
	 * 发送一个消息到通道,区分系统类型
	 * @param channel
	 * @param message
	 */
	public boolean sendMessage(final MobileChannel channel,final MobileOSType osType,
			final MobileMessage  message);
	
	/**
	 * 发送一个消息到通道
	 * @param channel
	 * @param message
	 */
	public boolean sendMessage(final MobileChannel channel,final MobileMessage  message);
	
	/**
	 * 发送一个消息到所有设备,区分系统类型
	 * @param osType
	 * @param message
	 * @return
	 */
	public boolean sendMessage(final MobileOSType osType,final MobileMessage  message);
	/**
	 * 发送一个消息所有设备
	 * @param message
	 */
	public boolean sendMessage(final MobileMessage message);

}
