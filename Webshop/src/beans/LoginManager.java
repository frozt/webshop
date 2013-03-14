package beans;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import entities.*;
import common.*;

@ManagedBean
@SessionScoped

public class LoginManager {
	
		private HtmlDataTable dataTableItems;
		private HtmlDataTable checkoutTableItems;
		private HtmlDataTable dataTableUsers;
		private String buyingAmount;
	
		private String username;
		private String password;
		private ArrayList<BasketItem> basket = new ArrayList<BasketItem>();
		private BasketItem basketItem;
		private List<Item> items = null;
		private List<Users> userList = null;
		public Item newItem;
		private String item_name;
		private String item_price;
		private String item_amount;
		private String item_cat;
		
		public static final String USER_SESSION_KEY = "user";
		public static final String ADMIN_SESSION_KEY = "admin";
		
		private boolean itemListVisible = true;
		private boolean userListVisible = true;
		
		@EJB
		private SystemManager sysMan;
		
	    /**
	     * Default constructor. 
	     */
	    public LoginManager() {
	        // TODO Auto-generated constructor stub
	    }
		/* 
		 * @param text login function - checks user and if found start session for user
		 * */
		 
	    public String login() {      
	    	String returnValue;
	    	FacesContext context = FacesContext.getCurrentInstance();
	    	Users user = sysMan.login(username, password);
	    	if(user != null)
	    	{
	    		System.out.println(user.getUsername() + " user found");
	    		// if user is admin then admin page starts otherwise welcome page starts
	    		if(user.getUsername().equals("admin"))
	    		{
	    			context.getExternalContext().getSessionMap().put(USER_SESSION_KEY, user);
	    			returnValue = "admin";
	    		}
	    		else
	    		{
	    			context.getExternalContext().getSessionMap().put(USER_SESSION_KEY, user);
	    			returnValue = "success";
	    		}
	    	}
	    	// user login fails
	    	else
	    		returnValue = "fail";
	    	
	    	return returnValue;
	    }
	    //new user function and called when new user button is clicked on login page
		public String newUser()
		{
			Users user = getUser();
			// username does not exits
			if(user == null)
			{
				//if user created successfully login the user
				if( sysMan.newUser(username, password).equals("success"))
				{
					login();
					return "success";
				}
				else
					return "fail";
			}
			else
			{
				System.out.println("User already exits");
				return "fail";
			}
		}
		//log out function is called whenever log out link is used
	    public String logout() {
	        HttpSession session = (HttpSession)
	            FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	        if (session != null) {
	            session.invalidate();
	        }
	        return "login";
	    }
	    // get item list for admin or users
	    public List<Item> getItems()
	    {
	    	if(getUsername().equals("admin"))
	    	{
		    	items = sysMan.listAdminItems();
	    	}
	    	else
	    	{
	    		items = sysMan.listItems(item_cat);
	    	}
	    	return items;
	    }
	    // return the user list for admin page
		public List<Users> getUserList() {
			userList = sysMan.listUsers();
			return userList;
		}
		//setter of the user list
		public void setUserList(List<Users> userList) {
			this.userList = userList;
		}
		// add the seleted item to the basket
		public String addToBasket()
		{
			
			newItem = (Item)dataTableItems.getRowData();
			if(Integer.parseInt(buyingAmount) > newItem.getAmount())
			{
				System.out.println("Can not buy more than available");
				return "welcome";
			}
			basketItem = new BasketItem();
			basketItem.setId(newItem.getId());
			basketItem.setAmount(Integer.parseInt(buyingAmount));
			basketItem.setCategory(newItem.getCategory());
			basketItem.setName(newItem.getName());
			basketItem.setPrice(newItem.getPrice());
			basket.add(basketItem);
			
			newItem = null;
			basketItem = null;
			buyingAmount="";
			return "welcome";
		}
		// remove the selected item from basket in checkout page
		public String removeFromBasket()
		{
			basket.remove(checkoutTableItems.getRowIndex());
			return "checkout";
		}
		// direct to checkout page
		public String checkout()
		{
			return "checkout";
		}
		// to buy the items in the basket and updating the inventory for remaining items
		public String buy()
		{
			String result;
			for(int i=0; i<basket.size();i++)
			{
				newItem = new Item();
				newItem.setId(basket.get(i).getId());
				int totalAmount = sysMan.getItemAmount(basket.get(i).getId());
				newItem.setAmount(totalAmount - basket.get(i).getAmount());
				newItem.setCategory(basket.get(i).getCategory());
				newItem.setName(basket.get(i).getName());
				newItem.setPrice(basket.get(i).getPrice());
				result = sysMan.updateInventory(newItem);
				newItem = null;
			}
			basket.clear();
			return "welcome";
		}
		// update entities for updated items in admin page
		public String update()
		{
			String result;
			newItem = (Item)dataTableItems.getRowData();
			result = sysMan.updateInventory(newItem);
			newItem = null;
			return "admin";
		}
		public String deleteItem()
		{
			newItem = (Item)dataTableItems.getRowData();
			if(sysMan.removeItem(newItem).equals("success"))
			{
				System.out.println(newItem.getId()+" removed");
			}
			newItem = null;
			return "admin";
		}
		//update entities for updated users in admin page
		public String userUpdate()
		{
			String result;
			result = sysMan.updateUser((Users)dataTableUsers.getRowData());
			return "admin";
		}
		//return the user for given username to check existence
		private Users getUser()
		{
			Users user= sysMan.getUser(username);
			return user;
		}
		// add new item to the entities in admin page
		public String addNewItem()
		{

			newItem= new Item();
			newItem.setAmount(Integer.parseInt(item_amount));
			newItem.setPrice(Integer.parseInt(item_price));
			newItem.setName(item_name);
			newItem.setCategory(item_cat);
			
			String temp = sysMan.insertInventory(newItem);
			this.item_amount="";
			this.item_name = "";
			this.item_price="";
			newItem = null;
			return "admin";
		}

	//////////////////////////////////////////////////////
		////////// Getters and Setters
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public Item getNewItem() {
			return newItem;
		}
		public void setNewItem(Item newItem) {
			this.newItem = newItem;
		}
		public void setBasket(ArrayList<BasketItem> basket) {
			this.basket = basket;
		}
		public ArrayList<BasketItem> getBasket()
		{
			return basket;
		}
		
		public String getItem_name() {
			return item_name;
		}
		public void setItem_name(String item_name) {
			this.item_name = item_name;
		}
		public String getItem_price() {
			return item_price;
		}
		public void setItem_price(String item_price) {
			this.item_price = item_price;
		}
		public String getItem_amount() {
			return item_amount;
		}
		public void setItem_amount(String item_amount) {
			this.item_amount = item_amount;
		}
		public String getItem_cat() {
			return item_cat;
		}
		public void setItem_cat(String item_cat) {
			this.item_cat = item_cat;
		}
		public boolean isItemListVisible() {
			return itemListVisible;
		}
		public void setItemListVisible(boolean itemListVisible) {
			this.itemListVisible = itemListVisible;
		}
		public boolean isUserListVisible() {
			return userListVisible;
		}
		public void setUserListVisible(boolean userListVisible) {
			this.userListVisible = userListVisible;
		}
		public HtmlDataTable getDataTableItems() {
			return dataTableItems;
		}
		public void setDataTableItems(HtmlDataTable dataTableItems) {
			this.dataTableItems = dataTableItems;
		}
		public String getBuyingAmount() {
			return buyingAmount;
		}
		public void setBuyingAmount(String buyingAmount) {
			this.buyingAmount = buyingAmount;
		}
		public HtmlDataTable getCheckoutTableItems() {
			return checkoutTableItems;
		}
		public void setCheckoutTableItems(HtmlDataTable checkoutTableItems) {
			this.checkoutTableItems = checkoutTableItems;
		}
		public HtmlDataTable getDataTableUsers() {
			return dataTableUsers;
		}
		public void setDataTableUsers(HtmlDataTable dataTableUsers) {
			this.dataTableUsers = dataTableUsers;
		}

}
