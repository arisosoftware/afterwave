package com.afterwave.web.front;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.afterwave.config.LogEventConfig;
import com.afterwave.core.base.BaseController;
import com.afterwave.core.util.FreemarkerUtil;
import com.afterwave.module.log.service.LogService;

/**
 * 
 */
@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {

	private int codeCount = 4;// 定义图片上显示验证码的个数
	char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', '3', '4', '5', '6', '7', '8' };
	private int codeY = 25;

	private int fontHeight = 26;
	@Autowired
	FreemarkerUtil freemarkerUtil;
	private int height = 32;// 定义图片的height
	@Autowired
	LogEventConfig logEventConfig;
	@Autowired
	LogService logService;
	private int width = 120;// 定义图片的width
	private int xx = 22;

	/**
	 * 验证码生成
	 *
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	@RequestMapping("/code")
	public void getCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);


		Graphics gd = buffImg.getGraphics();

		Random random = new Random();

		gd.setColor(Color.WHITE);
		gd.fillRect(0, 0, width, height);


		Font font = new Font("Fixedsys", Font.BOLD, fontHeight);

		gd.setFont(font);


		gd.setColor(Color.BLACK);
		gd.drawRect(0, 0, width - 1, height - 1);


		gd.setColor(Color.BLACK);
		for (int i = 0; i < 40; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(20);
			int yl = random.nextInt(20);
			gd.drawLine(x, y, x + xl, y + yl);
		}


		StringBuffer randomCode = new StringBuffer();
		int red, green, blue;


		for (int i = 0; i < codeCount; i++) {

			String code = String.valueOf(codeSequence[random.nextInt(29)]);

			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);


			gd.setColor(new Color(red, green, blue));
			gd.drawString(code, (i + 1) * xx, codeY);

			randomCode.append(code);
		}

		HttpSession session = req.getSession();
		session.setAttribute("index_code", randomCode.toString());

		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);
		resp.setContentType("image/jpeg");

		ServletOutputStream sos = resp.getOutputStream();
		ImageIO.write(buffImg, "jpeg", sos);
	}
}
