package ca.jonestremblay.WeatherAPI.controllers;

import ca.jonestremblay.WeatherAPI.models.CurrentWeather;
import ca.jonestremblay.WeatherAPI.models.Location;
import ca.jonestremblay.WeatherAPI.models.WeatherData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WeatherController {

    @GetMapping("/weatherForm")
    public String showWeatherForm() {
        return "weather-form.html";
    }

    @PostMapping("/weatherPost")
    @ResponseBody
    public String getWeather(@RequestParam("apiKey") String apiKey, @RequestParam("location") String location, Model model) {
        // Effectuer l'appel à l'API météo avec les paramètres fournis
        // Ici, vous pouvez insérer votre code pour effectuer l'appel à l'API et récupérer les données météo

        // Ajouter les données météo au modèle
        model.addAttribute("apiKey", apiKey);
        model.addAttribute("location", location);

        // Insérer ici le code pour ajouter les données météo retournées par l'API au modèle


        return "Ton API KEY : " + apiKey + "   | Ta location : " + location;
    }

    @GetMapping("/get-weather-api")
    @ResponseBody
    public String makeApiCall(@RequestParam("query") String queryParam) {
        String queryEmpty =  queryParam.isEmpty() ? "You need to input a query. It can be a city, postal code, etc." : "";
        if (!queryEmpty.isEmpty()) { return queryEmpty;}

        RestTemplate restTemplate = new RestTemplate();

        String apiDomainUrl = "http://api.weatherapi.com/v1/";
        String endpointUrl = "current.json?q=";
        String apiKeyUrlSuffix = "&key=900b5060847b4dd2bda141416231007";

        String requestUrl = apiDomainUrl + endpointUrl + queryParam + apiKeyUrlSuffix;

        try {
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("THE STATUS CODE IS : " + response.getStatusCode());

                String jsonResults = response.getBody();

                ObjectMapper objectMapper = new ObjectMapper();
                WeatherData weatherData = objectMapper.readValue(jsonResults, WeatherData.class);

                // Utilisez maintenant l'objet weatherData pour accéder aux données météorologiques :
                Location location = weatherData.getLocation();
                CurrentWeather currentWeather = weatherData.getCurrent();

                String fullObjects = "WeatherData : " + weatherData.toString() + "\n\n\nLocation : " + location.toString() + "\n\n\nCurrentWeather : " + currentWeather.toString();

                String ville = location.getName();
                String region = location.getRegion();
                String temperatureActuelleC = String.valueOf(currentWeather.getTemp_c());
                String temperatureActuelleF = String.valueOf(currentWeather.getTemp_f());

                //String htmlResults = "<html><header><h1>Welcome</h1></header><body>Hello world</body></html>";
                String htmlCode = "<!DOCTYPE html><html><head><title>Resultats meteo</title><style>body{font-family:Arial,sans-serif;}h1{font-weight:bold;}input[type=\"text\"]{background-color:#e6f0ff;}</style></head><body><h1>Resultats metéo</h1><form><label for=\"city\">Ville :</label><input disabled type=\"text\" id=\"city\" name=\"city\" value=\"" + ville + "\"><br><br><label for=\"region\">Region :</label><input disabled type=\"text\" id=\"region\" name=\"region\" value=\"" + region + "\"><br><br><label for=\"country\">Pays :</label><input disabled type=\"text\" id=\"country\" name=\"country\" value=\"" + location.getCountry() + "\"><br><br><label for=\"temperature\">Temperature actuelle :</label><input disabled type=\"text\" id=\"temperature\" name=\"temperature\" value=\"" + temperatureActuelleC + " \"><br><br><label for=\"temperatureF\">Temperature actuelle en Fahrenheit :</label><input disabled type=\"text\" id=\"temperatureF\" name=\"temperatureF\" value=\"" + temperatureActuelleF + "\"><br><br></form></body></html>";


                return htmlCode;
                //return fullObjects;
                //return "La région de la ville entrée est : " +location.getRegion();
            } else {
                return "Erreur lors de l'appel à l'API : statut " + response.getStatusCode();
            }
        } catch (HttpClientErrorException e){
            return "Erreur lors de l'appel à l'API : " + e.getMessage();
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
