package com.websystique.springmvc.controller;

import com.websystique.springmvc.model.*;
import com.websystique.springmvc.service.BrowserService;
import com.websystique.springmvc.service.UserDocumentService;
import com.websystique.springmvc.service.UserService;
import com.websystique.springmvc.util.File2Validator;
import com.websystique.springmvc.util.FileValidator;
import com.websystique.springmvc.util.GenScriptValidator;
import com.websystique.springmvc.util.SuiteValidator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping("/")
public class AppController {

    @Value("${path.location}")
    private String LOC;

    @Value("${number.id}")
    private int USERID; //yes, the user id is static for now, but the database is alr created. Login as future enhancement

    @Autowired
    UserService userService;

    @Autowired
    UserDocumentService userDocumentService;

    @Autowired
    BrowserService browserService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    FileValidator fileValidator;

    @Autowired
    File2Validator file2Validator;

    @Autowired
    SuiteValidator suiteValidator;

    @Autowired
    GenScriptValidator genScriptValidator;

    @InitBinder("fileBucket")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(fileValidator);
    }

    @InitBinder("fileChange")
    protected void initBinder2(WebDataBinder binder) {
        binder.setValidator(file2Validator);
    }

    @InitBinder("suite")
    protected void initBinder3(WebDataBinder binder) {
        binder.setValidator(suiteValidator);
    }

    @InitBinder("generateScript")
    protected void initBinder4(WebDataBinder binder) {
        binder.setValidator(genScriptValidator);
    }


    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String Home(ModelMap model) {

        return "home";
    }

    @RequestMapping(value = {"/404"}, method = RequestMethod.GET)
    public String Redirect404(ModelMap model) {

        return "404";
    }

    @RequestMapping(value = {"/500"}, method = RequestMethod.GET)
    public String Redirect500(ModelMap model) {

        return "500";
    }

    @RequestMapping(value = {"/generic"}, method = RequestMethod.GET)
    public String Redirect5Generic(ModelMap model) {

        return "errorgeneric";
    }

    @RequestMapping(value = {"/index"}, method = RequestMethod.GET)
    public String Index(ModelMap model) {
        List<UserDocument> documents = userDocumentService.findAllByUserId(USERID);
        model.addAttribute("documents", documents);

        return "index";
    }

    @RequestMapping(value = {"/script/upload"}, method = RequestMethod.GET)
    public String scriptUpload(ModelMap model) {
        FileBucket fileModel = new FileBucket();
        model.addAttribute("fileBucket", fileModel);
        return "uploadscript";
    }


    @RequestMapping(value = {"/script/upload"}, method = RequestMethod.POST)
    public String uploadDoc(@Valid FileBucket fileBucket, BindingResult result, ModelMap model
            , RedirectAttributes redirectAttrs) throws IOException {

//        if (result.hasErrors()) {
//            System.out.println("validation errors");
//
//            return "uploadscript";
//        } else {
            System.out.println("Fetching file");
            User user = userService.findById(USERID);
            String name = saveDocument(fileBucket, user);


            redirectAttrs.addFlashAttribute("MESSAGE",
                    name + " has been successfully added!");
            return "redirect:/index";
        //}
    }


    @RequestMapping(value = {"/script/{docId}"}, method = RequestMethod.GET)
    public String Documents(@PathVariable int docId, ModelMap model) throws IOException {

        UserDocument document = userDocumentService.findById(docId);

        FileChange f = new FileChange();
        f.setId(document.getId());
        f.setName(FilenameUtils.removeExtension(document.getName()));
        f.setDescription(document.getDescription());
        f.setContent(new String(document.getContent()));
        f.setCron(document.getCron());
        model.addAttribute("fileChange", f);
        model.addAttribute("document", document);

        return "managedoc";
    }

    @RequestMapping(value = {"/script/{docId}"}, method = RequestMethod.POST)
    public String updateDoc(@Valid FileChange fileChange, BindingResult result, ModelMap model, @PathVariable int docId,
                            RedirectAttributes redirectAttrs) throws IOException {

        if (result.hasErrors()) {
            System.out.println("validation errors");

            UserDocument document = userDocumentService.findById(docId);

            model.addAttribute("expandcollapse", "false");
            model.addAttribute("document", document);

            return "managedoc";
        } else {

            UserDocument document = userDocumentService.findById(docId);

            String name = updateDocument(document, fileChange);
            User user = userService.findById(USERID);
            Browser browser = browserService.findByName(user.getBrowser());

            Quartz.scheduleJob(document, browser);
            redirectAttrs.addFlashAttribute("MESSAGE2",
                    name + " has been successfully updated!");
            return "redirect:/script/" + docId;
        }
    }

    @RequestMapping(value = {"/suite/{docId}"}, method = RequestMethod.GET)
    public String Suites(@PathVariable int docId, ModelMap model) throws IOException {

        UserDocument document = userDocumentService.findById(docId);

        FileChange f = new FileChange();
        f.setId(document.getId());
        f.setName(FilenameUtils.removeExtension(document.getName()));
        f.setDescription(document.getDescription());
        f.setContent(new String(document.getContent()));
        f.setCron(document.getCron());
        model.addAttribute("fileChange", f);
        model.addAttribute("document", document);

        return "managesuite";
    }

    @RequestMapping(value = {"/suite/{docId}"}, method = RequestMethod.POST)
    public String updateSuite(@Valid FileChange fileChange, BindingResult result, ModelMap model, @PathVariable int docId,
                              RedirectAttributes redirectAttrs) throws IOException {

        if (result.hasErrors()) {
            System.out.println("validation errors");

            UserDocument document = userDocumentService.findById(docId);

            model.addAttribute("expandcollapse", "false");
            model.addAttribute("document", document);

            return "managesuite";
        } else {

            UserDocument document = userDocumentService.findById(docId);

            String name = updateSuite(document, fileChange);
            User user = userService.findById(USERID);
            Browser browser = browserService.findByName(user.getBrowser());

            Quartz.scheduleJob(document, browser);
            redirectAttrs.addFlashAttribute("MESSAGE2",
                    name + " has been successfully updated!");
            return "redirect:/suite/" + docId;
        }
    }

    @RequestMapping(value = {"/{docId}/script"}, method = RequestMethod.GET)
    public void dlScript(@PathVariable int docId, ModelMap model, HttpServletResponse response) throws IOException {
        UserDocument document = userDocumentService.findById(docId);
        response.setContentType(document.getType());
        response.setContentLength(document.getContent().length);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + document.getName() + "\"");

        FileCopyUtils.copy(document.getContent(), response.getOutputStream());
    }


    @RequestMapping(value = {"/script/{docId}/result"}, method = RequestMethod.GET)
    public String dlResult(@PathVariable int docId, ModelMap model, HttpServletResponse response,
                           RedirectAttributes redirectAttrs) throws IOException {
        UserDocument document = userDocumentService.findById(docId);

        String tempName = docId + "/";

        File dir = new File(LOC + tempName);
        ArrayList<String> list = new ArrayList<String>();

        for (File file : dir.listFiles()) {
            if (file.getName().endsWith((".zip"))) {
                list.add(file.getName());
            }
        }
        String[] arr = new String[list.size()];


        model.addAttribute("document", document);
        model.addAttribute("results", list);

        return "managedocresults";
    }

    @RequestMapping(value = {"/script/{docId}/result"}, method = RequestMethod.POST)
    public String dlResultPost(String result, @PathVariable int docId, ModelMap model, HttpServletResponse response,
                               RedirectAttributes redirectAttrs) throws IOException {

        String tempName = LOC + docId + "/" + result;

        File f = new File(tempName);

        if (f.exists()) {
            response.setContentType("application/zip");
            response.setContentLength((int) f.length());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"");
            Path path = Paths.get(f.getPath());
            byte[] zipFileBytes = Files.readAllBytes(path);
            FileCopyUtils.copy(zipFileBytes, response.getOutputStream());
        } else {
            redirectAttrs.addFlashAttribute("MESSAGE",
                    "No results found.");

        }
        return "redirect:/script/" + docId;

    }

    @RequestMapping(value = {"/suite/{docId}/result"}, method = RequestMethod.GET)
    public String dlResultsuite(@PathVariable int docId, ModelMap model, HttpServletResponse response,
                                RedirectAttributes redirectAttrs) throws IOException {
        UserDocument document = userDocumentService.findById(docId);

        String tempName = docId + "/";

        File dir = new File(LOC + tempName);
        ArrayList<String> list = new ArrayList<String>();

        for (File file : dir.listFiles()) {
            if (file.getName().endsWith((".zip"))) {
                list.add(file.getName());
            }
        }
        String[] arr = new String[list.size()];


        model.addAttribute("document", document);
        model.addAttribute("results", list);

        return "managesuiteresults";
    }

    @RequestMapping(value = {"/suite/{docId}/result"}, method = RequestMethod.POST)
    public String dlResultsuitePost(String result, @PathVariable int docId, ModelMap model, HttpServletResponse response,
                                    RedirectAttributes redirectAttrs) throws IOException {
        String tempName = LOC + docId + "/" + result;

        File f = new File(tempName);

        if (f.exists()) {
            response.setContentType("application/zip");
            response.setContentLength((int) f.length());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + f.getName() + "\"");
            Path path = Paths.get(f.getPath());
            byte[] zipFileBytes = Files.readAllBytes(path);
            FileCopyUtils.copy(zipFileBytes, response.getOutputStream());
        } else {
            redirectAttrs.addFlashAttribute("MESSAGE",
                    "No results found.");

        }

        return "redirect:/suite/" + docId;
    }

    @ResponseBody
    @RequestMapping(value = {"/rundoc"}, method = RequestMethod.POST)
    public void runDoc(int docId) throws IOException, ParseException {
        UserDocument document = userDocumentService.findById(docId);
        User user = userService.findById(USERID);
        Browser browser = browserService.findByName(user.getBrowser());
        Quartz.runMethod(document, browser);
    }

    @ResponseBody
    @RequestMapping(value = {"/deletedoc"}, method = RequestMethod.POST)
    public void deleteDoc(int docId, RedirectAttributes redirectAttrs) throws IOException {
        final UserDocument document = userDocumentService.findById(docId);
        String scriptName = document.getName();

        //delete document directory
        File f = new File(LOC + docId + "/");
        if (f.exists()) {
            FileUtils.deleteDirectory(f);
        }

        //delete schedule
        document.setCron("");
        Quartz.scheduleJob(document, null);

        userDocumentService.deleteById(docId);

        //delete related items found in suites
        File dir = new File(LOC);
        Collection<File> files = FileUtils.listFiles(dir,
                new NameFileFilter(scriptName),
                DirectoryFileFilter.DIRECTORY);

        for (File tmpFile : files
                ) {
            String path = tmpFile.getAbsolutePath();
            System.out.println(path);
            String suiteID = StringUtils.substringBetween(path, "Files\\", "\\");
            System.out.println(suiteID);

            File tempFile = new File(path);
            tmpFile.delete();

            UserDocument tmpSuite = userDocumentService.findById(Integer.parseInt(suiteID));
            System.out.println(tmpSuite);

            String tmpContent = new String(tmpSuite.getContent());

            String[] list = StringUtils.substringsBetween(tmpContent, "<a href=\"", "\">");
            System.out.println(Arrays.toString(list));

            if (list.length == 1) {
                File f2 = new File(LOC + tmpSuite.getId() + "/");

                if (f2.exists()) {
                    FileUtils.deleteDirectory(f2);
                }

                tmpSuite.setCron("");
                Quartz.scheduleJob(tmpSuite, null);

                userDocumentService.deleteById(tmpSuite.getId());

            } else {
                String t = "<tr><td><a href=\"" + scriptName + "\">" + FilenameUtils.removeExtension(scriptName) + "</a></td></tr>";
                System.out.println(t);
                tmpContent = tmpContent.replaceAll(t, "");
                System.out.println(tmpContent);

                FileChange change = new FileChange();
                change.setId(tmpSuite.getId());
                change.setCron(tmpSuite.getCron());
                change.setDescription(tmpSuite.getDescription());
                change.setName(tmpSuite.getName());
                change.setContent(tmpContent);

                updateDocument(tmpSuite, change);
                User user = userService.findById(USERID);
                Browser browser = browserService.findByName(user.getBrowser());

                Quartz.scheduleJob(tmpSuite, browser);
            }
        }


        redirectAttrs.addFlashAttribute("MESSAGE",
                "script has been successfully deleted!");
    }


    @ResponseBody
    @RequestMapping(value = {"/deletesuite"}, method = RequestMethod.POST)
    public void deleteSuite(int docId) throws IOException {
        UserDocument document = userDocumentService.findById(docId);
        File f = new File(LOC + docId + "/");

        if (f.exists()) {
            FileUtils.deleteDirectory(f);
        }

        document.setCron("");
        Quartz.scheduleJob(document, null);

        userDocumentService.deleteById(docId);


    }

    @RequestMapping(value = {"/user"}, method = RequestMethod.GET)
    public String UserInfo(ModelMap model) {

        User user = userService.findById(USERID);
        List<Browser> browsers = browserService.findAllBrowsers();
        model.addAttribute("user", user);
        model.addAttribute("browsers", browsers);
        return "user";
    }

    /*@RequestMapping(value = {"/testbrowser"}, method = RequestMethod.POST)
    public boolean testBrowser(String browser) {

        Browser b = browserService.findByName(browser);
        boolean check = Quartz.runTestMethod(b);
        return check;
    }*/

    @RequestMapping(value = {"/user"}, method = RequestMethod.POST)
    public String UserInfoPOST(@Valid User userUpdate, BindingResult result, ModelMap model,
                               RedirectAttributes redirectAttrs) throws IOException {

        if (result.hasErrors()) {
            System.out.println("validation errors");
            return "user";
        } else {
            boolean check = false;
            User user = userService.findById(USERID);
            if (!user.getEmail().equals(userUpdate.getEmail())) { //update scheduling jobs,if email was changed
                check = true;
            } else if (!user.getBrowser().equals(userUpdate.getBrowser())) { //update scheduling jobs,if email was changed
                check = true;
            }
            userService.updateUser(userUpdate);

            if (check) {
                List<UserDocument> documents = userDocumentService.findAllByUserId(USERID);
                Browser browser = browserService.findByName(userUpdate.getBrowser());
                for (UserDocument doc : documents
                        ) {
                    Quartz.scheduleJob(doc, browser);
                }
            }

            redirectAttrs.addFlashAttribute("MESSAGE",
                    "User has been successfully updated!");
            return "redirect:/user";
        }
    }

    @RequestMapping(value = {"/suite"}, method = RequestMethod.GET)
    public String Suite(ModelMap model) {
        List<UserDocument> documents = userDocumentService.findAllByUserId(USERID);
        model.addAttribute("documents", documents);

        return "suite";
    }

    @RequestMapping(value = {"/suite/generate"}, method = RequestMethod.GET)
    public String Suitegenerate(ModelMap model) {
        List<UserDocument> documents = userDocumentService.findAllByUserId(USERID);
        model.addAttribute("documents", documents);

        String result = "";
        boolean first = true;
        for (UserDocument doc : documents) {
            if (doc.getType().equals("text/html")) {
                if (first) {
                    result += doc.getName();
                    first = false;
                } else {
                    result += "," + doc.getName();
                }
            }
        }

        model.addAttribute("list", result);

        Suite s = new Suite();
        model.addAttribute("suite", s);
        return "generatesuite";
    }

    @RequestMapping(value = {"/suite/generate"}, method = RequestMethod.POST)
    public String SuiteInfoPOST(@Valid Suite suite, BindingResult result, ModelMap model,
                                RedirectAttributes redirectAttrs) throws IOException {

        if (result.hasErrors()) {
            System.out.println("validation errors");

            List<UserDocument> documents = userDocumentService.findAllByUserId(USERID);
            model.addAttribute("documents", documents);

            String results = "";
            boolean first = true;
            for (UserDocument doc : documents) {
                if (doc.getType().equals("text/html")) {
                    if (first) {
                        results += doc.getName();
                        first = false;
                    } else {
                        results += "," + doc.getName();
                    }
                }
            }

            model.addAttribute("list", results);

            return "generatesuite";
        } else {
            User user = userService.findById(USERID);
            UserDocument doc = SuiteTemplate.createSuite(suite, user);
            userDocumentService.saveDocument(doc);

            createSuiteDirectory(doc);

            redirectAttrs.addFlashAttribute("MESSAGE",
                    "Suite has been successfully added!");
            return "redirect:/suite";
        }
    }

    @RequestMapping(value = {"/script/generate"}, method = RequestMethod.GET)
    public String generateScript(ModelMap model) {
        GenerateScript script = new GenerateScript();
        model.addAttribute("generateScript", script);
        return "generatescript";
    }

    @RequestMapping(value = {"/script/generate"}, method = RequestMethod.POST)
    public String generateScriptPOST(@Valid GenerateScript tempScript, BindingResult result, ModelMap model,
                                     RedirectAttributes redirectAttrs) throws IOException {

        if (result.hasErrors()) {
            System.out.println("validation errors");
            return "generatescript";
        } else {
            User user = userService.findById(USERID);
            UserDocument doc = ScriptTemplate.createScript(tempScript, user);

            userDocumentService.saveDocument(doc);

            createScriptDirectory(doc);

            redirectAttrs.addFlashAttribute("MESSAGE",
                    "Script has been successfully added!");
            return "redirect:/index";
        }
    }

    @RequestMapping(value = {"/about"}, method = RequestMethod.GET)
    public String About(ModelMap model) {

        return "about";
    }

    @RequestMapping(value = {"/script/{docId}/deleteresults"}, method = RequestMethod.GET)
    public String DeleteScriptResults(@PathVariable int docId, ModelMap model, RedirectAttributes redirectAttrs) throws IOException {

        File dir = new File(LOC + docId + "/");

        for (File file : dir.listFiles()) {
            if (file.getName().endsWith((".zip"))) {
                file.delete();
            }
            else if(file.isDirectory()){
                FileUtils.deleteDirectory(file);
            }
        }

            redirectAttrs.addFlashAttribute("MESSAGE2",
                    "Results have been cleared!");

            return "redirect:/script/" + docId;

    }

    @RequestMapping(value = {"/suite/{docId}/deleteresults"}, method = RequestMethod.GET)
    public String DeleteSuiteResults(@PathVariable int docId, ModelMap model, RedirectAttributes redirectAttrs) throws IOException {

        File dir = new File(LOC + docId + "/");

        for (File file : dir.listFiles()) {
            if (file.getName().endsWith((".zip"))) {
                file.delete();
            }
            else if(file.isDirectory()){
                FileUtils.deleteDirectory(file);
            }
        }

        redirectAttrs.addFlashAttribute("MESSAGE2",
                "Results have been cleared!");

        return "redirect:/suite/" + docId;

    }


    private String saveDocument(FileBucket fileBucket, User user) throws IOException {

        UserDocument document = new UserDocument();

        MultipartFile multipartFile = fileBucket.getFile();

        document.setName(multipartFile.getOriginalFilename());
        document.setDescription(fileBucket.getDescription());
        document.setType(multipartFile.getContentType());
        document.setContent(multipartFile.getBytes());
        document.setUser(user);
        document.setCron("");
        userDocumentService.saveDocument(document);

        createScriptDirectory(document);

        return multipartFile.getOriginalFilename();
    }

    private String updateDocument(UserDocument document, FileChange file) throws IOException {
        String oldName = document.getName();
        String tmpName = file.getName();
        if (document.getType().equals("text/html")) {
            tmpName = file.getName() + ".html";
        }
        boolean check = false;
        //delete, then create the directory again later if the name is changed
        if (!document.getName().equals(tmpName)) {
            File dir = new File(LOC + document.getId() + "/");

            if (dir.exists()) {

                FileUtils.deleteDirectory(dir);
            }
            dir.mkdir();
            check = true;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        document.setName(tmpName);
        document.setDescription(file.getDescription());
        document.setContent(file.getContent().getBytes());
        document.setCron(file.getCron());
        System.out.println(document.toString());
        userDocumentService.updateDocument(document);

        if(check){
            System.out.println("rrrrrrrrrrrrrrrr");
            File dirSuite = new File(LOC);
            Collection<File> files = FileUtils.listFiles(dirSuite,
                    new NameFileFilter(oldName),
                    DirectoryFileFilter.DIRECTORY);

            for (File tmpFile : files
                    ) {
                String path = tmpFile.getAbsolutePath();
                System.out.println(path);
                String suiteID = StringUtils.substringBetween(path, "Files\\", "\\");
                System.out.println(suiteID);

                tmpFile.delete();

                UserDocument tmpSuite = userDocumentService.findById(Integer.parseInt(suiteID));
                System.out.println(tmpSuite);

                dirSuite = new File(LOC + tmpSuite.getId() + "/");
                FileUtils.deleteDirectory(dirSuite);

                String tmpContent = new String(tmpSuite.getContent());
                String strOld = "<tr><td><a href=\""+oldName+"\">"+FilenameUtils.removeExtension(oldName)+"</a></td></tr>";
                String strNew = "<tr><td><a href=\""+tmpName+"\">"+FilenameUtils.removeExtension(tmpName)+"</a></td></tr>";
                tmpContent = tmpContent.replaceAll(strOld,strNew);

                System.out.println(tmpContent);
                tmpSuite.setContent(tmpContent.getBytes());

                userDocumentService.updateDocument(tmpSuite);
                System.out.println(new String(tmpSuite.getContent()));
                User user = userService.findById(USERID);
                Browser browser = browserService.findByName(user.getBrowser());

                Quartz.scheduleJob(tmpSuite, browser);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("sdfsdf");


                createSuiteDirectory(tmpSuite);
            }
        }

        File f3 = new File(LOC + document.getId() + "/" + tmpName);
        f3.createNewFile();
        FileCopyUtils.copy(document.getContent(), f3);



        return tmpName;
    }

    private String updateSuite(UserDocument document, FileChange file) throws IOException {

        String tmpName = file.getName();
        if (document.getType().equals("text/html")) {
            tmpName = file.getName() + ".html";
        }
        boolean check = false;
        if (!document.getName().equals(tmpName)) {
            File dir = new File(LOC + document.getId() + "/");

            if (dir.exists()) {

                FileUtils.deleteDirectory(dir);
            }

            check = true;

        }

        document.setName(tmpName);
        document.setDescription(file.getDescription());
        document.setContent(file.getContent().getBytes());
        document.setCron(file.getCron());
        System.out.println(document.toString());
        userDocumentService.updateDocument(document);

        if (check) {
            createSuiteDirectory(document);
        }
        return tmpName;
    }


    private void createScriptDirectory(UserDocument document) throws IOException {
        File dir = new File(LOC + document.getId());

            System.out.println("ddddddddddddddd");
            dir.mkdir();
            File file = new File(LOC + document.getId() + "/" + document.getName());
            file.createNewFile();
            FileCopyUtils.copy(document.getContent(), file);

    }

    private void createSuiteDirectory(UserDocument document) throws IOException {
        File dir = new File(LOC + document.getId());
        if (!dir.exists()) {
            dir.mkdir();
            File file = new File(LOC + document.getId() + "/" + document.getName());
            file.createNewFile();
            FileCopyUtils.copy(document.getContent(), file);

            getScriptsinSuite(document);

        }
    }

    private void getScriptsinSuite(UserDocument document) throws IOException {
        String[] list;
        String content = new String(document.getContent());

        list = StringUtils.substringsBetween(content, "<a href=\"", "\">");
        Set<String> set = new HashSet<String>(Arrays.asList(list));
        System.out.println(Arrays.toString(list));
        ArrayList<String> list2 = new ArrayList<String>();
        for (String s: set
             ) {
            list2.add(s);
        }

        for (String scriptName : list2
                ) {
            UserDocument doc = userDocumentService.findByName(scriptName);
            System.out.println(doc);
            File f = new File(LOC + document.getId() + "/" + doc.getName());
            f.createNewFile();
            FileCopyUtils.copy(doc.getContent(), f);
        }
    }
}
