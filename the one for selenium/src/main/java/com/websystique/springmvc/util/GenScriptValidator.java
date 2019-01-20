package com.websystique.springmvc.util;

import com.websystique.springmvc.model.GenerateScript;
import com.websystique.springmvc.model.UserDocument;
import com.websystique.springmvc.service.UserDocumentService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class GenScriptValidator implements Validator {

    @Value("${number.id}")
    private int USERID;

    @Autowired
    UserDocumentService userDocumentService;

    public boolean supports(Class<?> clazz) {
        return GenerateScript.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        GenerateScript file = (GenerateScript) obj;
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]*$");
        Matcher matcher = pattern.matcher(file.getName());
        boolean found = matcher.find();

        if(!found){
            errors.rejectValue("name", "format.fileName");
        }

        List<UserDocument> documentList = userDocumentService.findAllByUserId(USERID);
        String str = FilenameUtils.removeExtension(file.getName());
        for (UserDocument doc : documentList) {
            if (str.equals(FilenameUtils.removeExtension(doc.getName()))) {
                errors.rejectValue("name", "object.exist");
            }

        }
    }

}

