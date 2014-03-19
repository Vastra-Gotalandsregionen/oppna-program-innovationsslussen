package se.vgregion.aspect;

import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import se.vgregion.portal.innovationsslussen.domain.jpa.Idea;
import se.vgregion.service.innovationsslussen.idea.IdeaService;

/**
 * Created with IntelliJ IDEA.
 * User: simongoransson
 * Date: 2013-11-19
 * Time: 11:14
 */

@Aspect
public class IndexerAspect {

    @AfterReturning( pointcut = "execution(* se.vgregion.service.innovationsslussen.idea.IdeaServiceImpl.addIdea(..))" ,
                     returning= "result")
    public void indexAddIdea(JoinPoint joinPoint, Object result){

        if (result.getClass().equals(Idea.class)){
            try {
                Idea idea = (Idea) result;
                Indexer indexer = IndexerRegistryUtil.getIndexer(IDEA_CLASS);
                indexer.reindex(idea);
            } catch (SearchException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterReturning( pointcut = "execution(* se.vgregion.service.innovationsslussen.idea.IdeaServiceImpl.updateFromBarium(..))" ,
            returning= "result")
    public void indexUpdateIdea(JoinPoint joinPoint, Object result){

        if(result != null){
            if (result.getClass().equals(Idea.class)){
                try {
                    Idea idea = (Idea) result;
                    Indexer indexer = IndexerRegistryUtil.getIndexer(IDEA_CLASS);
                    indexer.reindex(idea);
                } catch (SearchException e) {
                    e.printStackTrace();
                }
            } else if(result.getClass().equals(IdeaService.UpdateFromBariumResult.class)) {
                try {
                    IdeaService.UpdateFromBariumResult br = (IdeaService.UpdateFromBariumResult) result;
                    Indexer indexer = IndexerRegistryUtil.getIndexer(IDEA_CLASS);
                    indexer.reindex(((IdeaService.UpdateFromBariumResult) result).getNewIdea());
                } catch (SearchException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @AfterReturning( pointcut = "execution(* se.vgregion.service.innovationsslussen.idea.IdeaServiceImpl.addLike(..))" ,
            returning= "result")
    public void indexAddLike(JoinPoint joinPoint, Object result){

        try {
            Idea idea = (Idea) result;
            Indexer indexer = IndexerRegistryUtil.getIndexer(IDEA_CLASS);
            indexer.reindex(idea);
        } catch (SearchException e) {
            e.printStackTrace();
        }
    }

    @AfterReturning( pointcut = "execution(* se.vgregion.service.innovationsslussen.idea.IdeaServiceImpl.removeLike(..))" ,
            returning= "result")
    public void indexRemoveLike(JoinPoint joinPoint, Object result){

        try {
            Idea idea = (Idea) result;
            Indexer indexer = IndexerRegistryUtil.getIndexer(IDEA_CLASS);
            indexer.reindex(idea);
        } catch (SearchException e) {
            e.printStackTrace();
        }
    }

    @AfterReturning( pointcut = "execution(* se.vgregion.service.innovationsslussen.idea.IdeaService.addMBMessage(..))" ,
            returning= "result")
    public void indexAddComment(JoinPoint joinPoint, Object result){

        try {
            Idea idea = (Idea) result;
            Indexer indexer = IndexerRegistryUtil.getIndexer(IDEA_CLASS);
            indexer.reindex(idea);
        } catch (SearchException e) {
            e.printStackTrace();
        }
    }

    @Around(value = "execution(* se.vgregion.service.innovationsslussen.idea.IdeaServiceImpl.remove(..))", argNames = "idea")
    public void indexRemoveIdea(ProceedingJoinPoint joinPoint){

        Object[] args = joinPoint.getArgs();

        if (args.length > 0){
            Object arg1 = args[0];
            if (arg1.getClass().equals(Idea.class)){

                Idea idea = (Idea) arg1;

                try {
                    Indexer indexer = IndexerRegistryUtil.getIndexer(IDEA_CLASS);
                    indexer.delete(idea);
                } catch (SearchException e) {
                    e.printStackTrace();
                }

                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }else {
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }

    private static String IDEA_CLASS = "se.vgregion.portal.innovationsslussen.domain.jpa.Idea";
}
