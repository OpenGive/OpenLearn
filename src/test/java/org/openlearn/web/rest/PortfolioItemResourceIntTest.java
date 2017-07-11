package org.openlearn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.openlearn.OpenLearnApplication;
import org.openlearn.domain.ItemLink;
import org.openlearn.domain.Portfolio;
import org.openlearn.domain.PortfolioItem;
import org.openlearn.repository.PortfolioItemRepository;
import org.openlearn.service.PortfolioItemService;
import org.openlearn.web.rest.errors.ExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test class for the PortfolioItemResource REST controller.
 *
 * @see PortfolioItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenLearnApplication.class)
public class PortfolioItemResourceIntTest {

	@Autowired
	private PortfolioItemRepository portfolioItemRepository;

	@Autowired
	private PortfolioItemService portfolioItemService;

	@Autowired
	private MappingJackson2HttpMessageConverter jacksonMessageConverter;

	@Autowired
	private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

	@Autowired
	private ExceptionTranslator exceptionTranslator;

	@Autowired
	private EntityManager em;

	private MockMvc restPortfolioItemMockMvc;

	private PortfolioItem portfolioItem;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		final PortfolioItemResource portfolioItemResource = new PortfolioItemResource(portfolioItemService);
		restPortfolioItemMockMvc = MockMvcBuilders.standaloneSetup(portfolioItemResource)
				.setCustomArgumentResolvers(pageableArgumentResolver)
				.setControllerAdvice(exceptionTranslator)
				.setMessageConverters(jacksonMessageConverter).build();
	}

	/**
	 * Create an entity for this test.
	 *
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static PortfolioItem createEntity(final EntityManager em) {
		final PortfolioItem portfolioItem = new PortfolioItem();
		// Add required entity
		final Portfolio portfolio = PortfolioResourceIntTest.createEntity(em);
		em.persist(portfolio);
		em.flush();
		portfolioItem.setPortfolio(portfolio);
		// Add required entity
		final ItemLink resource = ItemLinkResourceIntTest.createEntity(em);
		em.persist(resource);
		em.flush();
		portfolioItem.getResources().add(resource);
		return portfolioItem;
	}

	@Before
	public void initTest() {
		portfolioItem = createEntity(em);
	}

	@Test
	@Transactional
	public void createPortfolioItem() throws Exception {
		final int databaseSizeBeforeCreate = portfolioItemRepository.findAll().size();

		// Create the PortfolioItem
		restPortfolioItemMockMvc.perform(post("/api/portfolio-items")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(portfolioItem)))
		.andExpect(status().isCreated());

		// Validate the PortfolioItem in the database
		final List<PortfolioItem> portfolioItemList = portfolioItemRepository.findAll();
		assertThat(portfolioItemList).hasSize(databaseSizeBeforeCreate + 1);
		final PortfolioItem testPortfolioItem = portfolioItemList.get(portfolioItemList.size() - 1);
	}

	@Test
	@Transactional
	public void createPortfolioItemWithExistingId() throws Exception {
		final int databaseSizeBeforeCreate = portfolioItemRepository.findAll().size();

		// Create the PortfolioItem with an existing ID
		portfolioItem.setId(1L);

		// An entity with an existing ID cannot be created, so this API call must fail
		restPortfolioItemMockMvc.perform(post("/api/portfolio-items")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(portfolioItem)))
		.andExpect(status().isBadRequest());

		// Validate the Alice in the database
		final List<PortfolioItem> portfolioItemList = portfolioItemRepository.findAll();
		assertThat(portfolioItemList).hasSize(databaseSizeBeforeCreate);
	}

	@Test
	@Transactional
	public void getAllPortfolioItems() throws Exception {
		// Initialize the database
		portfolioItemRepository.saveAndFlush(portfolioItem);

		// Get all the portfolioItemList
		restPortfolioItemMockMvc.perform(get("/api/portfolio-items?sort=id,desc"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.[*].id").value(hasItem(portfolioItem.getId().intValue())));
	}

	@Test
	@Transactional
	public void getPortfolioItem() throws Exception {
		// Initialize the database
		portfolioItemRepository.saveAndFlush(portfolioItem);

		// Get the portfolioItem
		restPortfolioItemMockMvc.perform(get("/api/portfolio-items/{id}", portfolioItem.getId()))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id").value(portfolioItem.getId().intValue()));
	}

	@Test
	@Transactional
	public void getNonExistingPortfolioItem() throws Exception {
		// Get the portfolioItem
		restPortfolioItemMockMvc.perform(get("/api/portfolio-items/{id}", Long.MAX_VALUE))
		.andExpect(status().isNotFound());
	}

	@Test
	@Transactional
	public void updatePortfolioItem() throws Exception {
		// Initialize the database
		portfolioItemService.save(portfolioItem);

		final int databaseSizeBeforeUpdate = portfolioItemRepository.findAll().size();

		// Update the portfolioItem
		final PortfolioItem updatedPortfolioItem = portfolioItemRepository.findOne(portfolioItem.getId());

		restPortfolioItemMockMvc.perform(put("/api/portfolio-items")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(updatedPortfolioItem)))
		.andExpect(status().isOk());

		// Validate the PortfolioItem in the database
		final List<PortfolioItem> portfolioItemList = portfolioItemRepository.findAll();
		assertThat(portfolioItemList).hasSize(databaseSizeBeforeUpdate);
		final PortfolioItem testPortfolioItem = portfolioItemList.get(portfolioItemList.size() - 1);
	}

	@Test
	@Transactional
	public void updateNonExistingPortfolioItem() throws Exception {
		final int databaseSizeBeforeUpdate = portfolioItemRepository.findAll().size();

		// Create the PortfolioItem

		// If the entity doesn't have an ID, it will be created instead of just being updated
		restPortfolioItemMockMvc.perform(put("/api/portfolio-items")
				.contentType(TestUtil.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(portfolioItem)))
		.andExpect(status().isCreated());

		// Validate the PortfolioItem in the database
		final List<PortfolioItem> portfolioItemList = portfolioItemRepository.findAll();
		assertThat(portfolioItemList).hasSize(databaseSizeBeforeUpdate + 1);
	}

	@Test
	@Transactional
	public void deletePortfolioItem() throws Exception {
		// Initialize the database
		portfolioItemService.save(portfolioItem);

		final int databaseSizeBeforeDelete = portfolioItemRepository.findAll().size();

		// Get the portfolioItem
		restPortfolioItemMockMvc.perform(delete("/api/portfolio-items/{id}", portfolioItem.getId())
				.accept(TestUtil.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk());

		// Validate the database is empty
		final List<PortfolioItem> portfolioItemList = portfolioItemRepository.findAll();
		assertThat(portfolioItemList).hasSize(databaseSizeBeforeDelete - 1);
	}

	@Test
	@Transactional
	public void equalsVerifier() throws Exception {
		TestUtil.equalsVerifier(PortfolioItem.class);
	}
}
