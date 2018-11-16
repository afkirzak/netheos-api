package com.netheos.api;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netheos.api.model.FAQ;

import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NetheosApiApplicationTests {

	@Autowired
	private MockMvc mvc;

	private static EmbeddedElastic elasticsearchCluster;
	private static TransportClient client;
	private static FAQ faqExpected;

	@BeforeClass
	public static void setup() throws IOException, InterruptedException {

		elasticsearchCluster = EmbeddedElastic.builder().withElasticVersion("6.0.0")
				.withSetting(PopularProperties.TRANSPORT_TCP_PORT, 9300).withSetting(PopularProperties.HTTP_PORT, 9200)
				.withSetting("network.host", "localhost").withCleanInstallationDirectoryOnStop(true)
				.withEsJavaOpts("-Xms512m -Xmx1024m").withStartTimeout(10, TimeUnit.MINUTES).build().start();

		client = new PreBuiltTransportClient(Settings.EMPTY)
				.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));

		faqExpected = new FAQ();
		faqExpected.setQuestion("my question");
		faqExpected.setAnswer("my answer");
		faqExpected.setTags(Arrays.asList("tag1", "tag2"));
	}

	@Test
	public void userstory1_should_success() throws Exception {

		MvcResult mvcResult = mvc
				.perform(
						post("/private/faq/save").content(new ObjectMapper().writeValueAsString(faqExpected))
								.contentType(MediaType.APPLICATION_JSON).with(SecurityMockMvcRequestPostProcessors
										.user("afkir").password("password").roles("ADMIN")))
				.andExpect(status().isOk()).andReturn();

		FAQ faqMvcResult = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), FAQ.class);
		assertEquals(faqMvcResult, faqExpected);

		SearchResponse searchResponse = client.prepareSearch("api").setTypes("faq")
				.setQuery(QueryBuilders.matchAllQuery()).setSize(1).execute().actionGet();

		FAQ faqEs = new ObjectMapper().readValue(searchResponse.getHits().getAt(0).getSourceAsString(), FAQ.class);
		assertEquals(faqEs, faqExpected);
	}

	@Test
	public void userstory1_should_fail() throws Exception {

		mvc.perform(post("/private/faq/save").content(new ObjectMapper().writeValueAsString(faqExpected))
				.contentType(MediaType.APPLICATION_JSON)
				.with(SecurityMockMvcRequestPostProcessors.user("afkir").password("password").roles("USER")))
				.andExpect(status().is(403));
	}

	@Test
	public void userstory2_should_success() throws Exception {

		client.prepareIndex("api", "faq")
				.setSource(new ObjectMapper().writeValueAsString(faqExpected), XContentType.JSON).execute();

		MvcResult mvcResult = mvc
				.perform(get("/private/faq/list")
						.with(SecurityMockMvcRequestPostProcessors.user("afkir").password("password").roles("ADMIN")))
				.andExpect(status().isOk()).andReturn();

		List<FAQ> faqMvcResult = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<List<FAQ>>() {
				});

		assertEquals(faqMvcResult.get(0), faqExpected);
	}

	@Test
	public void userstory2_should_fail() throws Exception {

		mvc.perform(get("/private/faq/list")
				.with(SecurityMockMvcRequestPostProcessors.user("afkir").password("password").roles("USER")))
				.andExpect(status().is(403));
	}

	@Test
	public void userstory3_should_success() throws Exception {

		client.prepareIndex("api", "faq")
				.setSource(new ObjectMapper().writeValueAsString(faqExpected), XContentType.JSON).execute();

		MvcResult mvcResult = mvc.perform(get("/public/faq/search")
				.with(SecurityMockMvcRequestPostProcessors.user("afkir").password("password").roles("USER"))
				.param("search", "question")).andExpect(status().isOk()).andReturn();

		List<String> faqMvcResult = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
				new TypeReference<List<String>>() {
				});

		assertEquals(faqMvcResult.get(0), faqExpected.getAnswer());
	}

	@Test
	public void userstory3_should_fail() throws Exception {

		mvc.perform(get("/public/faq/search").param("search", "question")).andExpect(status().isUnauthorized());
	}

	@AfterClass
	public static void teardown() throws IOException, InterruptedException {
		elasticsearchCluster.stop();
		client.close();
	}
}
