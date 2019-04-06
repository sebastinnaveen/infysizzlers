package com.infy.kms.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.infy.kms.service.ContentService;
import com.infy.kms.service.ContentServiceDAO;
import com.infy.kms.storage.StorageService;

@Controller
public class KmsController {
	 private final StorageService storageService;
//	@Autowired
//	 private ContentServiceDAO contentServiceDAO;
	 @GetMapping("/login")
	    public String login() {
	        return "login";
	    }
	 
	 @PostMapping("/kmsportal")
	    public String kmsportal(Model model) {
		 
		// model.addAttribute("contents", contentServiceDAO.listAll());
		// model.addAttribute("users",customerDAO.findAll());
		 
	        return "kms";
	    }
	 

	    @Autowired
	    public KmsController(StorageService storageService) {
	        this.storageService = storageService;
	    }

	    @GetMapping("/kmsauthoring")
	    public String listUploadedFiles(Model model) throws IOException {

	        model.addAttribute("files", storageService.loadAll().map(
	                path -> MvcUriComponentsBuilder.fromMethodName(KmsController.class,
	                        "serveFile", path.getFileName().toString()).build().toString())
	                .collect(Collectors.toList()));

	        return "uploadForm";
	    }

	    @GetMapping("/files/{filename:.+}")
	    @ResponseBody
	    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

	        Resource file = storageService.loadAsResource(filename);
	        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
	                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	    }

	    @PostMapping("/")
	    public String handleFileUpload(@RequestParam("file") MultipartFile file,
	            RedirectAttributes redirectAttributes) {

	        storageService.store(file);
	        redirectAttributes.addFlashAttribute("message",
	                "You successfully uploaded " + file.getOriginalFilename() + "!");

	        return "redirect:/";
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<?> handleStorageFileNotFound(Exception exc) {
	        return ResponseEntity.notFound().build();
	    }
}
