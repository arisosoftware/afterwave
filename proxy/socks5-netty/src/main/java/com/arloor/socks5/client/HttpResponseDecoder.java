package com.arloor.socks5.client;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arloor.socks5.common.SocketChannelUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ByteProcessor;

public class HttpResponseDecoder extends ByteToMessageDecoder {
	private enum State {
		START, CONTENTLENGTH, CRLFCRLF, CONTENT
	}

	private static Logger logger = LoggerFactory.getLogger(HttpResponseDecoder.class);
	private final static byte[] validHead = "HTTP/1.1 200 OK\r\nContent-Type: text/plain; charset=utf-8\r\nContent-Length: "
			.getBytes();

	private byte[] headStore = new byte[74];
	private int contentLength = 0;

	private State state = State.START;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {

		switch (state) {
		case START:
			if (in.readableBytes() >= 74) {// 如果不以 “"HTTP/1.1 200 OK。。。。。"”开始则直接点开连接
				if (!headValid(in)) {
					logger.error("来自服务器的错误的响应。请检查sogo.json配置");
					SocketChannelUtils.closeOnFlush(ctx.channel());
					return;
				}
			}

			if (in.readableBytes() <= 74) {
				return;
			} else {
				in.readerIndex(in.readerIndex() + 74);
				state = State.CONTENTLENGTH;
			}
		case CONTENTLENGTH:
			int index = in.forEachByte(ByteProcessor.FIND_CRLF);
			if (index == -1) {
				return;
			} else {
				CharSequence cs = in.readCharSequence(index - in.readerIndex(), StandardCharsets.UTF_8);
				contentLength = Integer.parseInt(cs.toString());

				state = State.CRLFCRLF;
			}
		case CRLFCRLF:
			if (in.readableBytes() < 4) {
				return;
			} else {
				in.readerIndex(in.readerIndex() + 4);
				state = State.CONTENT;
			}
		case CONTENT:
			if (in.readableBytes() < contentLength) {
				return;
			} else {
				ByteBuf buf = in.readSlice(contentLength);
				ByteBuf content = PooledByteBufAllocator.DEFAULT.buffer();
				while (buf.isReadable()) {
					content.writeByte(~buf.readByte());
				}
				out.add(content);
				state = State.START;
			}
		}
	}

	// 检查响应的其实部分是否正确
	private boolean headValid(ByteBuf slice) {
		slice.markReaderIndex();
		slice.readBytes(headStore);
		slice.resetReaderIndex();

		for (int i = 0; i < validHead.length; i++) {
			if (headStore[i] != validHead[i]) {
				return false;
			}
		}
		return true;
	}

}
