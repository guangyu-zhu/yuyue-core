package com.yuyue.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

public class PictureUtil {
	public static void resizePNG(String fromFile, String toFile,
			int outputWidth, int outputHeight, boolean proportion) {
		try {
			File f2 = new File(fromFile);
			BufferedImage bi2 = null;
			try{
				bi2 = ImageIO.read(f2);
			} catch (Exception e) {
				try(InputStream fis = new FileInputStream( f2 )){
					JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(fis);
					bi2 = decoder.decodeAsBufferedImage();
				}
			} 
			int newWidth;
			int newHeight;
			// 判断是否是等比缩放
			if (proportion == true) {
				// 为等比缩放计算输出的图片宽度及高度
				double rate1 = ((double) bi2.getWidth(null)) / (double) outputWidth + 0.1;
				double rate2 = ((double) bi2.getHeight(null)) / (double) outputHeight + 0.1;
				// 根据缩放比率大的进行缩放控制
				double rate = rate1 < rate2 ? rate1 : rate2;
				newWidth = (int) (((double) bi2.getWidth(null)) / rate);
				newHeight = (int) (((double) bi2.getHeight(null)) / rate);
			} else {
				newWidth = outputWidth; // 输出的图片宽度
				newHeight = outputHeight; // 输出的图片高度
			}
			BufferedImage to = new BufferedImage(newWidth, newHeight,

			BufferedImage.TYPE_INT_RGB);

			Graphics2D g2d = to.createGraphics();

			to = g2d.getDeviceConfiguration().createCompatibleImage(newWidth, newHeight, Transparency.TRANSLUCENT);

			g2d.dispose();

			g2d = to.createGraphics();

			Image from = bi2.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_AREA_AVERAGING);
			g2d.drawImage(from, 0, 0, null);
			g2d.dispose();
			
			ImageIO.write(to, "png", new File(toFile));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			long fromLength = new File(fromFile).length();
			long toLength = new File(toFile).length();
			if(toLength > fromLength){
				StreamUtils.copyFile(fromFile, toFile);
			}
		}

	}

}
