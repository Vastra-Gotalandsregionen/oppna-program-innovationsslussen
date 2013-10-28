/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.service.innovationsslussen.idea;

import java.util.concurrent.Future;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import se.vgregion.portal.innovationsslussen.domain.IdeaContentType;
import se.vgregion.portal.innovationsslussen.domain.IdeaObjectFields;
import se.vgregion.portal.innovationsslussen.domain.IdeaStatus;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaFile;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaUserFavorite;
import se.vgregion.service.barium.BariumService;
import se.vgregion.service.innovationsslussen.exception.FileUploadException;
import se.vgregion.service.innovationsslussen.repository.idea.IdeaRepository;
import se.vgregion.service.innovationsslussen.repository.ideauserfavorite.IdeaUserFavoriteRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Patrik Bergström
 * @author Simon Göransson - simon.goransson@monator.com - vgrid: simgo3
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-jpa-infrastructure-configuration.xml", "classpath:task-context.xml",
"classpath:service-context-test.xml"})
public class IdeaServiceImplIT {

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
            idea1.setStatus(IdeaStatus.PUBLIC_IDEA);

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
            idea2.setStatus(IdeaStatus.PUBLIC_IDEA);

            // Mock seems not to work
            //            IdeaSettingsService ideaSettingsService = Mockito.mock(IdeaSettingsService.class);
            //
            //            when(ideaSettingsService.getSetting(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong())).thenReturn("");
            //
            //            Field ideaSettingsServiceField = IdeaServiceImpl.class.getDeclaredField("ideaSettingsService");
            //
            //            ideaSettingsServiceField.setAccessible(true);
            //            ideaSettingsServiceField.set(service, ideaSettingsService);

            TransactionStatus transaction = jpaTransactionManager.getTransaction(new DefaultTransactionAttribute());
            entityManager.persist(idea1);
            entityManager.persist(idea2);
            jpaTransactionManager.commit(transaction);
        }

        IdeaObjectFields ideaObjectFields = new IdeaObjectFields();
        ideaObjectFields.setIde("The description1");
        ideaObjectFields.setInstanceName("Stora titeln");

        when(bariumService.getBariumIdea("bariumId1")).thenReturn(ideaObjectFields);
        when(bariumService.asyncGetIdeaObjectFields(anyString())).thenCallRealMethod();
        when(bariumService.asyncGetIdeaPhaseFuture(anyString())).thenReturn(new AsyncResult<String>("1"));

        ReflectionTestUtils.setField(bariumService, "executor", Executors.newCachedThreadPool());
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
        Thread.sleep(500);

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
        List<Idea> ideasByGroupId = ideaService.findIdeasByGroupId(1, 1, IdeaStatus.PUBLIC_IDEA);

        for (Idea idea : ideasByGroupId) {
            System.out.println(idea.getTitle() + " - " + idea.getCreated());
        }
    }

    @Test
    public void testFindIdeaByUrlTitle() throws Exception {
        Idea ideaByUrlTitle = ideaService.findIdeaByUrlTitle("stora-titeln");

        assertNotNull(ideaByUrlTitle);
    }

    @Test
    public void testuploadFile() {

        Idea idea = ideaService.findIdeaByUrlTitle("stora-titeln");
        String ideaFileName = null;

        try {

            InputStream inputStream = getClass().getResourceAsStream("/text.txt");
            IdeaFile ideaFile = ideaService.uploadFile(idea, false, "text.txt", "txt", inputStream);

            Idea idea2 = ideaRepository.findIdeaByUrlTitle("stora-titeln");

            Set<IdeaFile> ideaFiles = idea2.getIdeaContentPrivate().getIdeaFiles();
            ideaFileName = ideaFiles.iterator().next().getName();


        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        assertEquals("text.txt", ideaFileName);
    }

}
