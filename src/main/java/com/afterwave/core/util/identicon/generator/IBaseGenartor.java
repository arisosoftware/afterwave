package com.afterwave.core.util.identicon.generator;

import java.awt.Color;

 
public interface IBaseGenartor {
	/**
	 * 获取图片背景色
	 *
	 * @return
	 */
	public Color getBackgroundColor();

	/**
	 * 将hash字符串转换为bool二维6*5数组
	 *
	 * @param hash
	 * @return
	 */
	public boolean[][] getBooleanValueArray(String hash);

	/**
	 * 获取图案前景色
	 *
	 * @return
	 */
	public Color getForegroundColor();
}
