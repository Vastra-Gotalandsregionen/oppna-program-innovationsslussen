package se.vgregion.service.innovationsslussen.validator;

import org.springframework.beans.BeanWrapper;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.portal.innovationsslussen.domain.jpa.IdeaContent;

@Component
public class IdeaValidator implements Validator {

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(Class<?> clazz) {
	    return Idea.class.equals(clazz);
	}	
	
	@Override
	public void validate(Object target, Errors errors) {
		Idea idea = (Idea)target;
		
		validateTitle(idea, errors);
		validateDescription(idea, errors);
		validateName(idea, errors);
		validateEmail(idea, errors);
		
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "required.title", "title-is-required");
		
	}
	
	private void validateTitle(Idea idea, Errors errors) {
		
		String title = idea.getTitle();
		
		if(title == null) {
			errors.rejectValue("title", "title.not-null", "title-must-not-be-null");
		}
		else if(title.equals("")) {
			errors.rejectValue("title", "title.not-empty", "title-must-not-be-empty");
		}
	}
	
	private void validateDescription(Idea idea, Errors errors) {
		
		String description = idea.getIdeaContentPrivate().getDescription();
		
		if(description == null) {
			errors.rejectValue("ideaContentPrivate.description", "description.not-null", "description-must-not-be-null");
		}
		else if(description.equals("")) {
			errors.rejectValue("ideaContentPrivate.description", "description.not-empty", "description-must-not-be-empty");
		}
	}
	
	private void validateName(Idea idea, Errors errors) {
		
		String name = idea.getIdeaPerson().getName();
		
		if(name == null) {
			errors.rejectValue("ideaPerson.name", "name.not-null", "name-must-not-be-null");
		}
		else if(name.equals("")) {
			errors.rejectValue("ideaPerson.name", "name.not-empty", "name-must-not-be-empty");
		}
	}
	
	private void validateEmail(Idea idea, Errors errors) {
		
		String email = idea.getIdeaPerson().getEmail();
		
		if(email == null) {
			errors.rejectValue("ideaPerson.email", "email.not-null", "email-must-not-be-null");
		}
		else if(email.equals("")) {
			errors.rejectValue("ideaPerson.email", "email.not-empty", "email-must-not-be-empty");
		}
	}
	
	
}
