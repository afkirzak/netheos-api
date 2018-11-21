package com.netheos.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netheos.api.model.FAQ;
import com.netheos.api.service.FAQService;

@RestController
public class APIController {

	@Autowired
    private FAQService faqService;
	
	@RequestMapping(value = "/private/faq", method = RequestMethod.POST)
    public FAQ save(@RequestBody FAQ faq) {
        return faqService.save(faq);
    }
	
	@RequestMapping(value = "/private/faq", method = RequestMethod.GET)
    public List<FAQ> list() {
        return faqService.list();
    }
	
	@RequestMapping(value = "/public/faq", method = RequestMethod.GET)
    public List<String> search(@RequestParam("search") String txt) {
        return faqService.search(txt);
    }
}
