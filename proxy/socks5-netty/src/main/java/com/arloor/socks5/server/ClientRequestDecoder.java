package com.arloor.socks5.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arloor.socks5.common.MyBase64;
import com.arloor.socks5.common.SocketChannelUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ByteProcessor;

//POST /target?at=AMeVrMaYq7NWq7eYrdPYra== HTTP/1.1
//        Host: qtgwuehaoisdhuaishdaisuhdasiuhlassjd.com
//        Authorization: Basic YTpi
//        Accept: */*
//Content-Type: text/plain
//accept-encoding: gzip, deflate
//content-length: 265

public class ClientRequestDecoder extends ByteToMessageDecoder {
	private enum State {
		START, HEADER, CRLFCRLF, CONTENT
	}

	private static Logger logger = LoggerFactory.getLogger(ClientRequestDecoder.class);
	private final static String fakeHost = "qtgwuehaoisdhuaishdaisuhdasiuhlassjd.com";
	private final static String head1 = "GET";
	private final static String head2 = "POST";
	private final static String end = "HTTP/1.1";
	private int contentLength = 0;
	private State state = State.START;
	private final int tempByteStoreLength = 200;
	private byte[] tempByteStore = new byte[tempByteStoreLength];
	private String path;

	private String mehtod;
	private byte[] oldContent;
	private byte[] newContent;

	private Map<String, String> headers = new HashMap<>();

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

//        newContent=new byte[in.readableBytes()];
//        in.markReaderIndex();
//        in.readBytes(newContent);
//        in.resetReaderIndex();

		switch (state) {
		case START:
			int index = in.forEachByte(ByteProcessor.FIND_CR);
			if (index != -1) {
				int length = index - in.readerIndex();
				in.markReaderIndex();
				in.readBytes(tempByteStore, 0, length);
				String initLine = new String(tempByteStore, 0, length);
				if (initLine.endsWith(end) && (initLine.startsWith(head1) || initLine.startsWith(head2))) {
					int index1 = initLine.indexOf(" ");
					int index2 = initLine.lastIndexOf(" ");
					if (index2 == index1) {
						logger.error("??????\"A B C\"?????????initline???????????????GET/POST?????????????????????????????????[" + initLine + "] ?????????"
								+ ctx.channel().remoteAddress());
						in.resetReaderIndex();
						ctx.close();
						return;
					} else {
						path = initLine.substring(index1 + 1, index2);
						mehtod = initLine.substring(0, index1);
						state = State.HEADER;
					}
				} else {
					logger.error(
							"????????????????????????????????????GET/POST?????????????????????????????????[" + initLine + "] ?????????" + ctx.channel().remoteAddress());
//                        logger.error("?????????\n"+new String(newContent).substring(0,4)+"....");
//                        String s=new String(oldContent);
//                        logger.error("???????????? \n...."+s.substring(s.length()-4));
					in.resetReaderIndex();
					ctx.close();
					return;
				}
			} else {
				return;
			}
		case HEADER:
			while (headers.get("content-length") == null) {
				int newIndex = in.forEachByte(ByteProcessor.FIND_NON_CRLF);
				if (newIndex == -1) {
					return;
				}
				in.readerIndex(newIndex);
				newIndex = in.forEachByte(ByteProcessor.FIND_CR);
				if (newIndex == -1) {
					return;
				}
				int length = newIndex - in.readerIndex();
				try {
					in.readBytes(tempByteStore, 0, length);
				} catch (IllegalArgumentException e) {
					System.out.println(e);
				}

				String header = new String(tempByteStore, 0, length);
				int split = header.indexOf(": ");
				if (split != -1 && split < header.length() - 2) {
					if (header.substring(0, split).equals("Host") && !header.substring(split + 2).equals(fakeHost)) {
						logger.error("??????host??????????????? " + ctx.channel().remoteAddress());
						SocketChannelUtils.closeOnFlush(ctx.channel());
						return;
					}
					headers.put(header.substring(0, split), header.substring(split + 2));
				} else {
					logger.error("????????????????????????????????? " + ctx.channel().remoteAddress());
					SocketChannelUtils.closeOnFlush(ctx.channel());
					return;
				}
			}
			contentLength = Integer.parseInt(headers.get("content-length"));
			state = State.CRLFCRLF;
		case CRLFCRLF:
			// ????????????4???byte?????????\r\n\r\n
			if (in.readableBytes() < 4) {
				return;
			} else {
				in.readerIndex(in.readerIndex() + 4);
				state = State.CONTENT;
			}
		case CONTENT:
			if (in.readableBytes() < contentLength) {
				return;
			}
			ByteBuf slice = in.readSlice(contentLength);
			ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();
			while (slice.isReadable()) {
				buf.writeByte(~slice.readByte());
			}
			headers.remove("content-length");
			state = State.START;
			if (path.startsWith("/target?at=")) {
				String hostAndPort = new String(Objects.requireNonNull(MyBase64.decode(path.substring(11).getBytes())));
				int splitIndex = hostAndPort.indexOf(":");
				String host = hostAndPort.substring(0, splitIndex);
				int port = Integer.parseInt(hostAndPort.substring(splitIndex + 1));
				Request request = new Request(host, port, buf);
				out.add(request);
			}
		}
	}
}
