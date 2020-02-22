package com.sinco.messager.mobile.jpush;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sinco.messager.MessageDelegate;
import com.sinco.messager.mobile.MobileChannel;
import com.sinco.messager.mobile.MobileMessage;
import com.sinco.messager.mobile.MobileMessageHandler;
import com.sinco.messager.mobile.MobileOSType;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.PushPayload.Builder;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class JpushMobileMessageHandler implements MobileMessageHandler{
	
    protected static final Logger LOG = LoggerFactory.getLogger(JpushMobileMessageHandler.class);

    // demo App defined in resources/jpush-api.conf 
	private  String appKey;
	private  String masterSecret;
	private JPushClient jpushClient ;
	private String aliasFlag; //别名的标示，主要用来标示环境
	private boolean apnsProduction=true; //ios来标示环境   True 表示推送生产环境，False 表示要推送开发环境

	public static void main(String[] args) {
	    //testSendPush();
	}
	
	public JpushMobileMessageHandler(String appKey,String masterSecret, boolean apnsProduction){
		this.appKey=appKey;
		this.masterSecret=masterSecret;
		this.apnsProduction = apnsProduction;
		jpushClient=new JPushClient(masterSecret, appKey, null, ClientConfig.getInstance());
	}
	
	public JpushMobileMessageHandler(String appKey,String masterSecret){
		this.appKey=appKey;
		this.masterSecret=masterSecret;
		jpushClient=new JPushClient(masterSecret, appKey, null, ClientConfig.getInstance());
	}
	

	@Override
	public boolean sendMessage(String channel, String... message) {
		boolean result=false;
		
		String alias=channel;
		if(StringUtils.isNotBlank(aliasFlag)){
			alias=aliasFlag+alias;
		}
		
		for (String m : message) {
			 PushPayload payload =  PushPayload.newBuilder()
            .setPlatform(Platform.all())
            .setAudience(Audience.alias(alias))
            .setNotification(Notification.alert(m))
            .build();
			 result=sendPush(payload);
			 
		}
		return result;
	}

	@Override
	public void listenerMessage(MessageDelegate delegate, String channel,
			int threadNum) {
		throw new RuntimeException("该类未现实该方法");
	}

	@Override
	public void listenerMessage(MessageDelegate delegate, String channel) {
		throw new RuntimeException("该类未现实该方法");	
	}

	@Override
	public void shutdownListenerAll() {
		throw new RuntimeException("该类未现实该方法");	
	}

	@Override
	public void shutdownListener(String channel) {
		throw new RuntimeException("该类未现实该方法");
	}

	@Override
	public boolean sendMessage(MobileChannel channel, MobileOSType osType,
			MobileMessage message) {
		Builder builder= makeBuilder(osType, message);
		// 如果是别名
		if(channel instanceof AliasMobileChannel){
			builder.setAudience(Audience.alias(getAlias(channel.getChannel())));
		}else if(channel instanceof TagMobileChannel){
			builder.setAudience(Audience.tag(channel.getChannel()));
		}else{
			builder.setAudience(Audience.alias(channel.getChannel()));
		}
		PushPayload payload =builder.build();
		return sendPush(payload);
	}

	/**
	 * 得到别名
	 * @param channels
	 * @return
	 */
	private Set<String> getAlias(Set<String> channels ){
		if(StringUtils.isNotBlank(aliasFlag)){
			Set<String> alias=new HashSet<>();
			for (String c : channels) {
				alias.add(aliasFlag+c);
			}
			return alias;
		}else{
			return channels;
		}
	}
	
	@Override
	public boolean sendMessage(MobileChannel channel, MobileMessage message) {
		Builder builder= makeBuilder(MobileOSType.ALL, message);
		// 如果是别名
		if(channel instanceof AliasMobileChannel){
			builder.setAudience(Audience.alias(getAlias(channel.getChannel())));
		}else if(channel instanceof TagMobileChannel){
			builder.setAudience(Audience.tag(channel.getChannel()));
		}else{
			builder.setAudience(Audience.alias(channel.getChannel()));
		}
		PushPayload payload =builder.build();
		return sendPush(payload);
	}
	
	@Override
	public boolean sendMessage(MobileOSType osType, MobileMessage message) {
		PushPayload payload = makeBuilder(osType, message).build();
	    return sendPush(payload);
	}

	@Override
	public boolean sendMessage(MobileMessage message) {
	    PushPayload payload = makeBuilder(MobileOSType.ALL, message).build();
	    return sendPush(payload);
	}
	
	/***
	 * 推送消息
	 * @param payload
	 * @return
	 */
	private boolean sendPush(PushPayload payload ){
		try {
			PushResult result = jpushClient.sendPush(payload);
			LOG.info("Got result - " + result);

		} catch (APIConnectionException e) {
			LOG.error("Connection error. Should retry later. ", e);
			return false;
		} catch (APIRequestException e) {
			LOG.error(
					"Error response from JPush server. Should review and fix it. ");
			LOG.error("HTTP Status: " + e.getStatus());
			LOG.error("Error Code: " + e.getErrorCode());
			LOG.error("Error Message: " + e.getErrorMessage());
			LOG.error("Msg ID: " + e.getMsgId());
			return false;
		}
		return true;
	}
	
	/**
	 * 构建出jpush 的 builder对象
	 * @param osType
	 * @param message
	 * @return
	 */
	private Builder makeBuilder(MobileOSType osType,MobileMessage message){
		
		Builder builder= PushPayload.newBuilder();
		builder.setOptions(Options.newBuilder().setApnsProduction(apnsProduction).build());
		switch (message.getMessageType()) {
		case ALERT:
			switch (osType) {
			case ALL:
				builder
				.setPlatform(Platform.all())
				.setNotification(Notification.newBuilder()
						.addPlatformNotification(AndroidNotification.newBuilder()
			                    .setAlert(message.getMessage())
			                    .setTitle(message.getTitle())
			                    .addExtras(message.getExts())
			                    .build())
			             .addPlatformNotification(IosNotification.newBuilder()
									.setAlert(message.getMessage())
									.addExtras(message.getExts())
									.build()).build());
				break;
			case ANDROID:
				builder
				.setPlatform(Platform.android())
				.setNotification(Notification.android(message.getMessage(),
						message.getTitle(), message.getExts()));
				break;
			case IOS:
				//如果类型 AlertMobileMessage，设置 setBadge 和setSound
				if(message instanceof AlertMobileMessage){
					AlertMobileMessage alertMobileMessage=(AlertMobileMessage)message;
					builder
					.setPlatform(Platform.ios())
					.setNotification(Notification.newBuilder()
							.addPlatformNotification(IosNotification.newBuilder()
									.setAlert(message.getMessage())
									.setBadge(alertMobileMessage.getBadge())
									.setSound(alertMobileMessage.getSound())
									.addExtras(message.getExts())
									.build()).build());
				}else{
					builder
					.setPlatform(Platform.ios())
					.setNotification(Notification.newBuilder()
							.addPlatformNotification(IosNotification.newBuilder()
									.setAlert(message.getMessage())
									.addExtras(message.getExts())
									.build()).build());
				}
				break;
			}
			break;
		case MESSAGE:
			switch (osType) {
			case ALL:
				builder
				.setPlatform(Platform.all())
				.setMessage(Message.newBuilder()
                        .setMsgContent(message.getMessage())
                        .setTitle(message.getTitle())
                        .addExtras(message.getExts())
                        .build());
				break;
			case ANDROID:
				builder
				.setPlatform(Platform.android())
				.setMessage(Message.newBuilder()
                        .setMsgContent(message.getMessage())
                        .setTitle(message.getTitle())
                        .addExtras(message.getExts())
                        .build());
				break;
			case IOS:
				builder
				.setPlatform(Platform.ios())
				.setMessage(Message.newBuilder()
                        .setMsgContent(message.getMessage())
                        .setTitle(message.getTitle())
                        .addExtras(message.getExts())
                        .build());
				break;
			}
			break;
		}
		
		return builder;
	}

	public String getAliasFlag() {
		return aliasFlag;
	}

	public void setAliasFlag(String aliasFlag) {
		this.aliasFlag = aliasFlag;
	}

	@Override
	public boolean sendMessage(String channel, byte[] message) {
		return sendMessage(new String(message));
	}
}

