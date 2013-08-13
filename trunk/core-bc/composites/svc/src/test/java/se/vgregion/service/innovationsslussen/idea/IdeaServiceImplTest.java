package se.vgregion.service.innovationsslussen.idea;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import se.vgregion.portal.innovationsslussen.domain.IdeaContentType;
import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.service.barium.BariumService;
import se.vgregion.service.innovationsslussen.idea.settings.IdeaSettingsService;
import se.vgregion.service.innovationsslussen.repository.idea.IdeaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author Patrik Bergström
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-jpa-infrastructure-configuration.xml", "classpath:task-context.xml",
        "classpath:service-context-test.xml"})
public class IdeaServiceImplTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JpaTransactionManager jpaTransactionManager;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private IdeaRepository ideaRepository;

    @Autowired
    private BariumService bariumService;

    @Autowired
    private IdeaServiceImpl ideaService;

    @Before
    public void setup() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        // Persist two ideas. This is done once for all test methods
        Idea idea1 = entityManager.find(Idea.class, "bariumId1");
        if (idea1 == null) {
            idea1 = new Idea(1, 1, 1);
            idea1.setId("bariumId1");

            IdeaContent ideaContentPrivate1 = new IdeaContent(1, 1, 1);
            ideaContentPrivate1.setDescription("The description1");
            ideaContentPrivate1.setIdea(idea1);
            ideaContentPrivate1.setType(IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE);

            idea1.getIdeaContents().add(ideaContentPrivate1);

            idea1.setUrlTitle("stora-titeln");
            idea1.setTitle("Stora titeln");
            idea1.setIdeaSiteLink("http://example.com/asldkfj/url-title");

            Idea idea2 = new Idea(1, 1, 1);
            idea2.setId("bariumId2");
            idea2.setTitle("Lilla titeln");
            
            // Mock seems not to work
//            IdeaSettingsService ideaSettingsService = Mockito.mock(IdeaSettingsService.class);
//            
//            when(ideaSettingsService.getSetting(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong())).thenReturn("");
//            
//            Field ideaSettingsServiceField = IdeaServiceImpl.class.getDeclaredField("ideaSettingsService");
//            
//            ideaSettingsServiceField.setAccessible(true);
//            ideaSettingsServiceField.set(ideaService, ideaSettingsService);

            TransactionStatus transaction = jpaTransactionManager.getTransaction(new DefaultTransactionAttribute());
            entityManager.persist(idea1);
            entityManager.persist(idea2);
            jpaTransactionManager.commit(transaction);
        }

        IdeaObjectFields ideaObjectFields = new IdeaObjectFields();
        ideaObjectFields.setIde("The description1");
        ideaObjectFields.setInstanceName("Stora titeln");

        when(bariumService.getBariumIdea("bariumId1")).thenReturn(ideaObjectFields);
    }

    @Test
    @DirtiesContext
    public void testInit() throws Exception {

        Idea bariumId1 = entityManager.find(Idea.class, "bariumId1");

        // Given
        IdeaObjectFields ideaObjectFields = new IdeaObjectFields();
        ideaObjectFields.setIde("The description has changed");
        ideaObjectFields.setInstanceName("Stora titeln");

        when(bariumService.getBariumIdea("bariumId1")).thenReturn(ideaObjectFields);

        // When
        ideaService.init();

        // Then (see that the description has been updated compared to what the setup() method would result in)
        Idea idea = ideaService.findIdeaByUrlTitle("stora-titeln");

        assertEquals("The description has changed", idea.getIdeaContentPrivate().getDescription());
    }

    @Test
    @DirtiesContext
    public void updateAllIdeasFromBarium() throws Exception {

        // Given
        IdeaObjectFields ideaObjectFields = new IdeaObjectFields();
        ideaObjectFields.setIde("The description has changed");
        ideaObjectFields.setInstanceName("Stora titeln");

        when(bariumService.getBariumIdea("bariumId1")).thenReturn(ideaObjectFields);

        // When
        ideaService.updateAllIdeasFromBarium();

        // Then (see that the description has been updated compared to what the setup() method would result in)
        Idea idea = ideaService.findIdeaByUrlTitle("stora-titeln");

        assertEquals("The description has changed", idea.getIdeaContentPrivate().getDescription());
    }

    @Test
    @DirtiesContext
    public void replaceLastPart() throws Exception {
        String result = ideaService.replaceLastPart("http://asldkfj.se/aefkjkl/-/skaldjf/toBeReplaced", "newString");

        assertEquals("http://asldkfj.se/aefkjkl/-/skaldjf/newString", result);
    }

    @Test
    @DirtiesContext
    public void testDateOnIdeas() throws Exception {
        List<Idea> ideasByGroupId = ideaService.findIdeasByGroupId(1, 1);

        for (Idea idea : ideasByGroupId) {
            System.out.println(idea.getTitle() + " - " + idea.getCreated());
        }
    }

    @Test
    public void testFindIdeaByUrlTitle() throws Exception {
        Idea ideaByUrlTitle = ideaService.findIdeaByUrlTitle("stora-titeln");

        assertNotNull(ideaByUrlTitle);
    }

}
