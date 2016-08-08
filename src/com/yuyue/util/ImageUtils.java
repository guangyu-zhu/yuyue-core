package com.yuyue.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageUtils {
	
	private static Logger log = Logger.getLogger(ImageUtils.class);
	
	private static final String PICTRUE_FORMATE_JPG = "jpg";

	public static void zoomImage(String src, int width, int height) {
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
			
			if(dheight < _dheight){
				times = Double.valueOf(dheight)/Double.valueOf(oheight);
			}
			
			double newWidth = owidth * times;
			double newHeight = oheight * times;

			BufferedImage bfImage = new BufferedImage((int)newWidth, (int)newHeight,
					BufferedImage.TYPE_INT_RGB);
			bfImage.getGraphics().drawImage(
					image.getScaledInstance((int)newWidth, (int)newHeight,
							Image.SCALE_SMOOTH), 0, 0, null);
			
			os = new FileOutputStream(src);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
			encoder.encode(bfImage);
			
		} catch (Exception e) {
			log.error("create failed" + e);
		} finally{
			if(os != null){
				try{
					os.close();
				}catch(Exception e){
					log.error(CommConstants.ERROR_LEVEL_0 + "when try to close connection, error happen",e);
				}
			}
		}
	}
	
	public static void enforceZoomImage(String src, int width, int height) {
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
			FileOutputStream os = new FileOutputStream(src);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
			encoder.encode(bfImage);
			os.close(); 	
		} catch (Exception e) {
			log.error("create failed" + e);
		}
	}
	
	public static String markBlankImage(PageContext pageContext, String src, int width, int height) {
		try {
			int index = src.lastIndexOf(".");
			StringBuilder sb = new StringBuilder(src);
			sb.insert(index, "-blank");
			String blankSrc = sb.toString();
			String realBlankSrc = blankSrc.replace("/html", "");
			String blankSrcPath = pageContext.getSession().getServletContext().getRealPath(realBlankSrc);
			File blankSrcfile = new File(blankSrcPath);
			if (blankSrcfile.exists()) {
				return StringUtils.getContextPath(pageContext.getSession().getServletContext().getContextPath()) + realBlankSrc;
			}
			
			String srcPath = pageContext.getSession().getServletContext().getRealPath(src);
			File srcfile = new File(srcPath);
			if (!srcfile.exists()) {
				log.error("File not exist!");
				return null;
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
			Graphics graphics = bfImage.getGraphics();
			graphics.drawImage(image.getScaledInstance((int)newWidth, (int)newHeight,
							Image.SCALE_SMOOTH), -x, -y, null);
			graphics.setColor(Color.WHITE);
			int fontSize = (int)newWidth/7;
			Font f = new Font("宋体",Font.BOLD,fontSize);
			graphics.setFont(f);
			graphics.drawString("添加新图", x+(int)newWidth/2-fontSize*2, (int)newHeight/2+fontSize/2-y);
			FileOutputStream os = new FileOutputStream(blankSrcPath);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
			encoder.encode(bfImage);
			os.close(); 	
			return StringUtils.getContextPath(pageContext.getSession().getServletContext().getContextPath()) + realBlankSrc;
		} catch (Exception e) {
			log.error("create failed" + e);
			return null;
		}
	}
	
	public static Integer[] getImageWH(String path){
		File picture = new File(path); 
		if(!picture.exists()){
			return null;
		}
        BufferedImage sourceImg;
        FileInputStream fis = null;
		try {
			fis = new FileInputStream(picture);
			sourceImg = ImageIO.read(fis);
			if(sourceImg != null){
				return new Integer[]{sourceImg.getWidth(),sourceImg.getHeight()};
			}else{
				return null;
			}
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} finally {
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
		return null;
	}
	
	public static Integer[] getImageWH(InputStream inputStream){
        BufferedImage sourceImg;
        FileInputStream fis = null;
		try {
			sourceImg = ImageIO.read(inputStream);
			return new Integer[]{sourceImg.getWidth(),sourceImg.getHeight()};
		} catch (FileNotFoundException e) {
			log.error(e);
			return null;
		} catch (IOException e) {
			log.error(e);
			return null;
		} catch (Exception e){
			log.error(e);
			return null;
		} finally {
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
	}
	
	public static void rotateImg(String path, int degree) {  
		FileOutputStream os = null;
		ByteArrayOutputStream  byteOut = null;
		ImageOutputStream iamgeOut = null;
		InputStream  inputStream = null;
		InputStream is = null;
		try{
			is = new FileInputStream(new File(path));
			BufferedImage image = ImageIO.read(is);
			int iw = image.getWidth();//原始图象的宽度   
			int ih = image.getHeight();//原始图象的高度  
			int w = 0;  
			int h = 0;  
			int x = 0;  
			int y = 0;  
			degree = degree % 360;  
			if (degree < 0)  
				degree = 360 + degree;//将角度转换到0-360度之间  
			double ang = Math.toRadians(degree);//将角度转为弧度  
			
			/** 
			 *确定旋转后的图象的高度和宽度 
			 */  
			
			if (degree == 180 || degree == 0 || degree == 360) {  
				w = iw;  
				h = ih;  
			} else if (degree == 90 || degree == 270) {  
				w = ih;  
				h = iw;  
			} else {  
				int d = iw + ih;  
				w = (int) (d * Math.abs(Math.cos(ang)));  
				h = (int) (d * Math.abs(Math.sin(ang)));  
			}  
			
			x = (w / 2) - (iw / 2);//确定原点坐标  
			y = (h / 2) - (ih / 2);  
			BufferedImage rotatedImage = new BufferedImage(w, h, image.getType());  
			Graphics2D gs = (Graphics2D)rotatedImage.getGraphics();  
			rotatedImage  = gs.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.OPAQUE);  
			
			AffineTransform at = new AffineTransform();  
			at.rotate(ang, w / 2, h / 2);//旋转图象  
			at.translate(x, y);  
			AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);  
			op.filter(image, rotatedImage);  
			image = rotatedImage;  
			
			byteOut = new ByteArrayOutputStream();  
			iamgeOut = ImageIO.createImageOutputStream(byteOut);  
			ImageIO.write(image, "png", iamgeOut);  
			inputStream = new ByteArrayInputStream(byteOut.toByteArray());  
			os = new FileOutputStream(path);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
			encoder.encode(ImageIO.read(inputStream));
		}catch(Exception e){
			log.error(e);
		}finally{
			if(byteOut != null){
				try {
					byteOut.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
			if(iamgeOut != null){
				try {
					iamgeOut.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
    }  
	
    /** 
     * 添加图片水印 
     * @param targetImg 目标图片路径，如：C://myPictrue//1.jpg 
     * @param waterImg  水印图片路径，如：C://myPictrue//logo.png 
     * @param x 水印图片距离目标图片左侧的偏移量，如果x<0, 则在正中间 
     * @param y 水印图片距离目标图片上侧的偏移量，如果y<0, 则在正中间 
     * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明) 
     */  
    public final static void pressImage(String targetImg, String waterImg, int x, int y, float alpha) {  
            try {  
                File file = new File(targetImg);  
                Image image = ImageIO.read(file);
                int width = image.getWidth(null);  
                int height = image.getHeight(null);  
                BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
                Graphics2D g = bufferedImage.createGraphics();  
                g.drawImage(image, 0, 0, width, height, null);  
              
                Image waterImage = ImageIO.read(new File(waterImg));    // 水印文件  
                int width_1 = waterImage.getWidth(null);  
                int height_1 = waterImage.getHeight(null);  
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));  
                  
                int widthDiff = width - width_1;  
                int heightDiff = height - height_1;  
                if(x < 0){  
                    x = widthDiff / 2;  
                }else if(x > widthDiff){  
                    x = widthDiff;  
                }  
                if(y < 0){  
                    y = heightDiff / 2;  
                }else if(y > heightDiff){  
                    y = heightDiff;  
                }  
                g.drawImage(waterImage, x, y, width_1, height_1, null); // 水印文件结束  
                g.dispose();  
                ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, file);  
            } catch (IOException e) {  
                log.error(e); 
            }  
    }  
  
    /** 
     * 添加文字水印 
     * @param targetImg 目标图片路径，如：C://myPictrue//1.jpg 
     * @param pressText 水印文字， 如：中国证券网 
     * @param fontName 字体名称，    如：宋体 
     * @param fontStyle 字体样式，如：粗体和斜体(Font.BOLD|Font.ITALIC) 
     * @param fontSize 字体大小，单位为像素 
     * @param color 字体颜色 
     * @param x 水印文字距离目标图片左侧的偏移量，如果x<0, 则在正中间 
     * @param y 水印文字距离目标图片上侧的偏移量，如果y<0, 则在正中间 
     * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明) 
     */  
    public static void pressText(String targetImg, String pressText, String fontName, int fontStyle, int fontSize, Color color, int x, int y, float alpha) {  
        try {  
            File file = new File(targetImg);  
              
            Image image = ImageIO.read(file);  
            int width = image.getWidth(null);  
            int height = image.getHeight(null);  
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
            Graphics2D g = bufferedImage.createGraphics();  
            g.drawImage(image, 0, 0, width, height, null);  
            g.setFont(new Font(fontName, fontStyle, fontSize));  
            g.setColor(color);  
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));  
              
            int width_1 = fontSize * getLength(pressText);  
            int height_1 = fontSize;  
            int widthDiff = width - width_1;  
            int heightDiff = height - height_1;  
            if(x < 0){  
                x = widthDiff / 2;  
            }else if(x > widthDiff){  
                x = widthDiff;  
            }  
            if(y < 0){  
                y = heightDiff / 2;  
            }else if(y > heightDiff){  
                y = heightDiff;  
            }  
              
            g.drawString(pressText, x, y + height_1);  
            g.dispose();  
            ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, file);  
        } catch (Exception e) {  
            log.error(e);
        }  
    }  
      
    /** 
     * 获取字符长度，一个汉字作为 1 个字符, 一个英文字母作为 0.5 个字符 
     * @param text 
     * @return 字符长度，如：text="中国",返回 2；text="test",返回 2；text="中国ABC",返回 4. 
     */  
    public static int getLength(String text) {  
        int textLength = text.length();  
        int length = textLength;  
        for (int i = 0; i < textLength; i++) {  
            if (String.valueOf(text.charAt(i)).getBytes().length > 1) {  
                length++;  
            }  
        }  
        return (length % 2 == 0) ? length / 2 : length / 2 + 1;  
    }  
  
    /** 
     * 图片缩放 
     * @param filePath 图片路径 
     * @param height 高度 
     * @param width 宽度 
     * @param bb 比例不对时是否需要补白 
     */  
    public static void resize(String filePath, int width, int height, boolean bb) {  
		try {
			double ratio = 0; //缩放比例      
			File f = new File(filePath);     
			BufferedImage bi = ImageIO.read(f);     
			Image itemp = bi.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);     
			//计算比例     
			if ((bi.getHeight() > height) || (bi.getWidth() > width)) {     
				if (bi.getHeight() > bi.getWidth()) {     
					ratio = (new Integer(height)).doubleValue() / bi.getHeight();     
				} else {     
					ratio = (new Integer(width)).doubleValue() / bi.getWidth();     
				}     
				AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);     
				itemp = op.filter(bi, null);     
			}     
			if (bb) {     
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);     
				Graphics2D g = image.createGraphics();     
				g.setColor(Color.white);     
				g.fillRect(0, 0, width, height);  
				if (width == itemp.getWidth(null))     
					g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);     
				else    
					g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null), itemp.getHeight(null), Color.white, null);     
				g.dispose();     
				itemp = image;     
			}  
			ImageIO.write((BufferedImage) itemp, "jpg", f);     
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
    }  

	public static void main(String[] args) {
//		String src = "fa/dfas/fdsaf/test.png";
//		int index = src.lastIndexOf(".");
//		StringBuilder sb = new StringBuilder(src);
//		sb.insert(index, "-blank");
//		String blankSrc = sb.toString();
//		System.out.println(blankSrc);
//		zoomImage("F:/workspace/Yuyue/WebRoot/cs/buckster/reject/images/browser_msie.gif",100,100);
//		System.out.println("success");
//		rotateImg("C:/Users/Public/Pictures/Sample Pictures/Koala - 副本.jpg",-90);
		String targetImgPath = "C:/Users/Public/Pictures/Sample Pictures/Koala - 副本.jpg";
		String waterImgPath = "C:/Users/Public/Pictures/Sample Pictures/Koala-32379806061.jpg";
		ImageUtils.pressImage(targetImgPath, waterImgPath, -1, -1, 1);

	}
	
}