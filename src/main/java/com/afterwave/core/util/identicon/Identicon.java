package com.afterwave.core.util.identicon;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.common.base.Preconditions;
import com.afterwave.config.SiteConfig;
import com.afterwave.core.util.HashUtil;
import com.afterwave.core.util.StrUtil;
import com.afterwave.core.util.encrypt.MD5Helper;
import com.afterwave.core.util.identicon.generator.IBaseGenartor;
import com.afterwave.core.util.identicon.generator.impl.MyGenerator;

 
@Component
public class Identicon {

	public static String imgToBase64String(RenderedImage img, String formatName) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, formatName, os);
			return Base64.getEncoder().encodeToString(os.toByteArray());
		} catch (final IOException ioe) {
			throw new UncheckedIOException(ioe);
		}
	}

	public static void main(String[] args) {
		System.out.println(new Identicon().generator());
	}

	private IBaseGenartor genartor;

	private Logger log = LoggerFactory.getLogger(Identicon.class);

	@Autowired
	private SiteConfig siteConfig;

	public Identicon() {
		this.genartor = new MyGenerator();
	}

	public BufferedImage create(String hash, int size) {
		Preconditions.checkArgument(size > 0 && !StringUtils.isEmpty(hash));

		boolean[][] array = genartor.getBooleanValueArray(hash);


		int ratio = size / 6;

		BufferedImage identicon = new BufferedImage(ratio * 6, ratio * 6, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = identicon.getGraphics();

		graphics.setColor(genartor.getBackgroundColor()); // 背景色
		graphics.fillRect(0, 0, identicon.getWidth(), identicon.getHeight());

		graphics.setColor(genartor.getForegroundColor()); // 图案前景色
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (array[i][j]) {
					graphics.fillRect(j * ratio + 35, i * ratio + 35, ratio, ratio);
				}
			}
		}

		return identicon;
	}

	public String generator() {
		Identicon identicon = new Identicon();
		String md5 = MD5Helper.getMD5String(StrUtil.randomString(6));
		BufferedImage image = identicon.create(md5, 300);
		return "data:image/png;base64," + imgToBase64String(image, "png");
	}

	public String generator(String username) {
		Identicon identicon = new Identicon();
		String md5 = HashUtil.md5(StrUtil.randomString(6));
		BufferedImage image = identicon.create(md5, 420);
		return saveFile(username, image);
	}

	public String saveFile(String username, BufferedImage image) {
		String fileName = "avatar.png";
		String userAvatarPath = username + "/";
		try {
			File file = new File(siteConfig.getUploadPath() + userAvatarPath);
			if (!file.exists())
				file.mkdirs();
			File file1 = new File(siteConfig.getUploadPath() + userAvatarPath + fileName);
			if (!file1.exists())
				file1.createNewFile();
			ImageIO.write(image, "PNG", file1);
			return siteConfig.getStaticUrl() + userAvatarPath + fileName;
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}
		return "";
	}

 
}
