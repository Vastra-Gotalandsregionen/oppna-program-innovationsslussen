package se.vgregion.service.innovationsslussen.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;

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
		
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "required.title", "title-is-required");
		
	}
	
	private void validateTitle(Idea idea, Errors errors) {
		
		String title = idea.getTitle();
		
		if(title == null) {
			errors.rejectValue("title", "required.title", "title-must-not-be-null");
		}
		else if(title.equals("")) {
			errors.rejectValue("title", "required.title", "title-must-not-be-empty");
		}
	}
	
	
	
}
