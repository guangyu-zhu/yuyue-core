package com.yuyue.cu.core.web.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.yuyue.cu.core.bo.FileUploader;
import com.yuyue.cu.core.web.form.FileUploadForm;
import com.yuyue.cu.util.WebUtils;
import com.yuyue.util.CommConstants;
import com.yuyue.util.ImageUtils;
import com.yuyue.util.OperateImage;
import com.yuyue.util.OperateJpgImage;
import com.yuyue.util.PictureUtil;
import com.yuyue.util.StreamUtils;
import com.yuyue.util.StringUtils;

public class GlobalAction extends AbstractDispatchAction {
	
	private static final double biggerRate = 1.5;
	private static final int SW = 700;
	private static final int SH = 700;
	
	public ActionForward doUploadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		FormFile formFile = null;
		InputStream inputStream = null;
		File imgFile = null;
		try{
			JSONObject jsonObject = new JSONObject();
			FileUploadForm fileUploadForm = (FileUploadForm)form;
			String filePath = request.getServletContext().getRealPath(CommConstants.FILE_SRC);
			String path = request.getParameter("path");
			String multi = request.getParameter("multi");
			String fixname = request.getParameter("fixname");
			int w = 0;
			int h = 0;
			if(request.getParameter("w") != null){
				w = (int)Double.parseDouble(request.getParameter("w"));
			}
			if(request.getParameter("h") != null){
				h = (int)Double.parseDouble(request.getParameter("h"));
			}
			// handle cellphone
			int smallWidth = SW;
			int smallHeight = SH;
			
			w *= biggerRate;
			h *= biggerRate;
			String accept = request.getParameter("accept");
			String _maxsize = request.getParameter("maxsize");
			double maxsize = 10000000;
			if(_maxsize != null){
				maxsize = Double.parseDouble(_maxsize)*1000000d;
			}
			if("multi".equals(multi) || "single".equals(multi)){
				formFile = fileUploadForm.getFormFile();
				String uploadFileName = formFile.getFileName();
				uploadFileName = uploadFileName.toLowerCase();
				// exclude exe sh
				if(uploadFileName.endsWith(".exe") || uploadFileName.endsWith(".sh") || uploadFileName.endsWith(".bat") || uploadFileName.endsWith(".php") || uploadFileName.endsWith(".jsp") || uploadFileName.endsWith(".asp") || uploadFileName.endsWith(".sql") || uploadFileName.endsWith(".com") || uploadFileName.endsWith(".html")){
					request.setAttribute("uploadFailed", "上传文件格式不正确");
					return mapping.findForward("upload_failed");
				}
				
				if(!StringUtils.isEmpty(path) && formFile != null){
					if(accept != null && accept.indexOf("*") == -1){
						String[] types = accept.split(",");
						if(types != null){
							boolean acceptable = false;
							for(String type : types){
								if(uploadFileName.endsWith("."+type.toLowerCase())){
									acceptable = true;
								}
							}
							if(!acceptable){
								request.setAttribute("uploadFailed", "上传文件格式不正确");
								return mapping.findForward("upload_failed");
							}
						}
					}
					if(formFile.getFileSize() > maxsize){
						request.setAttribute("uploadFailed", "文件大小不能超过"+(maxsize/1000000)+"M");
						return mapping.findForward("upload_failed");
					}
					String suffix = formFile.getFileName().substring(formFile.getFileName().lastIndexOf("."));
					String fileName = null;
					String newFileName = null;
					Date nowDate = new Date();
					String fileType = request.getParameter("fileType");
					if("files".equals(fileType)){
						fileName = formFile.getFileName().substring(0,formFile.getFileName().lastIndexOf("."));
						newFileName = fileName+"-"+CommConstants.SDF_TIMER.format(nowDate)+suffix;
					}else{
						fileName = CommConstants.SDF_TIMER.format(nowDate) + WebUtils.generateRandomNumber(11);
						newFileName = fileName + suffix;
					}
					String folderPath = filePath+File.separator+path;
					inputStream = formFile.getInputStream();
					if(fixname != null){
						newFileName = fixname;
					}
					new FileUploader(inputStream,folderPath,newFileName);
					if("single".equals(multi)){
						request.setAttribute("name", newFileName);
						request.setAttribute("index", request.getParameter("index"));
						// to find if the image is uneditable
						try{
							String fileFullPath = folderPath+File.separator+newFileName;
							Integer[] wh = ImageUtils.getImageWH(fileFullPath);
							if(wh != null){
								if(fixname != null){
									if(!fileFullPath.toLowerCase().endsWith(".gif")){
										PictureUtil.resizePNG(fileFullPath, fileFullPath, smallWidth>wh[0]?wh[0]:smallWidth, smallHeight>wh[1]?wh[1]:smallHeight, true);
									}
//								ImageUtils.resize(fileFullPath, wh[0], wh[1], true);
								}else{
									String bigFileFullPath = folderPath+File.separator+getBigImgName(newFileName);
									String smallFileFullPath = folderPath+File.separator+getSmallImgName(newFileName);
									StreamUtils.copyFile(fileFullPath, bigFileFullPath);
									if(fileFullPath.toLowerCase().endsWith(".gif")){
										StreamUtils.copyFile(fileFullPath, smallFileFullPath);
									}else{
										PictureUtil.resizePNG(bigFileFullPath, fileFullPath, w>wh[0]?wh[0]:w, h>wh[1]?wh[1]:h, true);
										PictureUtil.resizePNG(bigFileFullPath, smallFileFullPath, smallWidth>wh[0]?wh[0]:smallWidth, smallHeight>wh[1]?wh[1]:smallHeight, true);
									}
//								ImageUtils.resize(smallFileFullPath, wh[0], wh[1], true);
									if(w == -1 || h == -1 || (wh[0] < 400 && wh[1] < 400) || (wh[0] < 2000 && wh[1] < 2000 && (w > 1000 || h > 1000))){
										// do nothing...
									}else if(w == 0 || h == 0 || (wh[0] < 800 && wh[1] < 800) || (w > 1000 || h > 1000)){
										//ImageUtils.resize(fileFullPath, wh[0], wh[1], true);
									}else{
										/*double rate = 1;
									if(w >= h){
										rate = (double)w / (double)wh[0];
									}else{
										rate = (double)h / (double)wh[1];
									}
									ImageUtils.resize(fileFullPath, (int)(wh[0]*rate), (int)(wh[1]*rate), true);*/
									}
								}
							}
							request.setAttribute("disableEdit", false);
						}catch(Exception e){
							request.setAttribute("disableEdit", true);
						}
						return mapping.findForward("image_upload_edit");
					}else{
						if("files".equals(fileType)){
							imgFile = new File(folderPath+File.separator+newFileName);
							if(imgFile.length() > 0){
								fileName = imgFile.getName();
								float fileSize = imgFile.length()/1000f;
								suffix = fileName.substring(fileName.lastIndexOf(".")+1);
								jsonObject.put("path", path);
								jsonObject.put("type", suffix);
								jsonObject.put("name", fileName);
								jsonObject.put("size", String.valueOf(fileSize)+"KB");
							}
						}else{
							imgFile = new File(folderPath+File.separator+newFileName);
							if(imgFile.length() > 0){
								BufferedImage image = ImageIO.read(imgFile);
								int width = image.getWidth();
								int height = image.getHeight();
								double rate = WebUtils.getAdjustRate(80, 75, width, height);
								fileName = imgFile.getName();
								float fileSize = imgFile.length()/1000f;
								suffix = fileName.substring(fileName.lastIndexOf(".")+1);
								jsonObject.put("width", String.valueOf(width*rate));
								jsonObject.put("height", String.valueOf(height*rate));
								jsonObject.put("path", path);
								jsonObject.put("type", suffix);
								jsonObject.put("name", fileName);
								jsonObject.put("size", String.valueOf(fileSize)+"KB");
								// to find if the image is uneditable
								try{
									String fileFullPath = folderPath+File.separator+newFileName;
									Integer[] wh = ImageUtils.getImageWH(fileFullPath);
									if(wh != null){
										String bigFileFullPath = folderPath+File.separator+getBigImgName(newFileName);
										String smallFileFullPath = folderPath+File.separator+getSmallImgName(newFileName);
										StreamUtils.copyFile(fileFullPath, bigFileFullPath);
										if(fileFullPath.toLowerCase().endsWith(".gif")){
											StreamUtils.copyFile(fileFullPath, smallFileFullPath);
										}else{
											PictureUtil.resizePNG(bigFileFullPath, fileFullPath, w>wh[0]?wh[0]:w, h>wh[1]?wh[1]:h, true);
											PictureUtil.resizePNG(bigFileFullPath, smallFileFullPath, smallWidth>wh[0]?wh[0]:smallWidth, smallHeight>wh[1]?wh[1]:smallHeight, true);
										}
//									ImageUtils.resize(smallFileFullPath, wh[0], wh[1], true);
//									fileUploader.generate(wh[0], wh[1]);
										if(w == -1 || h == -1 || (wh[0] < 400 && wh[1] < 400)){
											// do nothing...
										}else if(w == 0 || h == 0 || (wh[0] < 800 && wh[1] < 800)){
											//ImageUtils.resize(fileFullPath, wh[0], wh[1], true);
										}else{
											/*if(w >= h){
											rate = (double)w / (double)wh[0];
										}else{
											rate = (double)h / (double)wh[1];
										}
										ImageUtils.resize(fileFullPath, (int)(wh[0]*rate), (int)(wh[1]*rate), true);*/
										}
									}
									jsonObject.put("disabled", "");
									jsonObject.put("disabledMsg", "");
								}catch(Exception e){
									jsonObject.put("disabled", "disabled=\"disabled\"");
									jsonObject.put("disabledMsg", "<span style=\"font-size:5px;\">(仅支持jpg)</span>");
								}
							}
						}
						WebUtils.printJsonObject(response, jsonObject);
					}
					return null;
				}else{
					return null;
				}
			}else{
				return null;
			}
		}catch(Exception e){
			return mapping.findForward("upload_error");
		}finally{
			formFile = null;
			inputStream = null;
			imgFile = null;
		}
	}
	
	public ActionForward doFindFiles(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObject = null;
		File imgFile = null;
		JSONArray authorArray = new JSONArray();
		try{
			String singleName = request.getParameter("name");
			String names = request.getParameter("names");
			String path = request.getParameter("path");
			String filePath = request.getServletContext().getRealPath(CommConstants.FILE_SRC);
			String folderPath = filePath+File.separator+path;
			if(singleName != null){
				jsonObject = new JSONObject();
				if(!StringUtils.isEmpty(singleName) && !StringUtils.isEmpty(path)){
					String fp = folderPath + File.separator + singleName;
					imgFile = new File(fp);
					if(imgFile.length() > 0){
						jsonObject.put("src", request.getContextPath()+"/"+CommConstants.FILE_SRC+"/"+path+"/"+singleName);
					}else{
						jsonObject.put("src", request.getContextPath()+CommConstants.NO_IMAGE_SRC);
					}
				}else{
					jsonObject.put("src", request.getContextPath()+CommConstants.NO_IMAGE_SRC);
				}
				WebUtils.printJsonObject(response, jsonObject);
			}else{
				if(!StringUtils.isEmpty(names) && !StringUtils.isEmpty(path)){
					jsonObject = new JSONObject();
					String[] nameArray = names.split(",");
					for(String name : nameArray){
						if(!StringUtils.isEmpty(name)){
							String fp = folderPath + File.separator + name;
							imgFile = new File(fp);
							if(imgFile.length() > 0){
								BufferedImage image = ImageIO.read(imgFile);
								if(imgFile != null){
									if(image == null){
										String fileName = imgFile.getName();
										float fileSize = imgFile.length()/1000f;
										String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
										jsonObject.put("path", path);
										jsonObject.put("type", suffix);
										jsonObject.put("name", fileName);
										jsonObject.put("size", String.valueOf(fileSize)+"KB");
										authorArray.add(JSONObject.fromObject(jsonObject));
									}else{
										int width = image.getWidth();
										int height = image.getHeight();
										double rate = WebUtils.getAdjustRate(80, 75, width, height);
										String fileName = imgFile.getName();
										float fileSize = imgFile.length()/1000f;
										String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
										Map<String, String> imageInfor = new HashMap<String, String>();
										imageInfor.put("width", String.valueOf(width*rate));
										imageInfor.put("height", String.valueOf(height*rate));
										imageInfor.put("path", path);
										imageInfor.put("type", suffix);
										imageInfor.put("name", fileName);
										imageInfor.put("size", String.valueOf(fileSize)+"KB");
										
										imageInfor.put("disabled", "");
										imageInfor.put("disabledMsg", "");
										/*// to find if the image is uneditable
										try{
											String fileFullPath = fp;
											Integer[] wh = ImageUtils.getImageWH(fileFullPath);
											OperateImage o = new OperateImage(0, 0, wh[0], wh[1]);
											o.setSrcpath(fileFullPath);
											o.setSubpath(fileFullPath);
											o.cut();
											imageInfor.put("disabled", "");
											imageInfor.put("disabledMsg", "");
										}catch(Exception e){
											imageInfor.put("disabled", "disabled=\"disabled\"");
											imageInfor.put("disabledMsg", "<span style=\"font-size:5px;\">(仅支持jpg)</span>");
										}*/
										authorArray.add(JSONObject.fromObject(imageInfor));
									}
								}
							}
						}
					}
					jsonObject.put("result", authorArray);
					WebUtils.printJsonObject(response, jsonObject);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			jsonObject = null;
			imgFile = null;
			authorArray = null;
		}
		return null;
	}
	
	public ActionForward doRemoveFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		JSONObject jsonObject = new JSONObject();
		try{
			String path = request.getParameter("path");
			String name = request.getParameter("name");
			if(!StringUtils.isEmpty(path) && !StringUtils.isEmpty(name)){
				String filePath = request.getServletContext().getRealPath(CommConstants.FILE_SRC);
				String folderPath = filePath+File.separator+path;
				if(name != null && name.indexOf("-small") == -1){
					String bigName = name.substring(0,name.lastIndexOf(".")) + "-big" + name.substring(name.lastIndexOf("."));
					StreamUtils.deleteFile(folderPath+File.separator+bigName);
					String smallName = name.substring(0,name.lastIndexOf(".")) + "-small" + name.substring(name.lastIndexOf("."));
					StreamUtils.deleteFile(folderPath+File.separator+smallName);
					boolean success = StreamUtils.deleteFile(folderPath+File.separator+name);
					if(success){
						jsonObject.put("success", true);
						WebUtils.printJsonObject(response, jsonObject);
					}
				}else{
					jsonObject.put("success", true);
					WebUtils.printJsonObject(response, jsonObject);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			jsonObject = null;
		}
		return null;
	}
	
	public ActionForward doCropImage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try{
			String path = request.getParameter("path");
			String name = request.getParameter("name");
			String _width = request.getParameter("width");
			String _height = request.getParameter("height");
			int x = (int)Double.parseDouble(request.getParameter("x"));
			int y = (int)Double.parseDouble(request.getParameter("y"));
			double w = Double.parseDouble(request.getParameter("w"));
			double h = Double.parseDouble(request.getParameter("h"));
			String filePath = request.getServletContext().getRealPath(CommConstants.FILE_SRC);
			String folderPath = filePath+File.separator+path;
			String fileFullPath = folderPath+File.separator+name;
			if(!StringUtils.isEmpty(path) && !StringUtils.isEmpty(name)){
				try{
					OperateImage o = new OperateImage(x, y, (int)w, (int)h);
					o.setSrcpath(fileFullPath);
					o.setSubpath(fileFullPath);
					o.cut();
					o = null;
				}catch(Exception e){
					OperateJpgImage o = new OperateJpgImage(x, y, (int)w, (int)h);
					o.setSrcpath(fileFullPath);
					o.setSubpath(fileFullPath);
					o.cut();
					o = null;
				}
			}
			if(_width != null && _height != null){
				Integer[] wh = ImageUtils.getImageWH(fileFullPath);
				if(wh != null){
					double width = Double.parseDouble(_width);
					double height = Double.parseDouble(_height);
					if(wh != null && wh[0] > width || wh[1] > height){
						ImageUtils.resize(fileFullPath, (int)width, (int)height, true);
					}
				}
			}
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return null;
	}
	
	private static String getBigImgName(String name){
		String bigName = name;
		if(name != null && name.indexOf("/placehold/") == -1){
			int offset = name.lastIndexOf(".");
			if(offset != -1){
				bigName = name.substring(0,offset) + "-big" + name.substring(offset);
			}
		}
		return bigName;
	}
	
	private static String getSmallImgName(String name){
		String bigName = name;
		if(name != null && name.indexOf("/placehold/") == -1){
			int offset = name.lastIndexOf(".");
			if(offset != -1){
				bigName = name.substring(0,offset) + "-small" + name.substring(offset);
			}
		}
		return bigName;
	}
	
}
