package se.vgregion.service.idea;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.service.idea.exception.CreateIdeaException;
import se.vgregion.service.idea.repository.IdeaRepository;

/**
 * Implementation of {@link IdeaService}.
 *
 * @author Erik Andersson
 * @company Monator Technologies AB
 */
public class IdeaServiceImpl implements IdeaService {

    private IdeaRepository ideaRepository;

    /**
     * Constructor.
     *
     * @param ideaRepository the {@link IdeaRepository}
     */
    @Autowired
    public IdeaServiceImpl(IdeaRepository ideaRepository) {
        this.ideaRepository = ideaRepository;
    }

    @Override
    public List<Idea> findIdeaByCompanyId(long companyId) {
        return ideaRepository.findIdeasByCompanyId(companyId);
    }

    @Override
    public List<Idea> findIdeaByGroupId(long companyId, long groupId) {
        return ideaRepository.findIdeasByGroupId(companyId, groupId);
    }

    @Override
    @Transactional(rollbackFor = CreateIdeaException.class)
    public void addIdea(String bariumId, long userId, long companyId, long groupId) {
        Idea idea = new Idea(bariumId, userId, companyId, groupId);
        ideaRepository.merge(idea);
        
        // Create Liferay Asset
        // Create Liferay Resource
        // If any of these fails, Listen to Liferay exception and throw CreateIdeaException
        // https://bitbucket.org/martinlau/spring-liferay-integration
    }

    @Override
    public Collection<Idea> findAll() {
        return ideaRepository.findAll();
    }

    @Override
    @Transactional
    public void remove(Idea idea) {
        ideaRepository.remove(idea);
    }

    @Override
    @Transactional
    public void removeAll() {
        Collection<Idea> all = ideaRepository.findAll();
        for (Idea idea : all) {
            ideaRepository.remove(idea);
        }
    }

}
