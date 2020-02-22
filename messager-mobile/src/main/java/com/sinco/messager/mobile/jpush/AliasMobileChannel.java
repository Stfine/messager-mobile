package com.sinco.messager.mobile.jpush;

import java.util.Set;

import com.google.common.collect.Sets;
import com.sinco.messager.mobile.MobileChannel;

/**
 * 别名
 * @author james
 *
 */
public class AliasMobileChannel implements MobileChannel{

	private Set<String> aliass;
	
	public AliasMobileChannel(String ... aliass){
		this.aliass=Sets.newHashSet(aliass);
	}
	
	@Override
	public Set<String> getChannel() {
		return aliass;
	}

}
