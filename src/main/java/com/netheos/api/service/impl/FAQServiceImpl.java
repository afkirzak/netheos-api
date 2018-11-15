package com.netheos.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import com.netheos.api.config.ElasticsearchConfig;
import com.netheos.api.model.FAQ;
import com.netheos.api.repository.FAQRepository;
import com.netheos.api.service.FAQService;

@Service
public class FAQServiceImpl implements FAQService {

	private final static Logger LOG = LoggerFactory.getLogger(FAQServiceImpl.class);

	@Autowired
	private FAQRepository faqRepository;

	@Autowired
	ElasticsearchConfig esConfig;

	@Override
	public FAQ save(FAQ faq) {
		
		faq.setId(System.currentTimeMillis());
		return faqRepository.save(faq);
	}

	@Override
	public List<FAQ> list() {

		Iterable<FAQ> itFaq = faqRepository.findAll();
		List<FAQ> list = new ArrayList<FAQ>();
		itFaq.forEach(list::add);
		return list;
	}

	@Override
	public List<String> search(String text) {

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.multiMatchQuery(text)
				.field("question").field("answer").type(MultiMatchQueryBuilder.Type.BEST_FIELDS)).build();

		List<String> answersList = null;

		try {
			answersList = esConfig.elasticsearchTemplate().queryForList(searchQuery, FAQ.class).stream().map(FAQ::getAnswer)
					.collect(Collectors.toList());
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}

		return answersList;
	}
}
