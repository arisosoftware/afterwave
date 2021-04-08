package com.afterwave.core.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;


public class StrUtil {

	public static final String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	private static final char[] digits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	private static final char[] hexDigits = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b',
			'c', 'd', 'e', 'f' };
	public static final Random random = new Random();
	public static final String userNameCheck = "[a-z0-9A-Z]{2,16}";

	public static boolean check(String text, String regex) {
		if (StringUtils.isEmpty(text)) {
			return false;
		} else {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(text);
			return matcher.matches();
		}
	}




	public static Map<String, Object> formatParams(String params) {
		if (StringUtils.isEmpty(params))
			return null;
		Map<String, Object> map = new HashMap<>();
		for (String s : params.split("&")) {
			String[] ss = s.split("=");
			map.put(ss[0], ss[1]);
		}
		return map;
	}

	/**
	 * 检测是否是用户accessToken
	 */
	public static boolean isUUID(String accessToken) {
		if (StringUtils.isEmpty(accessToken)) {
			return false;
		} else {
			try {

				UUID.fromString(accessToken);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}

	/**
	 * 随机指定长度的数字
	 *
	 * @param length
	 * @return
	 */
	public static String randomNumber(int length) {
		StringBuilder sb = new StringBuilder();
		for (int loop = 0; loop < length; ++loop) {
			sb.append(digits[random.nextInt(digits.length)]);
		}
		return sb.toString();
	}

	/**
	 * 随机指定长度的字符串
	 *
	 * @param length
	 * @return
	 */
	public static String randomString(int length) {
		StringBuilder sb = new StringBuilder();
		for (int loop = 0; loop < length; ++loop) {
			sb.append(hexDigits[random.nextInt(hexDigits.length)]);
		}
		return sb.toString();
	}

	/**
	 * remove duplicate
	 *
	 * @param strs
	 * @return
	 */
	public static List<String> removeDuplicate(String[] strs) {
		List<String> list = new ArrayList<>();
		for (String s : strs) {
			if (!list.contains(s)) {
				list.add(s);
			}
		}
		return list;
	}

	
	public static String formatContent(String content) {
		Document parse = Jsoup.parse(content);
		Elements tableElements = parse.select("table");
		tableElements.forEach(element -> element.addClass("table table-bordered"));
		Elements aElements = parse.select("p");
		if (aElements != null && aElements.size() > 0) {
			aElements.forEach(element -> {
				try {
					String href = element.text();
					if (href.contains("//www.youtube.com/watch")) {
						URL aUrl = new URL(href);
						String query = aUrl.getQuery();
						Map<String, Object> querys = StrUtil.formatParams(query);
						element.text("");
						element.addClass("embed-responsive embed-responsive-16by9");
						element.append("<iframe class='embedded_video' src='https://www.youtube.com/embed/"
								+ querys.get("v") + "' frameborder='0' allowfullscreen></iframe>");
					} else if (href.contains("//v.youku.com/v_show/")) {
						element.text("");
						URL aUrl = new URL(href);
						String _href = "https://player.youku.com/embed/"
								+ aUrl.getPath().replace("/v_show/id_", "").replace(".html", "");
						element.addClass("embedded_video_wrapper");
						element.append("<iframe class='embedded_video' src='" + _href
								+ "' frameborder='0' allowfullscreen></iframe>");
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			});
		}
		return parse.outerHtml();
	}

}
