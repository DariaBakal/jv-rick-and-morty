package mate.academy.rickandmorty.service;

import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import mate.academy.rickandmorty.dto.internal.CharacterDto;
import mate.academy.rickandmorty.exception.CharacterNotFoundException;
import mate.academy.rickandmorty.mapper.CharacterMapper;
import mate.academy.rickandmorty.model.Character;
import mate.academy.rickandmorty.repository.CharacterRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {
    private final CharacterRepository repository;
    private final CharacterMapper mapper;

    private final Random random = new Random();

    @Override
    public CharacterDto getRandomCharacter() {
        int totalCharacters = (int) repository.count();
        if (totalCharacters == 0) {
            throw new CharacterNotFoundException("There are no characters in the DB");
        }
        int randomOffset = generateRandomOffset(totalCharacters);
        List<Character> characters = repository.findAll(PageRequest.of(randomOffset, 1))
                .getContent();
        if (characters.isEmpty()) {
            throw new CharacterNotFoundException(
                    "Failed to retrieve a random character. Data might have changed.");
        }
        Character randomCharacter = characters.get(0);
        return mapper.toDto(randomCharacter);
    }

    @Override
    public List<CharacterDto> searchCharacterByName(String name) {
        return repository.findByNameContainingIgnoreCase(name).stream()
                .map(mapper::toDto)
                .toList();
    }

    private int generateRandomOffset(int totalCharacters) {
        return random.nextInt(totalCharacters);
    }
}
