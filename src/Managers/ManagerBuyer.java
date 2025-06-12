package Managers;

import java.util.*;

import Comparators.CompareBuyersByName;
import Enums.ExceptionsMessages;
import Exceptions.EmptyCartPayException;
import Factory.FactoryUser;
import Models.Buyer;

public class ManagerBuyer implements BuyerInterface {

	private String input;
	private String msg;

	private FactoryUser factoryUser;
	

	private final ArrayList<Buyer> list_buyers = new ArrayList<>();

	private final Comparator<Buyer> comparatorBuyer;

	private static ManagerBuyer instance;

	private ManagerBuyer() {
		comparatorBuyer = new CompareBuyersByName();
		factoryUser= new FactoryUser();

	}

	public static ManagerBuyer getInstance() {
		if (instance == null)
			instance = new ManagerBuyer();
		return instance;
	}

	public ArrayList<Buyer> getBuyers() {
		return list_buyers;
	}



	public String isExistBuyer(String name) {
		for (Buyer buyer : list_buyers) {
			if (buyer.getUserName().equalsIgnoreCase(name))
				return "Buyer name already EXIST, please try again!";
		}
		return null;
	}

	public String chooseValidBuyer(String indexInput) {
		try {
			int index = Integer.parseInt(indexInput);
			if (index > list_buyers.size() || index <= 0)
				throw new IndexOutOfBoundsException(ExceptionsMessages.INVALID_BUYER_INDEX.getExceptionMessage());
		} catch (IndexOutOfBoundsException e) {
			return e.getMessage();
		} catch (NumberFormatException e) {
			return ExceptionsMessages.INVALID_NUMBER_CHOICE.getExceptionMessage();
		}
		return null;
	}

	public String buyersInfo() {//COMp
		if (list_buyers.isEmpty()) {
			return "\nHaven't buyers yet, cannot be proceed. return to Menu.";
		}

		StringBuilder sb = new StringBuilder("\nBuyers info:\n--------------\n");
		ArrayList<Buyer> sortedBuyers = new ArrayList<>(list_buyers);
		sortedBuyers.sort(comparatorBuyer);

		for (int i = 0; i < sortedBuyers.size(); i++) {
			sb.append(i + 1).append(") ");
			sb.append(sortedBuyers.get(i).toString());
		}

		return sb.toString();
	}

	public String buyersNames() {
		StringBuilder sb = new StringBuilder("Buyers:\n--------------\n");
		for (int i = 0; i < list_buyers.size(); i++) {
			sb.append(i + 1)
					.append(") ")
					.append(list_buyers.get(i).getUserName())
					.append("\n");
		}
		return sb.toString();
	}

	public String pay(int buyerIndex, int lastID) {
		try {
			if (list_buyers.get(buyerIndex).getCurrentCart().getSize() == 0)
				throw new EmptyCartPayException(list_buyers.get(buyerIndex).getUserName());
		} catch (EmptyCartPayException e) {
			return e.getMessage();
		}

		list_buyers.get(buyerIndex).payAndMakeHistoryCart(lastID);



		return """
				 ____   _ __   ____  __ _____ _   _ _____                              \s
				|  _ \\ / \\\\ \\ / /  \\/  | ____| \\ | |_   _|                             \s
				| |_) / _ \\\\ V /| |\\/| |  _| |  \\| | | |                               \s
				|  __/ ___ \\| | | |  | | |___| |\\  | | |                               \s
				|_| /_/   \\_\\_|_|_|_ |_|_____|_|_\\_|_|_|  _____ ____  _____ _   _ _    \s
				            / ___|| | | |/ ___/ ___/ ___|| ____/ ___||  ___| | | | |   \s
				            \\___ \\| | | | |   \\___ \\___ \\|  _| \\___ \\| |_  | | | | |   \s
				             ___) | |_| | |___ ___) |__) | |___ ___) |  _| | |_| | |___\s
				            |____/ \\___/ \\____|____/____/|_____|____/|_|    \\___/|_____|""";
	}

	public int replaceCarts(int historyCartIndex, int buyerIndex) {
		int id = list_buyers.get(buyerIndex).getHistoryCart().get(historyCartIndex).getId();

		list_buyers.get(buyerIndex).setCurrentCart(
				list_buyers.get(buyerIndex).getHistoryCart().get(historyCartIndex)
		);
		return id;
	}

	public int chooseBuyer() {
		System.out.println(buyersNames());
		
		while (true) {
			input = UserInput.stringInput("Please choose buyer from the list above:");
			if (input.equals("-1"))
				return -1;
			msg = chooseValidBuyer(input);
			if (msg != null) {
				System.out.println(msg);
			} else {
				break;
			}
		}
		return Integer.parseInt(input) - 1;
	}

	public int paymentForBuyer(int lastID) {
		if (list_buyers.size() == 0) {
			System.out.println("Haven't buyers yet, cannot be proceed. return to Menu.");
			return -1;
		}
		System.out.println("Please choose buyer from list to process checkout: (Enter -1 to return main menu)");
		int buyerIndex = chooseBuyer();
		if (buyerIndex == -1)
			return -1 ;
		System.out.println(pay(buyerIndex,lastID));

		return buyerIndex + 1;
	}

	public HashMap<Integer,Integer> updateCartHistory() {
		if (list_buyers.isEmpty()) {
			System.out.println("Haven't buyers yet, cannot be proceed. return to Menu.");
			return null;
		}

		int buyerIndex = chooseBuyer();
		if (buyerIndex == -1)
			return null;

		Buyer buyer = list_buyers.get(buyerIndex);

		if (buyer.getHistoryCartsNum() == 0) {
			System.out.println("\nHistory cart's are empty for this buyer, cannot proceed. return to main menu.");
			return null;
		}

		System.out.println(buyer.toString());

		do {
			input = UserInput.stringInput("Please choose cart number from history carts:\nIf you have products in your current cart - they will be replaced.");

			if (input.equals("-1"))
				return null;

			msg = isValidHistoryCartIndex(input, buyerIndex);
			if (msg != null) {
				System.out.println(msg);
			}
		} while (msg != null);

		int historyCartIndex = Integer.parseInt(input);
		historyCartIndex = replaceCarts(historyCartIndex - 1, buyerIndex);
		System.out.println("Your current cart updated successfully.");
		HashMap<Integer,Integer> map = new HashMap<>();
		map.put(buyerIndex,historyCartIndex);
		return map;
	}

	public String isValidHistoryCartIndex(String indexCartInput, int buyerIndex) {
		try {
			int indexCart = Integer.parseInt(indexCartInput);
			int historyCount = list_buyers.get(buyerIndex).getHistoryCartsNum();

			if (indexCart <= 0 || indexCart > historyCount) {
				throw new IndexOutOfBoundsException(
						ExceptionsMessages.INVALID_HISTORY_CART_INDEX.getExceptionMessage()
				);
			}
		} catch (NumberFormatException e) {
			return ExceptionsMessages.INVALID_NUMBER_CHOICE.getExceptionMessage();
		} catch (IndexOutOfBoundsException e) {
			return e.getMessage();
		}
		return null;
	}

	public void addBuyer(Buyer buyer) {
		list_buyers.add(buyer);
	}
	public void showlist(){
		for (Buyer s : list_buyers) {
			System.out.println(s);
		}

	}


	public Buyer getBuyerById(int buyerId) {
		for(Buyer buyer : list_buyers){
			if(buyer.getBuyer_id() == buyerId){
				return buyer;
			}
		}
		return null;
	}
}
