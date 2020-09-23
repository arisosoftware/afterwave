package com.geccocrawler.socks5;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.geccocrawler.socks5.handler.ChannelListener;
import com.geccocrawler.socks5.handler.ProxyChannelTrafficShapingHandler;
import com.geccocrawler.socks5.handler.ProxyIdleHandler;
import com.geccocrawler.socks5.handler.ss5.Socks5CommandRequestHandler;
import com.geccocrawler.socks5.handler.ss5.Socks5InitialRequestHandler;

import com.geccocrawler.socks5.log.ProxyFlowLog;
import com.geccocrawler.socks5.log.ProxyFlowLog4j;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.socksx.v5.Socks5CommandRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder;
import io.netty.handler.codec.socksx.v5.Socks5ServerEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class ProxyServer {

	private static final Logger logger = LoggerFactory.getLogger(ProxyServer.class);

	public static ProxyServer create(int port) {
		return new ProxyServer(port);
	}

	public static void main(String[] args) throws Exception {
		int port = 11080;
		boolean auth = false;
		Properties properties = new Properties();
		try {
			properties.load(ProxyServer.class.getResourceAsStream("/config.properties"));
			port = Integer.parseInt(properties.getProperty("port"));
			auth = Boolean.parseBoolean(properties.getProperty("auth"));
		} catch (Exception e) {
			logger.warn("load config.properties error, default port 11080, auth false!");
		}
		ProxyServer.create(port).logging(true).auth(auth).start();
	}

	private boolean auth;

	private EventLoopGroup bossGroup = new NioEventLoopGroup();

	private ChannelListener channelListener;

	private boolean logging;

	private int port;

	private ProxyFlowLog proxyFlowLog;

	private ProxyServer(int port) {
		this.port = port;
	}

	public ProxyServer auth(boolean auth) {
		this.auth = auth;
		return this;
	}

	public ProxyServer channelListener(ChannelListener channelListener) {
		this.channelListener = channelListener;
		return this;
	}

	public EventLoopGroup getBossGroup() {
		return bossGroup;
	}

	public ChannelListener getChannelListener() {
		return channelListener;
	}

	public ProxyFlowLog getProxyFlowLog() {
		return proxyFlowLog;
	}

	public boolean isAuth() {
		return auth;
	}

	public boolean isLogging() {
		return logging;
	}

	public ProxyServer logging(boolean logging) {
		this.logging = logging;
		return this;
	}

	public ProxyServer proxyFlowLog(ProxyFlowLog proxyFlowLog) {
		this.proxyFlowLog = proxyFlowLog;
		return this;
	}

	public void start() throws Exception {
		if (proxyFlowLog == null) {
			proxyFlowLog = new ProxyFlowLog4j();
		}

		EventLoopGroup boss = new NioEventLoopGroup(2);
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boss, worker).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// 流量统计
							ch.pipeline().addLast(ProxyChannelTrafficShapingHandler.PROXY_TRAFFIC,
									new ProxyChannelTrafficShapingHandler(3000, proxyFlowLog, channelListener));
							// channel超时处理
							ch.pipeline().addLast(new IdleStateHandler(3, 30, 0));
							ch.pipeline().addLast(new ProxyIdleHandler());
							// netty日志
							if (logging) {
								ch.pipeline().addLast(new LoggingHandler());
							}
							// Socks5MessagByteBuf
							ch.pipeline().addLast(Socks5ServerEncoder.DEFAULT);
							// sock5 init
							ch.pipeline().addLast(new Socks5InitialRequestDecoder());
							// sock5 init
							ch.pipeline().addLast(new Socks5InitialRequestHandler(ProxyServer.this));

							// socks connection
							ch.pipeline().addLast(new Socks5CommandRequestDecoder());
							// Socks connection
							ch.pipeline().addLast(new Socks5CommandRequestHandler(ProxyServer.this.getBossGroup()));
						}
					});

			ChannelFuture future = bootstrap.bind(port).sync();
			logger.debug("bind port : " + port);
			future.channel().closeFuture().sync();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
