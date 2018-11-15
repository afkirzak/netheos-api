package com.netheos.api.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

import com.netheos.api.model.FAQ;

public interface FAQRepository extends ElasticsearchCrudRepository<FAQ, Long> {

}
