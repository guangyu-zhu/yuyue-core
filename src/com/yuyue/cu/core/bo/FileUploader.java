package com.yuyue.cu.core.bo;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.yuyue.exception.UnCheckedException;
import com.yuyue.util.CommConstants;
import com.yuyue.util.StreamUtils;

public class FileUploader {
	
	private static Logger log = Logger.getLogger(FileUploader.class);

	public FileUploader(InputStream inputStream, String path, String name){
		this.inputStream = inputStream;
		this.path = path;
		this.name = name;
		this.orgImagePath = path+File.separator+name;
		newImage();
	}
	
	private InputStream inputStream;
	private String path;
	private String name;
	private String orgImagePath;
	
	private void newImage(){
		if(inputStream != null){
			FileOutputStream fos = null;
			String folderPath = orgImagePath.substring(0,orgImagePath.lastIndexOf(File.separator));
			boolean suc = StreamUtils.createFolder(folderPath);
			if(!suc){
				throw new RuntimeException("folder create failed");
			}
			try{
				if(inputStream.available() > 0){
					fos = new FileOutputStream(orgImagePath);
					int len = 0;
					byte[] buf = new byte[1024];
					while((len=inputStream.read(buf))!=-1){
						fos.write(buf, 0, len);
					}
					fos.flush();
				}
			}catch(Exception e){
				throw new UnCheckedException(e,"generate FileUploader");
			}finally{
				if(fos != null){
					try {
						fos.close();
					} catch (IOException e) {
						log.error(e);
					}
				}
				release();
			}
		}
	}
	
	public void generate(int width, int height){
		enforceZoomImage(orgImagePath,path+File.separator+name,width,height);
	}
	
	private void enforceZoomImage(String src, String dest, int width, int height) {
		FileOutputStream os = null;
		try {
			File srcfile = new File(src);
			if (!srcfile.exists()) {
				log.error("File not exist!");
				return;
			}
			BufferedImage image = ImageIO.read(srcfile);

			int dwidth = width;
			int dheight = height; 
			
			int owidth = image.getWidth();
			int oheight = image.getHeight(); 
			
			// try equal width
			double times = Double.valueOf(dwidth)/Double.valueOf(owidth);
			double _dheight = Double.valueOf(oheight) * times;
			
			if(dheight > _dheight){
				times = Double.valueOf(dheight)/Double.valueOf(oheight);
			}
			
			double newWidth = owidth * times;
			double newHeight = oheight * times;
			
			BufferedImage bfImage = new BufferedImage((int)newWidth, (int)newHeight,
					BufferedImage.TYPE_INT_RGB);
			int x = (int)(newWidth-dwidth)/2;
			int y = (int)(newHeight-dheight)/2;
			bfImage = bfImage.getSubimage(0, 0, dwidth, dheight);
			Graphics2D g2d = bfImage.createGraphics();
			g2d.drawImage(
					image.getScaledInstance((int)newWidth, (int)newHeight,
							Image.SCALE_SMOOTH), -x, -y, null);
			os = new FileOutputStream(dest);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
			encoder.encode(bfImage);
		} catch (Exception e) {
			log.error("create failed" + e);
		} finally{
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					log.error(CommConstants.ERROR_LEVEL_0 + "when try to close inputStream, error happen",e);
				} 
			}
		}
	}
	
	private void release(){
		if(inputStream != null){
			try {
				inputStream.close();
			} catch (IOException e) {
				log.error(CommConstants.ERROR_LEVEL_0 + "when try to close inputStream, error happen",e);
			}
		}
	}
	
}
