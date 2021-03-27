package com.afterwave.core.util.identicon.generator.impl;

import java.awt.Color;
import java.util.Random;

import com.afterwave.core.util.identicon.generator.IBaseGenartor;

 
public class MyGenerator implements IBaseGenartor {

	private Random random = new Random();

	@Override
	public Color getBackgroundColor() {
		return new Color(236, 236, 236);
	}

	@Override
	public boolean[][] getBooleanValueArray(String hash) {

		boolean[][] array = new boolean[5][5];


		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				array[i][j] = false;
			}
		}

		for (int i = 0; i < 15; i++) {
			if (i % 3 == 0) {
				boolean b = random.nextBoolean();
				array[i / 3][0] = b;
				array[i / 3][4] = b;
			} else if (i % 3 == 1) {
				boolean b = random.nextBoolean();
				array[i / 3][1] = b;
				array[i / 3][3] = b;
			} else {
				boolean b = random.nextBoolean();
				array[i / 3][2] = b;
			}
		}

		return array;
	}

	@Override
	public Color getForegroundColor() {
		int r = random.nextInt(256);
		int g = random.nextInt(256);
		int b = random.nextInt(256);
		return new Color(r, g, b);
	}
}
