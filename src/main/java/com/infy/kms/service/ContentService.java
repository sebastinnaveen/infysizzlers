package com.infy.kms.service;

import java.util.ArrayList;
import java.util.List;



import com.infy.kms.repository.Content;
import com.infy.kms.repository.ContentRepository;



//@Service

public class ContentService implements ContentServiceDAO {
	
	 private ContentRepository contentRepository;
	 
	//  @Autowired
	    public ContentService(ContentRepository contentRepository) {
	        this.contentRepository = contentRepository;
	       
	    }


	    
	    public List<Content> listAll() {
	        List<Content> contents = new ArrayList<>();
	      //  contentRepository.findAll().forEach(contents::add); 
	        return contents;
	    }
}
