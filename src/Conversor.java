import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Conversor {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/f7aced73bb5836539cb0c698/latest/USD";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            try {

                JSONObject exchangeRates = getExchangeRates();


                JSONObject filteredRates = new JSONObject();
                filteredRates.put("USD", exchangeRates.getJSONObject("conversion_rates").getDouble("USD"));
                filteredRates.put("ARS", exchangeRates.getJSONObject("conversion_rates").getDouble("ARS"));
                filteredRates.put("BOB", exchangeRates.getJSONObject("conversion_rates").getDouble("BOB"));
                filteredRates.put("BRL", exchangeRates.getJSONObject("conversion_rates").getDouble("BRL"));
                filteredRates.put("CLP", exchangeRates.getJSONObject("conversion_rates").getDouble("CLP"));
                filteredRates.put("COP", exchangeRates.getJSONObject("conversion_rates").getDouble("COP"));


                System.out.println("=== Conversor de Monedas ===");
                System.out.println("1. Convertir monedas");
                System.out.println("2. Salir");
                System.out.print("Selecciona una opción: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:

                        System.out.print("Introduce la moneda de origen (USD, ARS, BOB, BRL, CLP, COP): ");
                        String fromCurrency = scanner.next().toUpperCase();
                        System.out.print("Introduce la moneda de destino (USD, ARS, BOB, BRL, CLP, COP): ");
                        String toCurrency = scanner.next().toUpperCase();
                        System.out.print("Introduce la cantidad a convertir: ");
                        double amount = scanner.nextDouble();


                        double convertedAmount = Conversor(filteredRates, fromCurrency, toCurrency, amount);
                        System.out.println(amount + " " + fromCurrency + " equivale a " + convertedAmount + " " + toCurrency);
                        break;
                    case 2:
                        exit = true;
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, elige una opción válida.");
                }
            } catch (IOException | InterruptedException | URISyntaxException e) {
                System.out.println("Error al conectar con la API: " + e.getMessage());
            }
        }
        scanner.close();
        System.out.println("Gracias por usar el conversor de monedas.");
    }

    private static JSONObject getExchangeRates() throws IOException, InterruptedException, URISyntaxException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI(API_URL)).GET().build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


        if (response.statusCode() == 200) {
            String jsonString = response.body();
            return new JSONObject(jsonString);
        } else {
            throw new IOException("Error en la respuesta: " + response.statusCode());
        }
    }

    private static double Conversor(JSONObject exchangeRates, String fromCurrency, String toCurrency, double amount) {
        double fromRate = exchangeRates.getDouble(fromCurrency);
        double toRate = exchangeRates.getDouble(toCurrency);
        return (amount / fromRate) * toRate;
    }
}
