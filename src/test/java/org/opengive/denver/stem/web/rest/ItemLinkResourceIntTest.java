package org.opengive.denver.stem.web.rest;

import org.opengive.denver.stem.OpenGiveApplication;

import org.opengive.denver.stem.domain.ItemLink;
import org.opengive.denver.stem.repository.ItemLinkRepository;
import org.opengive.denver.stem.service.ItemLinkService;
import org.opengive.denver.stem.repository.search.ItemLinkSearchRepository;
import org.opengive.denver.stem.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ItemLinkResource REST controller.
 *
 * @see ItemLinkResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenGiveApplication.class)
public class ItemLinkResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_THUMBNAIL_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ITEM_URL = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_URL = "BBBBBBBBBB";

    @Autowired
    private ItemLinkRepository itemLinkRepository;

    @Autowired
    private ItemLinkService itemLinkService;

    @Autowired
    private ItemLinkSearchRepository itemLinkSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restItemLinkMockMvc;

    private ItemLink itemLink;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItemLinkResource itemLinkResource = new ItemLinkResource(itemLinkService);
        this.restItemLinkMockMvc = MockMvcBuilders.standaloneSetup(itemLinkResource)
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
    public static ItemLink createEntity(EntityManager em) {
        ItemLink itemLink = new ItemLink()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .thumbnailImageUrl(DEFAULT_THUMBNAIL_IMAGE_URL)
            .itemUrl(DEFAULT_ITEM_URL);
        return itemLink;
    }

    @Before
    public void initTest() {
        itemLinkSearchRepository.deleteAll();
        itemLink = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemLink() throws Exception {
        int databaseSizeBeforeCreate = itemLinkRepository.findAll().size();

        // Create the ItemLink
        restItemLinkMockMvc.perform(post("/api/item-links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemLink)))
            .andExpect(status().isCreated());

        // Validate the ItemLink in the database
        List<ItemLink> itemLinkList = itemLinkRepository.findAll();
        assertThat(itemLinkList).hasSize(databaseSizeBeforeCreate + 1);
        ItemLink testItemLink = itemLinkList.get(itemLinkList.size() - 1);
        assertThat(testItemLink.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItemLink.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testItemLink.getThumbnailImageUrl()).isEqualTo(DEFAULT_THUMBNAIL_IMAGE_URL);
        assertThat(testItemLink.getItemUrl()).isEqualTo(DEFAULT_ITEM_URL);

        // Validate the ItemLink in Elasticsearch
        ItemLink itemLinkEs = itemLinkSearchRepository.findOne(testItemLink.getId());
        assertThat(itemLinkEs).isEqualToComparingFieldByField(testItemLink);
    }

    @Test
    @Transactional
    public void createItemLinkWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemLinkRepository.findAll().size();

        // Create the ItemLink with an existing ID
        itemLink.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemLinkMockMvc.perform(post("/api/item-links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemLink)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ItemLink> itemLinkList = itemLinkRepository.findAll();
        assertThat(itemLinkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemLinkRepository.findAll().size();
        // set the field null
        itemLink.setName(null);

        // Create the ItemLink, which fails.

        restItemLinkMockMvc.perform(post("/api/item-links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemLink)))
            .andExpect(status().isBadRequest());

        List<ItemLink> itemLinkList = itemLinkRepository.findAll();
        assertThat(itemLinkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkThumbnailImageUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemLinkRepository.findAll().size();
        // set the field null
        itemLink.setThumbnailImageUrl(null);

        // Create the ItemLink, which fails.

        restItemLinkMockMvc.perform(post("/api/item-links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemLink)))
            .andExpect(status().isBadRequest());

        List<ItemLink> itemLinkList = itemLinkRepository.findAll();
        assertThat(itemLinkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkItemUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemLinkRepository.findAll().size();
        // set the field null
        itemLink.setItemUrl(null);

        // Create the ItemLink, which fails.

        restItemLinkMockMvc.perform(post("/api/item-links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemLink)))
            .andExpect(status().isBadRequest());

        List<ItemLink> itemLinkList = itemLinkRepository.findAll();
        assertThat(itemLinkList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemLinks() throws Exception {
        // Initialize the database
        itemLinkRepository.saveAndFlush(itemLink);

        // Get all the itemLinkList
        restItemLinkMockMvc.perform(get("/api/item-links?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemLink.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].thumbnailImageUrl").value(hasItem(DEFAULT_THUMBNAIL_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].itemUrl").value(hasItem(DEFAULT_ITEM_URL.toString())));
    }

    @Test
    @Transactional
    public void getItemLink() throws Exception {
        // Initialize the database
        itemLinkRepository.saveAndFlush(itemLink);

        // Get the itemLink
        restItemLinkMockMvc.perform(get("/api/item-links/{id}", itemLink.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itemLink.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.thumbnailImageUrl").value(DEFAULT_THUMBNAIL_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.itemUrl").value(DEFAULT_ITEM_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItemLink() throws Exception {
        // Get the itemLink
        restItemLinkMockMvc.perform(get("/api/item-links/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemLink() throws Exception {
        // Initialize the database
        itemLinkService.save(itemLink);

        int databaseSizeBeforeUpdate = itemLinkRepository.findAll().size();

        // Update the itemLink
        ItemLink updatedItemLink = itemLinkRepository.findOne(itemLink.getId());
        updatedItemLink
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .thumbnailImageUrl(UPDATED_THUMBNAIL_IMAGE_URL)
            .itemUrl(UPDATED_ITEM_URL);

        restItemLinkMockMvc.perform(put("/api/item-links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItemLink)))
            .andExpect(status().isOk());

        // Validate the ItemLink in the database
        List<ItemLink> itemLinkList = itemLinkRepository.findAll();
        assertThat(itemLinkList).hasSize(databaseSizeBeforeUpdate);
        ItemLink testItemLink = itemLinkList.get(itemLinkList.size() - 1);
        assertThat(testItemLink.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemLink.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItemLink.getThumbnailImageUrl()).isEqualTo(UPDATED_THUMBNAIL_IMAGE_URL);
        assertThat(testItemLink.getItemUrl()).isEqualTo(UPDATED_ITEM_URL);

        // Validate the ItemLink in Elasticsearch
        ItemLink itemLinkEs = itemLinkSearchRepository.findOne(testItemLink.getId());
        assertThat(itemLinkEs).isEqualToComparingFieldByField(testItemLink);
    }

    @Test
    @Transactional
    public void updateNonExistingItemLink() throws Exception {
        int databaseSizeBeforeUpdate = itemLinkRepository.findAll().size();

        // Create the ItemLink

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restItemLinkMockMvc.perform(put("/api/item-links")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemLink)))
            .andExpect(status().isCreated());

        // Validate the ItemLink in the database
        List<ItemLink> itemLinkList = itemLinkRepository.findAll();
        assertThat(itemLinkList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteItemLink() throws Exception {
        // Initialize the database
        itemLinkService.save(itemLink);

        int databaseSizeBeforeDelete = itemLinkRepository.findAll().size();

        // Get the itemLink
        restItemLinkMockMvc.perform(delete("/api/item-links/{id}", itemLink.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean itemLinkExistsInEs = itemLinkSearchRepository.exists(itemLink.getId());
        assertThat(itemLinkExistsInEs).isFalse();

        // Validate the database is empty
        List<ItemLink> itemLinkList = itemLinkRepository.findAll();
        assertThat(itemLinkList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchItemLink() throws Exception {
        // Initialize the database
        itemLinkService.save(itemLink);

        // Search the itemLink
        restItemLinkMockMvc.perform(get("/api/_search/item-links?query=id:" + itemLink.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemLink.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].thumbnailImageUrl").value(hasItem(DEFAULT_THUMBNAIL_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].itemUrl").value(hasItem(DEFAULT_ITEM_URL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemLink.class);
    }
}
