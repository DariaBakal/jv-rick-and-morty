package mate.academy.rickandmorty.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.rickandmorty.dto.external.ExternalCharacterDto;
import mate.academy.rickandmorty.exception.ExternalApiException;
import mate.academy.rickandmorty.mapper.CharacterMapper;
import mate.academy.rickandmorty.model.Character;
import mate.academy.rickandmorty.repository.CharacterRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataInitializerService {
    private final RickAndMortyApiClient apiClient;
    private final CharacterRepository repository;
    private final CharacterMapper mapper;

    @PostConstruct
    @Transactional
    public void init() {
        try {
            List<ExternalCharacterDto> dtoList = apiClient.getAllCharacters();
            List<Character> characterList = dtoList.stream()
                    .map(mapper::toModel)
                    .toList();
            repository.saveAll(characterList);
        } catch (Exception e) {
            throw new ExternalApiException("Error during data initialization. ", e);
        }
    }
}
