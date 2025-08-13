package mate.academy.rickandmorty.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.rickandmorty.dto.internal.CharacterDto;
import mate.academy.rickandmorty.service.CharacterServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Character management",
        description = "Endpoints for managing Ricky and Morty characters")
@RequiredArgsConstructor
@RestController
@RequestMapping("/character")
public class RickAndMortyController {
    private final CharacterServiceImpl characterService;

    @GetMapping("/random")
    @Operation(summary = "Get a random character",
            description = "Get a random character from Ricky and Morty")
    public CharacterDto getRandomCharacter() {
        return characterService.getRandomCharacter();
    }

    @GetMapping("/search")
    @Operation(summary = "Search characters by name",
            description = "Search the list of characters from Ricky and Morty by the name provided")
    public List<CharacterDto> searchCharacterByName(@RequestParam String name) {
        return characterService.searchCharacterByName(name);
    }
}
