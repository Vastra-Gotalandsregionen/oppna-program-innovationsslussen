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

package se.vgregion.service.innovationsslussen.validator;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;

@Component
public class IdeaValidator implements Validator {


    static final String TITLE_MANDATORY = "Titel är obligatorisk";
    static final String TITLE_CANNOT_HAVE_NUMBERS = "Titel får inte innehålla enbart siffror.";
    static final String DESCRIPTION_MANDATORY = "Beskrivning är obligatorisk";
    static final String SOLVES_PROBLEM_MANDATORY = "Löser behov / problem är obligatoriskt";
    static final String WANTS_HELP_WITH_MANDATORY = "Vad behöver du hjälp med är obligatoriskt";
    static final String NAME_MANDATORY = "Namn är obligatoriskt";
    static final String EMAIL_MANDATORY = "E-post är obligatorisk";
    static final String INVALID_EMAIL = "Angiven e-post är ogiltig";
    static final String PHONE_MANDATORY = "Telefon är obligatoriskt";
    static final String INVALID_PHONE = "Ogiltigt telefonnummer";
    static final String MAX_LENGTH_TEXT = " har för många tecken";
    static final String MANDATORY = " är obligatoriskt";


    static final int MAX_LENGTH_SMALL = 200;
    static final int MAX_LENGTH_MEDIUM = 800;
    static final int MAX_LENGTH_BIG = 2000;
    static final int MAX_LENGTH_GIANT = 40000;


    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^?[+]?[0-9]{8,14}");


    /* (non-Javadoc)
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Idea.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Idea idea = (Idea) target;

        validateName(idea, errors);
        validateEmail(idea, errors);
        validatePhone(idea, errors);
        validateLength(idea, errors);
        validateTitle(idea, errors);
        validateDescription(idea, errors);
        validateNeed(idea, errors);
        validateWantsHelpWith(idea, errors);
    }

    private void validateLength(Idea idea, Errors errors) {
        maxLengthValidation(idea.getTitle(), "title", "Title", errors, MAX_LENGTH_SMALL);
        maxLengthValidation(idea.getIdeaContentPrivate().getDescription(), "ideaContentPrivate.description", "Beskrivning", errors, MAX_LENGTH_BIG);
        maxLengthValidation(idea.getIdeaContentPrivate().getSolvesProblem(), "ideaContentPrivate.solvesProblem", "Löser behov / problem",errors, MAX_LENGTH_BIG);
        maxLengthValidation(idea.getIdeaContentPrivate().getWantsHelpWith(), "ideaContentPrivate.wantsHelpWith", "Vad behöver du hjälp med?", errors, MAX_LENGTH_BIG);
        maxLengthValidation(idea.getIdeaPerson().getName(), "ideaPerson.name", "Namn", errors, MAX_LENGTH_SMALL);
        maxLengthValidation(idea.getIdeaPerson().getEmail(), "ideaPerson.email", "E-post", errors, MAX_LENGTH_SMALL);
        maxLengthValidation(idea.getIdeaPerson().getPhone(), "ideaPerson.phone", "Telefon", errors, MAX_LENGTH_SMALL);
        maxLengthValidation(idea.getIdeaPerson().getPhoneMobile(), "ideaPerson.phoneMobile", "Mobiltelefon", errors, MAX_LENGTH_SMALL);
        maxLengthValidation(idea.getIdeaPerson().getAdministrativeUnit(), "ideaPerson.administrativeUnit", "Förvaltning", errors, MAX_LENGTH_SMALL);
        maxLengthValidation(idea.getIdeaPerson().getJobPosition(), "ideaPerson.jobPosition", "Yrkesroll", errors, MAX_LENGTH_SMALL);
        maxLengthValidation(idea.getIdeaContentPrivate().getAdditionalIdeaOriginators(), "ideaContentPrivate.additionalIdeaOriginators", "Fler idégivare", errors, MAX_LENGTH_MEDIUM);
    }

    private void maxLengthValidation(String feildToValidate, String feildName, String svFeildName,  Errors errors, int max) {

        if (feildToValidate == null){
            errors.rejectValue(feildName, feildName + ".not-null", svFeildName + MANDATORY);

        } else if (feildToValidate.length() > max){
            errors.rejectValue(feildName, feildName + ".max-length", svFeildName + MAX_LENGTH_TEXT);
        }
    }

    private void validateNeed(Idea idea, Errors errors) {
        String solvesProblem = idea.getIdeaContentPrivate().getSolvesProblem();

        if (solvesProblem == null || "".equals(solvesProblem)) {
            errors.rejectValue("ideaContentPrivate.solvesProblem", "ideaContentPrivate.solvesProblem",
                    SOLVES_PROBLEM_MANDATORY);
        }
    }

    private void validateWantsHelpWith(Idea idea, Errors errors) {
        String wantsHelpWith = idea.getIdeaContentPrivate().getWantsHelpWith();

        if (wantsHelpWith == null || "".equals(wantsHelpWith)) {
            errors.rejectValue("ideaContentPrivate.wantsHelpWith", "ideaContentPrivate.wantsHelpWith",
                    WANTS_HELP_WITH_MANDATORY);
        }
    }

    private void validateTitle(Idea idea, Errors errors) {

        String title = idea.getTitle();

        if (title == null) {
            errors.rejectValue("title", "title.not-null", TITLE_MANDATORY);
        } else if (title.equals("")) {
            errors.rejectValue("title", "title.not-empty", TITLE_MANDATORY);
        }

        if (containsOnlyNumbers(title)) {
            errors.rejectValue("title", "title.not-number", TITLE_CANNOT_HAVE_NUMBERS);
        }
    }

    boolean containsOnlyNumbers(String string) {
        if (string == null) {
            return false;
        }
        return string.matches("[0-9]+");
    }

    private void validateDescription(Idea idea, Errors errors) {

        String description = idea.getIdeaContentPrivate().getDescription();

        if (description == null) {
            errors.rejectValue("ideaContentPrivate.description", "description.not-null", DESCRIPTION_MANDATORY);
        } else if (description.equals("")) {
            errors.rejectValue("ideaContentPrivate.description", "description.not-empty", DESCRIPTION_MANDATORY);
        }
    }

    private void validateName(Idea idea, Errors errors) {

        String name = idea.getIdeaPerson().getName();

        if (name == null) {
            errors.rejectValue("ideaPerson.name", "name.not-null", NAME_MANDATORY);
        } else if (name.equals("")) {
            errors.rejectValue("ideaPerson.name", "name.not-empty", NAME_MANDATORY);
        }
    }

    private void validateEmail(Idea idea, Errors errors) {

        String email = idea.getIdeaPerson().getEmail();

        if (email == null) {
            errors.rejectValue("ideaPerson.email", "email.not-null", EMAIL_MANDATORY);
        } else if (email.equals("")) {
            errors.rejectValue("ideaPerson.email", "email.not-empty", EMAIL_MANDATORY);
        } else if (!isValidEmail(email)) {
            errors.rejectValue("ideaPerson.email", "email.not-empty", INVALID_EMAIL);
        }
    }

    private void validatePhone(Idea idea, Errors errors) {

        String phone = idea.getIdeaPerson().getPhone();

        if (phone == null) {
            errors.rejectValue("ideaPerson.phone", "phone.not-null", PHONE_MANDATORY);
        } else if (phone.equals("")) {
            errors.rejectValue("ideaPerson.phone", "phone.not-empty", PHONE_MANDATORY);
        } else if (!isPhoneNumber(phone)) {
            errors.rejectValue("ideaPerson.phone", "phone.format", INVALID_PHONE);
        }
    }

    // Validate according to RFC822
    boolean isValidEmail(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    private boolean isPhoneNumber(String value) {

        String phoneNumber = value.replace('.', ' ').replace('-', ' ').replace('(', ' ').replace(')', ' ')
                .replaceAll("\\W", "");

        return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }

}
