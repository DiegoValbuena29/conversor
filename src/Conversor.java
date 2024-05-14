import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Conversor{

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/f7aced73bb5836539cb0c698/latest/USD";

    public static void main(String[] args) {
        try {
            // Obtener los tipos de cambio de la API
            JSONObject exchangeRates = getExchangeRates();

            // Mostrar los tipos de cambio disponibles
            System.out.println("Tipos de cambio disponibles:");
            System.out.println(exchangeRates.getJSONObject("conversion_rates").toString());

            // Obtener la entrada del usuario
            Scanner scanner = new Scanner(System.in);
            System.out.print("Introduce la moneda de origen (USD en este ejemplo): ");
            String fromCurrency = scanner.nextLine().toUpperCase();
            System.out.print("Introduce la moneda de destino: ");
            String toCurrency = scanner.nextLine().toUpperCase();
            System.out.print("Introduce la cantidad a convertir: ");
            double amount = scanner.nextDouble();

            // Realizar la conversión
            double convertedAmount = convertCurrency(exchangeRates, fromCurrency, toCurrency, amount);
            System.out.println(amount + " " + fromCurrency + " equivale a " + convertedAmount + " " + toCurrency);
        } catch (IOException e) {
            System.out.println("Error al conectar con la API: " + e.getMessage());
        }
    }

    private static JSONObject getExchangeRates() throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Leer la respuesta JSON
        Scanner scanner = new Scanner(connection.getInputStream());
        StringBuilder response = new StringBuilder();
        while (scanner.hasNextLine()) {
            response.append(scanner.nextLine());
        }
        scanner.close();

        // Convertir la respuesta a un objeto JSON
        return new JSONObject(response.toString());
    }

    private static double convertCurrency(JSONObject exchangeRates, String fromCurrency, String toCurrency, double amount) {
        JSONObject rates = exchangeRates.getJSONObject("conversion_rates");
        double fromRate = rates.getDouble(fromCurrency);
        double toRate = rates.getDouble(toCurrency);
        return (amount / fromRate) * toRate;
    }
}
