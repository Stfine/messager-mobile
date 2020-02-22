package com.sinco.messager.mobile.jpush;

import java.util.Set;

import com.google.common.collect.Sets;
import com.sinco.messager.mobile.MobileChannel;

/**
 * 别名
 * @author james
 *
 */
public class TagMobileChannel implements MobileChannel{

	private Set<String> tags;
	
	public TagMobileChannel(String ... tags){
		this.tags=Sets.newHashSet(tags);
	}
	
	@Override
	public Set<String> getChannel() {
		return tags;
	}

}
