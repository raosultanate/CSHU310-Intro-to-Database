
import java.sql.*;
import java.text.DecimalFormat;

/**
 * Java portion of final project for CS-HU 310 Spring 2019 5W1
 * 
 * @author danielrao
 * @author elizabethrios
 */
class project {

	private static Connection conn = null; // The connection object for our database
	private static DecimalFormat priceFormat = new DecimalFormat("#.00");

	/**
	 * tries to create a connection with a MySQL database
	 * and instantiates the static Connection object.
	 * 
	 * Prints error codes if connection is unsuccessful or if the query is invalid
	 * 
	 * @param port
	 * @param database
	 * @param password
	 * @return Connection
	 */
	private static void makeConnection(String port, String database, String password) {
		try {
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:" + port + "/" + database + "?verifyServerCertificate=false&useSSL=true",
					"msandbox", password);
			// Do something with the Connection
			System.out.println("Database " + database + " connection succeeded!");
			System.out.println();
		} catch (SQLException ex) {
			// handle any errors
			System.err.println("SQLException: " + ex.getMessage());
			System.err.println("SQLState: " + ex.getSQLState());
			System.err.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Creates an item and inserts it into the Item table
	 * 
	 * @param itemCode
	 * @param itemDescription
	 * @param price
	 */
	private static void CreateItem(String itemCode, String itemDescription, String price) {		
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement("Call CreateItem('" + itemCode + "', '" + itemDescription + "'," + price + ");");
			
			stmt.executeUpdate();
			System.out.println("Item " + itemCode + " created.\n");

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("SQLException: " + ex.getMessage());
			System.err.println("SQLState: " + ex.getSQLState());
			System.err.println("VendorError: " + ex.getErrorCode());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore
				stmt = null;
			}
		}
	}

	/**
	 * Creates a purchase and inserts it into the Purchase table
	 * 
	 * @param itemCode
	 * @param purchaseQuantity
	 */
	private static void CreatePurchase(String itemCode, int purchaseQuantity) {
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement("Call CreatePurchase('" + itemCode + "', " + purchaseQuantity + ");");
			
			if(stmt.executeUpdate() >= 1) {
				System.out.println("Purchase of " + purchaseQuantity + " " + itemCode + " created.\n");
			} else {
				System.out.println("Error: Cannot create purchase of " + itemCode + " because it does not exist in the list of items.");
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("SQLException: " + ex.getMessage());
			System.err.println("SQLState: " + ex.getSQLState());
			System.err.println("VendorError: " + ex.getErrorCode());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore
				stmt = null;
			}
		}
	}

	/**
	 * Creates a shipment and inserts it into the Shipment table
	 * 
	 * This is WITH a date
	 * 
	 * @param itemCode
	 * @param shipmentQuantity
	 * @param shipmentDate
	 */
	private static void CreateShipment(String itemCode, int shipmentQuantity, Date shipmentDate) {
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement("Call CreateShipment('" + itemCode + "', " + shipmentQuantity + ", '" + shipmentDate + "');");
			
			if(stmt.executeUpdate() >= 1) {
				System.out.println("Shipment of " + shipmentQuantity + " " + itemCode + " created.\n");
			} else {
				System.out.println("Error: Cannot create shipment of " + itemCode + " because it does not exist in the list of items.");
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("SQLException: " + ex.getMessage());
			System.err.println("SQLState: " + ex.getSQLState());
			System.err.println("VendorError: " + ex.getErrorCode());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore
				stmt = null;
			}
		}
	}

	/**
	 * Returns the item table
	 * 
	 * @param itemCode
	 */
	private static void GetItems(String itemCode) {
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("Call GetItems('" + itemCode + "');");
			
			System.out.println("ID:ItemCode:ItemDescription:Price\n");
			
			rs.beforeFirst();
			
			while (rs.next()) {
				System.out.println(rs.getInt(1) + ":" + rs.getString(2) + ":" + rs.getString(3) + ":" + rs.getDouble(4));
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("SQLException: " + ex.getMessage());
			System.err.println("SQLState: " + ex.getSQLState());
			System.err.println("VendorError: " + ex.getErrorCode());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
				} // ignore
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore
				stmt = null;
			}
		}
	}

	/**
	 * Returns the purchases table
	 * 
	 * @param itemCode
	 */
	private static void GetPurchases(String itemCode) {
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("Call GetPurchases('" + itemCode + "');");
			
			System.out.println("ID:ItemID:Quantity:PurchaseDate\n");
			
			rs.beforeFirst();
			
			while (rs.next()) {
				System.out.println(rs.getInt(1) + ":" + rs.getInt(2) + ":" + rs.getInt(3) + ":" + rs.getDate(4));
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("SQLException: " + ex.getMessage());
			System.err.println("SQLState: " + ex.getSQLState());
			System.err.println("VendorError: " + ex.getErrorCode());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
				} // ignore
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore
				stmt = null;
			}
		}
	}

	/**
	 * Returns the shipment table
	 * 
	 * @param itemCode
	 */
	private static void GetShipments(String itemCode) {
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("Call GetShipments('" + itemCode + "');");
			
			System.out.println("ID:ItemID:Quantity:ShipmentDate\n");
			
			rs.beforeFirst();
			
			while (rs.next()) {
				System.out.println(rs.getInt(1) + ":" + rs.getInt(2) + ":" + rs.getInt(3) + ":" + rs.getDate(4));
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("SQLException: " + ex.getMessage());
			System.err.println("SQLState: " + ex.getSQLState());
			System.err.println("VendorError: " + ex.getErrorCode());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
				} // ignore
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore
				stmt = null;
			}
		}
	}

	/**
	 * Returns all available items
	 * 
	 * @param itemCode
	 */
	private static void ItemsAvailable(String itemCode) {
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("Call ItemsAvailable('" + itemCode + "');");
			
			System.out.println("ItemCode:ItemDescription:ItemsAvailable\n");
			
			rs.beforeFirst();
			
			while (rs.next()) {
				System.out.println(rs.getString(1) + ":" + rs.getString(2) + ":" + rs.getInt(3));
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("SQLException: " + ex.getMessage());
			System.err.println("SQLState: " + ex.getSQLState());
			System.err.println("VendorError: " + ex.getErrorCode());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) {
				} // ignore
				rs = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore
				stmt = null;
			}
		}
	}

	/**
	 * Updates the specified item's price
	 * 
	 * @param itemCode
	 * @param price
	 */
	private static void UpdateItem(String itemCode, String price) {
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement("Call UpdateItem('" + itemCode + "', " + price + ");");
			
			if(stmt.executeUpdate() >= 1) {
				System.out.println("Updated " + itemCode + "'s price to " + price + ".\n");
			} else {
				System.out.println("Error: Cannot update " + itemCode + " because it does not exist in the list of items.");
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("SQLException: " + ex.getMessage());
			System.err.println("SQLState: " + ex.getSQLState());
			System.err.println("VendorError: " + ex.getErrorCode());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore
				stmt = null;
			}
		}
	}

	/**
	 * Deletes the specified item from the item table
	 * 
	 * @param itemCode
	 */
	private static void DeleteItem(String itemCode) {
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement("Call DeleteItem('" + itemCode + "');");
			
			if(stmt.executeUpdate() >= 1) {
				System.out.println("Deleted " + itemCode + " from the list of items.");
			} else {
				System.out.println("ERROR: Could not delete " + itemCode + " because it does not exist in the list of items.");
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("SQLException: " + ex.getMessage());
			System.err.println("SQLState: " + ex.getSQLState());
			System.err.println("VendorError: " + ex.getErrorCode());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore
				stmt = null;
			}
		}
	}



	/**
	 * Deletes the shipment from the shipment table
	 * 
	 * @param itemCode
	 */
	private static void DeleteShipment(String itemCode) {
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement("Call DeleteShipment('" + itemCode + "');");
			
			if(stmt.executeUpdate() >= 1) {
				System.out.println("Deleted " + itemCode + " from the list of shipments.");
			} else {
				System.out.println("ERROR: Could not delete " + itemCode + " because it does not exist in the list of shipments.");
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("SQLException: " + ex.getMessage());
			System.err.println("SQLState: " + ex.getSQLState());
			System.err.println("VendorError: " + ex.getErrorCode());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore
				stmt = null;
			}
		}
	}
	
	
	/**
	 * Deletes the purchase from the purchase table
	 * 
	 * @param itemCode
	 */
	private static void DeletePurchase(String itemCode) {
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement("Call DeletePurchase('" + itemCode + "');");
			
			if(stmt.executeUpdate() >= 1) {
				System.out.println("Deleted " + itemCode + " from the list of purchases.");
			} else {
				System.out.println("ERROR: Could not delete " + itemCode + " because it does not exist in the list of purchases.");
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("SQLException: " + ex.getMessage());
			System.err.println("SQLState: " + ex.getSQLState());
			System.err.println("VendorError: " + ex.getErrorCode());
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) {
				} // ignore
				stmt = null;
			}
		}
	}

	/**
	 * Prints usage for the user
	 */
	private static void printUsage() {
		System.err.println("Usage: \n"
				+ "/?\n"
				+ "CreateItem <itemCode> <itemDescription> <price>\n"
				+ "CreatePurchase <itemCode> <PurchaseQuantity>\n"
				+ "CreateShipment <itemCode> <ShipmentQuantity> <Shipment Date>\n"
				+ "GetItems <itemCode>\n"
				+ "GetPurchases <itemCode>\n"
				+ "GetShipments <itemCode>\n"
				+ "ItemsAvailable <itemCode>\n"
				+ "UpdateItem <itemCode> <price>\n"
				+ "DeleteItem <itemCode>\n"
				+ "DeletePurchase <itemCode>\n"
				+ "DeleteShipment <itemCode>\n");
	}

	/**
	 * Main method
	 * 
	 * Connects to the database, parses the user query, returns data values
	 * and/or modifies the table, and closes the database connection.
	 * 
	 * If query's format is invalid, prints usage.
	 * 
	 * If query results in a MySQL error, prints the MySQL error message.
	 * 
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			System.out.println();
			System.out.println("JDBC driver loaded");
			
			makeConnection("52935", "FinalProject", "nepal1234");
			
			switch (args[0]) {
			case "/?":
				printUsage();
				break;
			case "CreateItem":
				if (args.length == 4 && (Double.valueOf(args[3]) >= 0)) {
					String itemCode = args[1];
					String itemDescription = args[2];
					Double price = Double.valueOf(args[3]);
					
					String priceString = priceFormat.format(price);
					
					CreateItem(itemCode, itemDescription, priceString);
					
				} else {
					System.err.println("Error: Please specify an item code (% for all), item description, and price (0.00 or greater) when creating an item.\n");
					printUsage();
				}
				break;
				
				
			case "CreatePurchase":
				if (args.length == 3 && (Integer.valueOf(args[2]) > 0)) {
					String itemCode = args[1];
					int purchaseQuantity = Integer.valueOf(args[2]);
					
					CreatePurchase(itemCode, purchaseQuantity);
					
				} else {
					System.err.println("Error: Please specify an item code (% for all) and purchase quantity (greater than 0) when creating a purchase.\n");
					printUsage();
				}
				break;
			case "CreateShipment":
				if (args.length == 4 && (Integer.valueOf(args[2]) > 0)) {
					String itemCode = args[1];
					int shipmentQuantity = Integer.valueOf(args[2]);
					Date shipmentDate = Date.valueOf(args[3]);
					
					CreateShipment(itemCode, shipmentQuantity, shipmentDate);
					
				} else {
					System.err.println("Error: Please specify an item code (% for all), shipment quantity (greater than 0), and shipment date () when creating a shipment.\n");
					printUsage();
				}
				break;
			case "GetItems":
				if (args.length == 2) {
					String itemCode = args[1];
					
					GetItems(itemCode);
					
				} else {
					System.err.println("Error: Please specify an item code (% for all) when asking for items log.\n");
					printUsage();
				}
				break;
			case "GetShipments":
				if (args.length == 2) {
					String itemCode = args[1];
					
					GetShipments(itemCode);
					
				} else {
					System.err.println("Error: Please specify an item code (% for all) when asking for shipment log.\n");
					printUsage();
				}
				break;
			case "GetPurchases":
				if (args.length == 2) {
					String itemCode = args[1];
					
					GetPurchases(itemCode);
					
				} else {
					System.err.println("Error: Please specify an item code (% for all) when asking for purchases log.\n");
					printUsage();
				}
				break;
			case "ItemsAvailable":
				if (args.length == 2) {
					String itemCode = args[1];
					
					ItemsAvailable(itemCode);
					
				} else {
					System.err.println("Error: Please specify an item code (% for all) when asking for the available items log.\n");
					printUsage();
				}
				break;
			case "UpdateItem":
				if (args.length == 3 && (Double.valueOf(args[2]) >= 0)) {
					String itemCode = args[1];
					Double price = Double.valueOf(args[2]);
					
					String priceString = priceFormat.format(price);
					
					UpdateItem(itemCode, priceString);
					
				} else {
					System.err.println("Error: Please specify an item code and price (0 or greater) when updating an item.\n");
					printUsage();
				}
				break;
			case "DeleteItem":
				if (args.length == 2) {
					String itemCode = args[1];
					
					DeleteItem(itemCode);
					
				} else {
					System.err.println("Error: Please specify an item code when deleting an item.\n");
					printUsage();
				}
				break;
			case "DeletePurchase":
				if (args.length == 2) {
					String itemCode = args[1];
					
					DeletePurchase(itemCode);
					
				} else {
					System.err.println("Error: Please specify an item code when deleting a purchase.\n");
					printUsage();
				}
				break;
			case "DeleteShipment":
				if (args.length == 2) {
					String itemCode = args[1];
					
					DeleteShipment(itemCode);
					
				} else {
					System.err.println("Error: Please specify an item code when deleting a shipment.\n");
					printUsage();
				}
				break;
			default:
				printUsage();
				break;
			}
			
			System.out.println();
			System.out.println("Database connection closed");
			System.out.println();
		} catch (Exception ex) {
			// handle the error
			System.err.println(ex);
		}
	}
}
