package com.netheos.api.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(indexName = "api", type = "faq")
public class FAQ {

	@Id
	@JsonIgnore
	private long id;

	private String question;
	private String answer;
	private List<String> tags;

	public FAQ() {

	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public boolean equals(Object o) {

		if (o == this) {
			return true;
		}
		if (!(o instanceof FAQ)) {
			return false;
		}

		FAQ c = (FAQ) o;
		return this.getQuestion().equals(c.getQuestion()) && this.getAnswer().equals(c.getAnswer())
				&& this.getTags().equals(c.getTags());
	}
}
