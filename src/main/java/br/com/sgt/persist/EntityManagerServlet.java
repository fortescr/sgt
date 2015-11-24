package br.com.sgt.persist;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

public class EntityManagerServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3117690468069758233L;

	public void init(ServletConfig config) throws ServletException {  
		try  
		{  
			EntityManagerSGT2Factory.getEntityManager();
		}  
		catch(Exception ex)  
		{  
			Logger.getLogger(EntityManagerServlet.class.getName()).log(Level.SEVERE, null, ex);
		}  
	}  
}
