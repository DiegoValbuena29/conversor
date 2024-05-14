import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Conversor {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/f7aced73bb5836539cb0c698/latest/USD";

    public static void main(String[] args) {
        try {
            // Obtener los tipos de cambio de la API
            JSONObject exchangeRates = getExchangeRates();

            // Mostrar los tipos de cambio disponibles
            System.out.println("Tipos de cambio disponibles:");
            System.out.println(exchangeRates.getJSONObject("conversion_rates").toString());

            // Aquí puedes continuar con el código para realizar la conversión de moneda
            // ...
        } catch (IOException | InterruptedException | URISyntaxException e) {
            System.out.println("Error al conectar con la API: " + e.getMessage());
        }
    }

    private static JSONObject getExchangeRates() throws IOException, InterruptedException, URISyntaxException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI(API_URL)).GET().build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Verificar el código de estado de la respuesta
        if (response.statusCode() == 200) {
            String jsonString = response.body();
            return new JSONObject(jsonString);
        } else {
            throw new IOException("Error en la respuesta: " + response.statusCode());
        }
    }
}
