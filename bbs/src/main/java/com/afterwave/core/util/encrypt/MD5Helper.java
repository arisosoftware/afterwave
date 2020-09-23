package com.afterwave.core.util.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

 
public class MD5Helper {

	protected final static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	protected static MessageDigest messagedigest = null;

	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsaex) {
			System.err.println(MD5Helper.class.getName() + "初始化失败，MessageDigest不支持MD5Util。");
			nsaex.printStackTrace();
		}
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}


	public static String getFileMD5String(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
		messagedigest.update(byteBuffer);
		in.close();
		return bufferToHex(messagedigest.digest());
	}

	private static String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

 
	public static String getMD5String(String str) {
		return getMD5String(str.getBytes());
	}

	 
	public static String getMD5StringWithSalt(String password, String salt) {
		if (password == null) {
			throw new IllegalArgumentException("password不能为null");
		}
		if (salt.equals("") || salt.length() == 0) {
			throw new IllegalArgumentException("salt不能为空");
		}
		if ((salt.toString().lastIndexOf("{") != -1) || (salt.toString().lastIndexOf("}") != -1)) {
			throw new IllegalArgumentException("salt中不能包含 { 或者 }");
		}
		return getMD5String(password + "{" + salt.toString() + "}");
	}
 
	public static String hexdigest(byte[] paramArrayOfByte) {
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(paramArrayOfByte);
			byte[] arrayOfByte = localMessageDigest.digest();
			char[] arrayOfChar = new char[32];
			int i = 0;
			int j = 0;
			for (;;) {
				if (i >= 16) {
					return new String(arrayOfChar);
				}
				int k = arrayOfByte[i];
				int m = j + 1;
				arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
				j = m + 1;
				arrayOfChar[m] = hexDigits[(k & 0xF)];
				i++;
			}

		} catch (Exception localException) {
			return null;
		}
	}
}


/**
 * 功能：得到文件的md5值。
 *XVSR-510 https://avgle.com/video/gNEDxD_i0DQ/%E4%BA%BA%E5%A6%BB%E6%BF%83%E4%BA%A4-%E6%83%85%E7%86%B1%E7%9A%84%E3%81%AA%E4%B8%AD%E5%87%BA%E3%81%97%E6%B7%AB%E4%BA%A4%E8%A8%98%E9%8C%B2-%E4%BB%A4%E5%92%8C%E3%82%8C%E3%81%84-xvsr-510
	https://clipwatching.com/6puazt94on68/VENU-496.mp4.html
	https://clipwatching.com/ya6zxmtusf37/WANZ-296.mp4.html
	下载地址：http://katfile.com/udsjjxafwyrj/1pondo-081716_363.zip.html   水谷心音
	下載地址：https://katfile.com/fpd3msoh1sb8/STARS-166.mp4.html cool
	下载地址：https://rapidgator.net/file/5e286975a4419b762ca457c144e6b76d/STAR-545.wmv.html
 */