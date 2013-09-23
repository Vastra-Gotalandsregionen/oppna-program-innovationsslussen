package se.vgregion.service.innovationsslussen.validator;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;

@Component
public class IdeaValidator implements Validator {


    static final String TITLE_MANDATORY = "Titel är obligatorisk";
    static final String DESCRIPTION_MANDATORY = "Beskrivning är obligatorisk";
    static final String SOLVES_PROBLEM_MANDATORY = "Löser behov / problem är obligatoriskt";
    static final String NAME_MANDATORY = "Namn är obligatoriskt";
    static final String EMAIL_MANDATORY = "E-post är obligatorisk";
    static final String INVALID_EMAIL = "Angiven e-post är ogiltig";


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

        validateTitle(idea, errors);
        validateDescription(idea, errors);
        validateNeed(idea, errors);
        validateName(idea, errors);
        validateEmail(idea, errors);
    }

    private void validateNeed(Idea idea, Errors errors) {
        String solvesProblem = idea.getIdeaContentPrivate().getSolvesProblem();

        if (solvesProblem == null || "".equals(solvesProblem)) {
            errors.rejectValue("ideaContentPrivate.solvesProblem", "ideaContentPrivate.solvesProblem",
                    SOLVES_PROBLEM_MANDATORY);
        }
    }

    private void validateTitle(Idea idea, Errors errors) {

        String title = idea.getTitle();

        if (title == null) {
            errors.rejectValue("title", "title.not-null", TITLE_MANDATORY);
        } else if (title.equals("")) {
            errors.rejectValue("title", "title.not-empty", TITLE_MANDATORY);
        }
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

}
