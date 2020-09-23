package com.arloor.socks5.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arloor.socks5.common.RelayHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ReferenceCountUtil;

public class InitHandler extends ChannelInboundHandlerAdapter {
	private static Logger logger = LoggerFactory.getLogger(InitHandler.class.getSimpleName());
	private SocketChannel remoteChannel;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof Request) {
			Request request = (Request) msg;
			if (request.getPayload() != null && request.getPayload().readableBytes() == 4
					&& request.getPayload().readByte() == 'i' && request.getPayload().readByte() == 'n'
					&& request.getPayload().readByte() == 'i' && request.getPayload().readByte() == 't') {
				ReferenceCountUtil.release(request.getPayload());
				Bootstrap bootstrap = new Bootstrap();
				bootstrap.group(ServerBootstrap.workerGroup).channel(NioSocketChannel.class)
						.option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
						.handler(new ChannelInitializer<SocketChannel>() {
							@Override
							protected void initChannel(SocketChannel ch) throws Exception {
								remoteChannel = ch;
//                               ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
								ch.pipeline().addLast(new RelayHandler(ctx.channel()));
							}
						});
				ChannelFuture future = bootstrap.connect(request.getHost(), request.getPort());
				future.addListener(future1 -> {
					if (future1.isSuccess()) {
						ctx.pipeline().remove(InitHandler.class);
						ctx.pipeline().addLast(new RelayPayloadHandler(remoteChannel));
//                       System.out.println(ctx.pipeline().names());
						ctx.channel().writeAndFlush(Unpooled.wrappedBuffer("check".getBytes()));
						logger.info(remoteChannel.remoteAddress().getHostString() + ":"
								+ remoteChannel.remoteAddress().getPort() + "  <<<<<<<  "
								+ ctx.channel().remoteAddress());
					} else {
						ctx.close();
					}
				});

			} else {
				if (request.getPayload() != null)
					ReferenceCountUtil.release(request.getPayload());
			}
		}
	}
}
