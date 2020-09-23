package com.afterwave.core.util;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.afterwave.config.SiteConfig;
import com.afterwave.module.attachment.pojo.Attachment;
import com.afterwave.module.attachment.service.AttachmentService;
 
@Component
public class FileUtil {

	@Autowired
	private AttachmentService attachmentService;

	private Logger log = LoggerFactory.getLogger(FileUtil.class);
	@Autowired
	private SiteConfig siteConfig;


	public String getFileSize(long longSize) {
		String fileSize;
		long SIZE_BT = 1024L;
		long SIZE_KB = SIZE_BT * 1024;
		long SIZE_MB = SIZE_KB * 1024;
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
		if (longSize == 0) {
			fileSize = "0";
		} else if (longSize < SIZE_BT && longSize > 0) {// <1K
			fileSize = longSize + "B";
		} else if (longSize >= SIZE_BT && longSize < SIZE_KB) {// >1K and <1M
			fileSize = df.format(((double) longSize / (double) SIZE_BT)) + "K";
		} else if (longSize >= SIZE_KB && longSize < SIZE_MB) {// >1M and

			fileSize = df.format(((double) longSize / (double) SIZE_KB)) + "M";
		} else {
			fileSize = "1G";
		}
		return fileSize;
	}

	public Map<String, Object> getFileSize(MultipartFile file) {
		Map<String, Object> map = new HashMap<>();
		try {
			BufferedImage image = ImageIO.read(file.getInputStream());
			String suffix = "." + file.getContentType().split("/")[1];
			int width = image.getWidth();
			int height = image.getHeight();
			map.put("suffix", suffix);
			map.put("width", width);
			map.put("height", height);
			map.put("size", getFileSize(file.getSize()));
			return map;
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * search username upload dir's space size
	 *
	 * @param file
	 * @return
	 */
	public long getTotalSizeOfFilesInDir(File file) {
		if (file.isFile())
			return file.length();
		File[] children = file.listFiles();
		long total = 0;
		if (children != null)
			for (File child : children)
				total += getTotalSizeOfFilesInDir(child);
		return total;
	}

	/**
	 * 上传文件到本地
	 *
	 * @param file
	 * @param fileType
	 * @return
	 * @throws IOException
	 */

	public Attachment uploadFile(MultipartFile file, FileType fileType, String username) throws IOException {
		if (file.isEmpty())
			return null;
		Map<String, Object> map = getFileSize(file);
		String suffix = (String) map.get("suffix");
		String fileName = UUID.randomUUID().toString() + suffix;
		String today = DateUtil.formatDateTime(new Date(), DateUtil.FORMAT_DATE);

		File localPathDir = new File(siteConfig.getUploadPath() + username + File.separator + today);
		if (!localPathDir.exists())
			localPathDir.mkdirs();

		String localPath = siteConfig.getUploadPath() + username + File.separator + today + File.separator + fileName;
		String requestUrl = siteConfig.getStaticUrl() + username + File.separator + today + File.separator + fileName;
		String md5 = DigestUtils.md5DigestAsHex(file.getInputStream());


		BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(localPath)));
		stream.write(file.getBytes());
		stream.close();


		return attachmentService.createAttachment(localPath, fileName, requestUrl, fileType.name(),
				(Integer) map.get("width"), (Integer) map.get("height"), (String) map.get("size"), suffix, md5);
	}

	
}
