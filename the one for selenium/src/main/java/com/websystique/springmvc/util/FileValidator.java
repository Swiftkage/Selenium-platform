package com.websystique.springmvc.util;

import com.websystique.springmvc.model.FileBucket;
import com.websystique.springmvc.model.UserDocument;
import com.websystique.springmvc.service.UserDocumentService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class FileValidator implements Validator {

	@Value("${number.id}")
	private int USERID;

	@Autowired
	UserDocumentService userDocumentService;
		
	public boolean supports(Class<?> clazz) {
		return FileBucket.class.isAssignableFrom(clazz);
	}

	public void validate(Object obj, Errors errors) {
		FileBucket file = (FileBucket) obj;

		MultipartFile f = file.getFile();
		String fileStr = null;
		try {
			fileStr = new String(f.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		final String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
				"<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n" +
				"<head profile=\"http://selenium-ide.openqa.org/profiles/test-case\">\n" +
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />";

		Pattern pattern = Pattern.compile("^[a-zA-Z0-9]*$");
		String temp = FilenameUtils.removeExtension(file.getFile().getOriginalFilename());
		Matcher matcher = pattern.matcher(temp);
		boolean found = matcher.find();

		if(file.getFile()!=null){
			if (file.getFile().getSize() == 0) {
				errors.rejectValue("file", "missing.file");
			}

			else if(file.getFile().getSize() > 1024 * 1024)
			{
				errors.rejectValue("file", "maxSize.file");
			}

			else if(!file.getFile().getContentType().equals("text/html")
					//&& !file.getFile().getContentType().equals("application/octet-stream")
					){
				errors.rejectValue("file", "format.file");
			}
			else if(!found){
				errors.rejectValue("file", "format.fileName");
			}
			else if(!fileStr.contains(str)){
				errors.rejectValue("file", "format.file2");
			}

			List<UserDocument> documentList = userDocumentService.findAllByUserId(USERID);
			String tmp1 = FilenameUtils.removeExtension(file.getFile().getOriginalFilename());

			for (UserDocument doc : documentList) {
				if (tmp1.equals(FilenameUtils.removeExtension(doc.getName()))) {
					errors.rejectValue("file", "object.exist");
				}

			}
		}
	}
}

