package common;


import java.lang.String;


/**
 * This class is same as the Item entity class and used for item adding to basket
 *
 */

public class BasketItem  {

	   
	private int id;
	private String name;
	private int price;
	private int amount;
	private String category;

	public BasketItem() {
		super();
	}   
	public BasketItem(String name,int price, int amount , String category)
	{
		this.name = name;
		this.price = price;
		this.amount = amount;
		this.category = category;
	}
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}   
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}   
	public int getPrice() {
		return this.price;
	}

	public void setPrice(int price) {
		this.price = price;
	}   
	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	@Override
    public boolean equals(Object object) {
        if (!(object instanceof BasketItem)) {
            return false;
        }
        BasketItem other = (BasketItem) object;
        return this.id == other.id;
    }
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
   
}
