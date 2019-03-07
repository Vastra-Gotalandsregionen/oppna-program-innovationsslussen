/**
 * Copyright 2010 Västra Götalandsregionen
 * <p>
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of version 2.1 of the GNU Lesser General Public
 * License as published by the Free Software Foundation.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307  USA
 */

package se.vgregion.service.innovationsslussen.validator;

import junit.framework.Assert;
import org.junit.Test;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;
import se.vgregion.portal.innovationsslussen.domain.IdeaContentType;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaPerson;

import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Patrik Bergström
 */
public class IdeaValidatorTest {

    private IdeaValidator ideaValidator = new IdeaValidator();

    @Test
    public void testIsValidEmail() throws Exception {
        assertTrue(ideaValidator.isValidEmail("arne.anka@ankeborg.com"));
        assertFalse(ideaValidator.isValidEmail("arne.anka(at)ankeborg.com"));
        assertTrue(ideaValidator.isValidEmail("arne.anka@ankeborgcom"));
        assertTrue(ideaValidator.isValidEmail("arneanka@ankeborg.com"));

        assertTrue(ideaValidator.isValidEmail("\"Arne Anka\"@ankeborg.com"));
        assertTrue(ideaValidator.isValidEmail("Arne\\@Anka@ankeborg.com"));
        assertTrue(ideaValidator.isValidEmail("$Arne=Anka@ankeborg.com"));
        assertTrue(ideaValidator.isValidEmail("_A!r%ne.Anka@ankeborg.com"));
        assertFalse(ideaValidator.isValidEmail("Arne@Anka@ankeborg.com"));
    }

    @Test
    public void validate() throws Exception {
        HashMap map = new HashMap();
        Idea idea = new Idea();
        IdeaPerson ideaPerson = new IdeaPerson();
        ideaPerson.setEmail("akjhkljhfgtdfhgdfhgfjhgfjhgfjhgfjhgfjhgfjhgfrsgrsgsdfj@@skladjflakj.com"); // Note double-@
        idea.addIdeaPerson(ideaPerson);
        IdeaContent ideaContent = new IdeaContent();
        ideaContent.setType(IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE);
        idea.getIdeaContents().add(ideaContent);
        MapBindingResult result = new MapBindingResult(map, "aName");
        ideaValidator.validate(idea, result);

        String errorsConcatenated = "";
        List<ObjectError> allErrors = result.getAllErrors();
        for (ObjectError error : allErrors) {
            errorsConcatenated += error.toString();
        }

        Assert.assertTrue(errorsConcatenated.contains(IdeaValidator.TITLE_MANDATORY));
        Assert.assertTrue(errorsConcatenated.contains(IdeaValidator.DESCRIPTION_MANDATORY));
        Assert.assertTrue(errorsConcatenated.contains(IdeaValidator.SOLVES_PROBLEM_MANDATORY));
        Assert.assertTrue(errorsConcatenated.contains(IdeaValidator.NAME_MANDATORY));
        Assert.assertTrue(errorsConcatenated.contains(IdeaValidator.INVALID_EMAIL));
        Assert.assertTrue(errorsConcatenated.contains(IdeaValidator.WANTS_HELP_WITH_MANDATORY));

        assertEquals(16, allErrors.size());
    }

    @Test
    public void validateOk() throws Exception {
        HashMap map = new HashMap();

        Idea idea = new Idea();
        idea.setTitle("title");

        IdeaPerson ideaPerson = new IdeaPerson();
        ideaPerson.setName("name");
        ideaPerson.setEmail("valid@email.com");
        ideaPerson.setPhone("0704443331");
        ideaPerson.setPhoneMobile("0704443331");
        ideaPerson.setAdministrativeUnit("enhet");
        ideaPerson.setJobPosition("myJob");
        idea.addIdeaPerson(ideaPerson);

        IdeaContent ideaContent = new IdeaContent();
        ideaContent.setType(IdeaContentType.IDEA_CONTENT_TYPE_PRIVATE);
        ideaContent.setDescription("sadfasdf");
        ideaContent.setSolvesProblem("saldkfj");
        ideaContent.setIdeaTested("tested");
        ideaContent.setWantsHelpWith("helpme");

        idea.getIdeaContents().add(ideaContent);
        MapBindingResult result = new MapBindingResult(map, "aName");
        ideaValidator.validate(idea, result);

        String errorsConcatenated = "";
        List<ObjectError> allErrors = result.getAllErrors();
        for (ObjectError error : allErrors) {
            errorsConcatenated += error.toString();
        }

        Assert.assertFalse(errorsConcatenated.contains(IdeaValidator.TITLE_MANDATORY));
        Assert.assertFalse(errorsConcatenated.contains(IdeaValidator.DESCRIPTION_MANDATORY));
        Assert.assertFalse(errorsConcatenated.contains(IdeaValidator.SOLVES_PROBLEM_MANDATORY));
        Assert.assertFalse(errorsConcatenated.contains(IdeaValidator.NAME_MANDATORY));
        Assert.assertFalse(errorsConcatenated.contains(IdeaValidator.INVALID_EMAIL));

        assertEquals(0, allErrors.size());
    }

    @Test
    public void containtsOnlyNumbers() {
        boolean b = ideaValidator.containsOnlyNumbers("012345");
        Assert.assertTrue(b);

        b = !ideaValidator.containsOnlyNumbers("abcdefg");
        Assert.assertTrue(b);
    }
}
