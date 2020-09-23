
package com.arloor.socks5.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

public final class RelayOverHttpResponseHandler extends ChannelOutboundHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(RelayOverHttpResponseHandler.class);
	private final static String fakeHost = "qtgwuehaoisdhuaishdaisuhdasiuhlassjd.com";

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (msg instanceof ByteBuf) {
			ByteBuf content = (ByteBuf) msg;
			if (content.readableBytes() == 0) {
				ctx.writeAndFlush(content, promise);
			} else {
				ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();
				buf.writeBytes(
						"HTTP/1.1 200 OK\r\nContent-Type: text/plain; charset=utf-8\r\nContent-Length: ".getBytes());
				buf.writeBytes(String.valueOf(content.readableBytes()).getBytes());
				buf.writeBytes("\r\n\r\n".getBytes());
				while (content.isReadable()) {
					buf.writeByte(~content.readByte());
				}
				ctx.writeAndFlush(buf, promise);
				ReferenceCountUtil.release(content);
			}
		}
	}

}
