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

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.ResourceLocalService;
import com.liferay.portal.service.UserGroupRoleLocalService;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portlet.messageboards.service.MBMessageLocalService;
import junit.framework.Assert;
import org.apache.commons.collections.BeanMap;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import se.vgregion.portal.innovationsslussen.domain.BariumResponse;
import se.vgregion.portal.innovationsslussen.domain.IdeaContentType;
import se.vgregion.portal.innovationsslussen.domain.IdeaStatus;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.service.barium.BariumService;
import se.vgregion.service.innovationsslussen.exception.CreateIdeaException;
import se.vgregion.service.innovationsslussen.exception.RemoveIdeaException;
import se.vgregion.service.innovationsslussen.idea.settings.IdeaSettingsService;
import se.vgregion.service.innovationsslussen.repository.idea.IdeaRepository;
import se.vgregion.service.innovationsslussen.repository.ideafile.IdeaFileRepository;
import se.vgregion.service.innovationsslussen.repository.ideauserfavorite.IdeaUserFavoriteRepository;
import se.vgregion.service.innovationsslussen.repository.ideauserlike.IdeaUserLikeRepository;

/**
 * @author Patrik Bergström
 * @author Simon Göransson - simon.goransson@monator.com - vgrid: simgo3
 */
public class IdeaServiceImplTest {


    private IdeaServiceImpl service;
    private IdeaRepository ideaRepository;
    private IdeaFileRepository ideaFileRepository;
    private IdeaUserLikeRepository ideaUserLikeRepository;
    private IdeaUserFavoriteRepository ideaUserFavoriteRepository;
    private BariumService bariumService;
    private IdeaSettingsService ideaSettingsService;
    private MBMessageLocalService mbMessageLocalService;
    private UserLocalService userLocalService;
    private UserGroupRoleLocalService userGroupRoleLocalService;
    private ResourceLocalService resourceLocalService;

    @Before
    public void setUp() {
        ideaRepository = Mockito.mock(IdeaRepository.class);
        ideaFileRepository = Mockito.mock(IdeaFileRepository.class);
        ideaUserLikeRepository = Mockito.mock(IdeaUserLikeRepository.class);
        ideaUserFavoriteRepository = Mockito.mock(IdeaUserFavoriteRepository.class);
        bariumService = Mockito.mock(BariumService.class);
        ideaSettingsService = Mockito.mock(IdeaSettingsService.class);
        mbMessageLocalService = Mockito.mock(MBMessageLocalService.class);
        userLocalService = Mockito.mock(UserLocalService.class);
        userGroupRoleLocalService = Mockito.mock(UserGroupRoleLocalService.class);
        resourceLocalService = Mockito.mock(ResourceLocalService.class);

        Idea idea = new Idea();
        Mockito.when(ideaRepository.findIdeaByUrlTitle(Mockito.anyString())).thenReturn(idea);

        service = new IdeaServiceImpl(ideaRepository, ideaFileRepository,
                ideaUserLikeRepository, ideaUserFavoriteRepository,
                bariumService, ideaSettingsService,
                mbMessageLocalService, userLocalService,
                userGroupRoleLocalService, resourceLocalService);
    }

    @Test
    public void addFavorite() throws NoSuchFieldException, IllegalAccessException {
        service.addFavorite(1l, 2l, 3l, "foo");
    }

    @Test
    public void addLike() throws NoSuchFieldException, IllegalAccessException {
        service.addLike(1l, 2l, 3l, "foo");
    }

    @Test
    public void addIdea() throws NoSuchFieldException, IllegalAccessException, CreateIdeaException {
        final IdeaContent pub = new IdeaContent(1l,2l,3l);
        pub.setType(IdeaContentType.IDEA_CONTENT_TYPE_PUBLIC);
        pub.setUserId(100l);
        pub.setId(400l);

        final IdeaContent pri = new IdeaContent(4l, 5l, 6l);
        pri.setType(IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE);
        pri.setUserId(200l);
        pri.setId(300l);

        Idea newItem = new Idea(){
            @Override
            public IdeaContent getIdeaContentPublic() {
                return pub;
            }

            @Override
            public IdeaContent getIdeaContentPrivate() {
                return pri;
            }
        };

        newItem.setUrlTitle("urlTitle");
        newItem.setTitle("title");
        //BariumResponse bariumResponse = bariumService.createIdea(newItem);
        BariumResponse bariumResponse = Mockito.mock(BariumResponse.class);
        Mockito.when(bariumService.createIdea(Mockito.any(Idea.class))).thenReturn(bariumResponse);
        Mockito.when(bariumResponse.getSuccess()).thenReturn(true);

        //idea = ideaRepository.persist(idea);
        Mockito.when(ideaRepository.persist(newItem)).thenReturn(newItem);

        //newItem.setIdeaContentsPublic(new IdeaContent());
        //newItem.setIdeaContentPrivate(new IdeaContent());


        newItem.getIdeaContents().add(pri);
        newItem.getIdeaContents().add(pub);

        try {
            Idea bar = service.addIdea(newItem, "bar");
        } catch (PortalException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SystemException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test @Ignore
    public void remove() throws RemoveIdeaException {

        Idea idea = new Idea() {
            @Override
            public IdeaContent getIdeaContentPrivate() {
                IdeaContent r = new IdeaContent();
                r.setId(10001l);
                return r;
            }

            @Override
            public IdeaContent getIdeaContentPublic() {
                IdeaContent r = new IdeaContent();
                r.setId(10001l);
                return r;
            }
        };
        Mockito.when(ideaRepository.find("foo")).thenReturn(idea);
        //BariumResponse bariumResponse = bariumService.deleteBariumIdea(idea.getId());
        BariumResponse bariumResponse = Mockito.mock(BariumResponse.class);
        Mockito.when(bariumResponse.getSuccess()).thenReturn(true);
        Mockito.when(bariumService.deleteBariumIdea(Mockito.anyString())).thenReturn(bariumResponse);

        service.remove("foo");
    }

    @Test
    public void isIdeasTheSame() {
        Idea i1 = new Idea();
        Idea i2 = new Idea();

        i1.setId("1");
        i2.setId("1");

        i1.setStatus(IdeaStatus.PUBLIC_IDEA);
        i2.setStatus(IdeaStatus.PUBLIC_IDEA);

        boolean r = service.isIdeasTheSame(i1, i2);
        Assert.assertTrue(r);

        initDefaultStringValues(i1);
        r = service.isIdeasTheSame(i1, i2);
        Assert.assertFalse(r);

        initDefaultStringValues(i2);
        r = service.isIdeasTheSame(i1, i2);
        Assert.assertTrue(r);

        IdeaContent priv1 = new IdeaContent();
        priv1.setId(1l);
        priv1.setType(IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE);
        i1.getIdeaContents().add(priv1);

        IdeaContent priv2 = new IdeaContent();
        priv2.setId(1l);
        priv2.setType(IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE);
        i2.getIdeaContents().add(priv2);

        r = service.isIdeasTheSame(i1, i2);
        Assert.assertTrue(r);

        i1.setBariumUrl("foo");
        r = service.isIdeasTheSame(i1, i2);
        Assert.assertFalse(r);
    }

    private void initDefaultStringValues(Object o) {
        BeanMap bm = new BeanMap(o);
        for (Object key: bm.keySet()) {
            String name = (String) key;
            if (bm.getWriteMethod(name) != null) {
                if (bm.getType(name).equals(String.class)) {
                    bm.put(name, name);
                }
            }
        }
    }




}
