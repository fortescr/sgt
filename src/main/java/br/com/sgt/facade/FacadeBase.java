package br.com.sgt.facade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.codehaus.jettison.json.JSONException;

import br.com.sgt.persist.EntityManagerSGT2Factory;

@SuppressWarnings("unchecked")
public class FacadeBase <T>  {
	
	protected static EntityManager em = EntityManagerSGT2Factory.getEntityManager();
	
	public static Object insere(Object entity) throws JSONException, Exception{
		em.getTransaction().begin();
		try{
			em.persist(entity);
			em.flush();
			em.getTransaction().commit();
		} catch(Exception e){
			Logger.getLogger(FacadeBase.class.getName()).log(Level.SEVERE, null, e);
			em.getTransaction().rollback();
		}
		return entity;
	}
	
	public static void atualiza(Object entity) throws Exception{
		em.getTransaction().begin();
		try{
			em.merge(entity);
			em.getTransaction().commit();
		}catch(Exception e){
			Logger.getLogger(FacadeBase.class.getName()).log(Level.SEVERE, null, e);
			em.getTransaction().rollback();
		}
	}
	
	public static void remove(Object entity)  throws Exception, PersistenceException{
		em.getTransaction().begin();
		try{
			em.remove(entity);
			em.getTransaction().commit();
		}catch (PersistenceException px) {   
        	Logger.getLogger(FacadeBase.class.getName()).log(Level.SEVERE, null, px);
        	throw new PersistenceException(px.getMessage());  
        }catch(Exception e){
			Logger.getLogger(FacadeBase.class.getName()).log(Level.SEVERE, null, e);
			em.getTransaction().rollback();
		}
	}
	
	public List<T> findAll(Class<T> entity){
		return em.createQuery("select x from "+entity.getName()).getResultList();
	}
	
	
	public T findById(Class<T> classe, int id){
		return em.find(classe, id);
	}
	
	public T findById(Class<T> classe, String id){
		return em.find(classe, id);
	}
	
	public T selectByNamedQuerySingle(String namedQuery,
			Map<String, Object> parameters) {
		try {
			Query query = substituirParametros(namedQuery, parameters);

			List<T> result = query.getResultList();
			if (result.size() > 0) {
				return (T) result.get(0);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public List<T> selectByNamedQuery(String namedQuery) {
		try {
			Query query = em.createNamedQuery(namedQuery);

			return query.getResultList();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public List<T> selectByNamedQuery(String namedQuery,
			Map<String, Object> parameters) {
		try {

			Query query = substituirParametros(namedQuery, parameters);

			List<T> result = query.getResultList();
			return result;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	protected Query substituirParametros(String namedQuery,
			Map<String, Object> parameters) {
		Query query = em.createNamedQuery(namedQuery);
		parameters = parameters == null ? new HashMap<String, Object>(0) : parameters;
		for (String key : parameters.keySet()) {
			query.setParameter(key, parameters.get(key));
		}
		return query;
	}
}
