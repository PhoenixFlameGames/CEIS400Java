package ToolManagementSystem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * ClientAPI - Handles communication with the FastAPI backend.
 */
public class ClientAPI {

    private static final String BASE_URL = "http://localhost:8000";  // Replace with your FastAPI server URL
    private final Gson gson = new Gson();

    // Fetch list of employees
    public List<Employee> getEmployees() throws Exception {
        String endpoint = BASE_URL + "/employees";
        String jsonResponse = getResponse(endpoint);
        return gson.fromJson(jsonResponse, new TypeToken<List<Employee>>(){}.getType());
    }

    // Fetch list of tools
    public List<Tool> getInventory() throws Exception {
        String endpoint = BASE_URL + "/inventory";
        String jsonResponse = getResponse(endpoint);
        return gson.fromJson(jsonResponse, new TypeToken<List<Tool>>(){}.getType());
    }

    // Fetch list of open transactions
    public List<Transaction> getTransactions() throws Exception {
        String endpoint = BASE_URL + "/open-transactions";
        String jsonResponse = getResponse(endpoint);
        return gson.fromJson(jsonResponse, new TypeToken<List<Transaction>>(){}.getType());
    }

    // Helper method to make a GET request and return the response as a string
    private String getResponse(String endpoint) throws Exception {
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return response.toString();
            }
        } else {
            throw new Exception("GET request failed, Response Code: " + responseCode);
        }
    }

    // Send a transaction to the server
    public boolean sendTransaction(int empID, int toolID, String transactionType) throws Exception {
        String endpoint = BASE_URL + "/transactions";
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        // Create transaction object
        Transaction transaction = new Transaction();
        transaction.setTransaction_owner_id(empID);
        transaction.setTransaction_item_id(toolID);
        transaction.setTransaction_type(transactionType);
        transaction.setTransaction_status("Pending");
        transaction.setTransaction_open_date(new Date());

        // Convert transaction object to JSON
        String jsonInputString = gson.toJson(transaction);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                // Assuming that a successful POST request will return a response indicating success
                return response.toString().equals("true"); // Adjust based on actual API response
            }
        } else {
            throw new Exception("POST request failed, Response Code: " + responseCode);
        }
    }

/*    // Testing of the ClientAPI
    public static void main(String[] args) {
        try {
            ClientAPI clientAPI = new ClientAPI();

            List<Employee> employees = clientAPI.getEmployees();
            System.out.println("Employees:");
            employees.forEach(System.out::println);

            List<Tool> inventory = clientAPI.getInventory();
            System.out.println("Inventory:");
            inventory.forEach(System.out::println);

            List<Transaction> openTransactions = clientAPI.getTransactions();
            System.out.println("Open Transactions:");
            openTransactions.forEach(System.out::println);

            // Test sendTransaction
            boolean transactionResult = clientAPI.sendTransaction(1, 101, "Checked Out");
            System.out.println("Transaction Result: " + transactionResult);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
}