package mate.academy.rickandmorty.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.rickandmorty.dto.external.ExternalCharacterDto;
import mate.academy.rickandmorty.dto.external.Info;
import mate.academy.rickandmorty.dto.external.RickAndMortyApiResponse;
import mate.academy.rickandmorty.exception.ExternalApiException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RickAndMortyApiClient {
    private static final String BASE_URL = "https://rickandmortyapi.com/api/character";
    private final ObjectMapper objectMapper;

    public List<ExternalCharacterDto> getAllCharacters() {
        HttpClient httpClient = HttpClient.newHttpClient();
        List<ExternalCharacterDto> allCharacters = new ArrayList<>();
        String currentUrl = BASE_URL;
        while (currentUrl != null) {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(currentUrl))
                    .build();
            try {
                HttpResponse<String> response = httpClient.send(httpRequest,
                        HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != 200) {
                    throw new ExternalApiException(
                            "Failed to fetch data from Rick & Morty API: Status "
                                    + response.statusCode());
                }
                RickAndMortyApiResponse apiResponse = objectMapper.readValue(
                        response.body(), RickAndMortyApiResponse.class);
                allCharacters.addAll(apiResponse.getResults());

                currentUrl = Optional.ofNullable(apiResponse.getInfo())
                        .map(Info::getNext)
                        .orElse(null);
            } catch (IOException | InterruptedException e) {
                throw new ExternalApiException("Error while fetching from Rick & Morty API ", e);
            }
        }
        return allCharacters;
    }
}
