package beans;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.LocalBean;
import javax.ejb.Remove;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.eclipse.persistence.internal.jpa.ExceptionFactory;

import com.sun.tools.ws.wsdl.document.jaxws.Exception;
import entities.*;

/**
 * Session Bean implementation class SystemManager
 */

@Stateless
@LocalBean
public class SystemManager implements Serializable{

	//@Inject @LoggedIn Users currentUser;
    /**
     * Default constructor. 
     */
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Webshop");
	private EntityManager em = emf.createEntityManager();
	
    public SystemManager() {
        // TODO Auto-generated constructor stub
    	
    }
    // user login check from the users entity with given username and password
    public Users login (String username, String password)
    {
    	Users found = em.find(Users.class, username);
    	//if user exists and password match then return the user
		if (found.getPassword().equals(password))
			if(found.getIsActive())
			{
				return found;
			}
		// if password doesnt match returns null
		return null;
    	
    }
    // return user object for given username to check existence
    public Users getUser (String username)
	{
		Users found = em.find(Users.class, username);
		if(found != null)
		{
			return found;
		}
		else
		{
			return null;
		}
		
	}
    //returns the amount of the given item in buy process to calculate remaining amount
    public int getItemAmount(int ID)
    {
    	Item item = em.find(Item.class, ID);
    	return item.getAmount();
    }
    // new user function called in login page
	public String newUser (String username, String password)
	{
		
			em.getTransaction().begin();
			Users users = new Users(username,password,true);
		    em.persist(users);
		    em.getTransaction().commit();
		
		return "success";
	}
	//update users entity changed in admin page
	public String updateUser(Users updatedUser)
	{
		em.getTransaction().begin();
		em.merge(updatedUser);
	    em.getTransaction().commit();
		return "success";
	}
	//update item entity changed in admin page
	public String updateInventory(Item newItem)
	{
		em.getTransaction().begin();
		em.merge(newItem);
	    em.getTransaction().commit();
		return "success";
	}
	//add new item to the item entity from admin page
	public String insertInventory(Item newItem)
	{
		em.getTransaction().begin();
		em.persist(newItem);
	    em.getTransaction().commit();
		return "success";
	}
	public String removeItem(Item newItem)
	{
		em.getTransaction().begin();
		em.remove(newItem);
	    em.getTransaction().commit();
		return "success";
	}
	//list all items for admin page
	@SuppressWarnings("unchecked")
	public List<Item> listAdminItems()
	{
		Query query = em.createQuery("select o from Item o");
		return query.getResultList();
	}
	//list items for users in welcome page
	@SuppressWarnings("unchecked")
	public List<Item> listItems(String cat)
	{
		//list the selected category items
		if(cat != null)
		{
			Query query = em.createQuery("select o from Item o where o.amount > :amount and o.category=:cat");
			query.setParameter("amount", 0);
			query.setParameter("cat", cat);
			return query.getResultList();
		}
		//list all items that available
		else
		{
			Query query = em.createQuery("select o from Item o where o.amount > :amount");
			query.setParameter("amount", 0);
			return query.getResultList();
		}
	}
	//list all users for admin page
	@SuppressWarnings("unchecked")
	public List<Users> listUsers()
	{
		Query query = em.createQuery("select o from Users o");
		return query.getResultList();	
	}



}
