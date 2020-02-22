import java.util.Map;

import com.google.common.collect.Maps;
import com.sinco.messager.mobile.MobileMessageHandler;
import com.sinco.messager.mobile.jpush.AlertMobileMessage;
import com.sinco.messager.mobile.jpush.AliasMobileChannel;
import com.sinco.messager.mobile.jpush.JpushMobileMessageHandler;


public class MessageTest {

	public static void main(String[] args) {
		MobileMessageHandler messageHandler=new JpushMobileMessageHandler("c6257ea11c5218943556df10", 
				"ccec7df7346cbf10ca1d8b0b");
		
		AlertMobileMessage message=new AlertMobileMessage("您的实名认证申请已经被拒，请及时登录app查看原因！");
		
		//附加参数，需要什么自己加
		Map<String, String> exts=Maps.newHashMap();
		exts.put("msgType", "10003");
		message.setExts(exts);
		message.setTitle("用户消息");
		message.setSound("default");

		//消息的渠道分为 Alias 和 tag ,消息内容分为 alert 和 message
		messageHandler.sendMessage(new AliasMobileChannel("66380aecf769471483fa0f4da5b75cc0"),message);
	}
}
