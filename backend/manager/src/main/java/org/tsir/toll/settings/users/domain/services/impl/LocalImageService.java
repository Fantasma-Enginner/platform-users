package org.tsir.toll.settings.users.domain.services.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tsir.common.utils.PrintWriterPicture;
import org.tsir.toll.settings.users.domain.services.ImageService;

@Service
public class LocalImageService implements ImageService {

	Logger log = LoggerFactory.getLogger(LocalImageService.class);

	PrintWriterPicture pwp = new PrintWriterPicture("", null, false, "users/");

	@Override
	public void saveImage(long identifier, byte[] content) {
		pwp.writeFile(content, identifier);
	}

	@Override
	public byte[] retrieveImage(long identifier, float scale) {
		if (scale != 1) {
			try {
				byte[] bytes = pwp.getBytes(identifier);
				if (bytes.length > 0) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					BufferedImage bi = ImageIO.read(new ByteArrayInputStream(bytes));
					bi.getScaledInstance((int) (bi.getWidth() * scale), (int) (bi.getHeight() * scale),
							Image.SCALE_FAST);
					ImageIO.write(bi, "jpg", baos);
					return baos.toByteArray();
				} else {
					return bytes;
				}
			} catch (Exception e) {
				log.warn("Fail scaling user image", e);
				return pwp.getBytes(identifier);
			}
		} else {
			return pwp.getBytes(identifier);
		}
	}

}
