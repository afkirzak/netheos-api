package com.netheos.api.service;

import java.util.List;

import com.netheos.api.model.FAQ;

public interface FAQService {

	public FAQ save(FAQ faq);
	public List<FAQ> list();
	public List<String> search(String text);
}
