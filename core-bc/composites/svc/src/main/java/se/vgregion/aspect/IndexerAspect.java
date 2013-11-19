package se.vgregion.aspect;

import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.service.innovationsslussen.idea.IdeaService;

/**
 * Created with IntelliJ IDEA.
 * User: simongoransson
 * Date: 2013-11-19
 * Time: 11:14
 * To change this template use File | Settings | File Templates.
 */


@Aspect
public class IndexerAspect {

    @AfterReturning( pointcut = "execution(* se.vgregion.service.innovationsslussen.idea.IdeaServiceImpl.updateFromBarium(..))" ,
                     returning= "result")
    public void indexAddIdea(JoinPoint joinPoint, Object result){

        System.out.println("logAfter() is running!");
        System.out.println("hijacked : " + joinPoint.getSignature().getName());

        IdeaService.UpdateFromBariumResult br = (IdeaService.UpdateFromBariumResult) result;

        Indexer indexer = IndexerRegistryUtil.getIndexer(IDEA_CLASS);

        try {
            indexer.reindex(((IdeaService.UpdateFromBariumResult) result).getNewIdea());
        } catch (SearchException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println("Method returned value for an idea.title is : " + br.getNewIdea().getTitle());
        System.out.println("******");

    }

    private static String IDEA_CLASS = "se.vgregion.portal.innovationsslussen.domain.jpa.Idea";

}
