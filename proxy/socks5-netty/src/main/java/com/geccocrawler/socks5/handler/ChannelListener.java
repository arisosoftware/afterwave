package com.geccocrawler.socks5.handler;

import io.netty.channel.ChannelHandlerContext;

public interface ChannelListener {

	public void active(ChannelHandlerContext ctx);

	public void inActive(ChannelHandlerContext ctx);
}
 