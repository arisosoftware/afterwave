package com.afterwave.core.util.identicon.generator.impl;

import java.awt.Color;
import java.math.RoundingMode;
import java.util.Random;

import org.springframework.util.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.math.DoubleMath;
import com.afterwave.core.util.identicon.generator.IBaseGenartor;

 
public class DefaultGenerator implements IBaseGenartor {
	private boolean[][] booleanValueArray;
	private Random random = new Random();

	@Override
	public Color getBackgroundColor() {





		return new Color(236, 236, 236);
	}

	@Override
	public boolean[][] getBooleanValueArray(String hash) {
		Preconditions.checkArgument(!StringUtils.isEmpty(hash) && hash.length() >= 16,
				"illegal argument hash:not null and size >= 16");

		boolean[][] array = new boolean[6][5];


		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 5; j++) {
				array[i][j] = false;
			}
		}

		for (int i = 0; i < hash.length(); i += 2) {
			int s = i / 2; // 只取hash字符串偶数编号（从0开始）的字符

			boolean v = DoubleMath.roundToInt(Integer.parseInt(hash.charAt(i) + "", 16) / 10.0,
					RoundingMode.HALF_UP) > 0 ? true : false;
			if (s % 3 == 0) {
				array[s / 3][0] = v;
				array[s / 3][4] = v;
			} else if (s % 3 == 1) {
				array[s / 3][1] = v;
				array[s / 3][3] = v;
			} else {
				array[s / 3][2] = v;
			}
		}

		this.booleanValueArray = array;

		return this.booleanValueArray;
	}

	@Override
	public Color getForegroundColor() {






		int r = random.nextInt(256);
		int g = random.nextInt(256);
		int b = random.nextInt(256);
		return new Color(r, g, b);
	}
}
