package io.aif.language.sentence.separators.extractors;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class StatData {

    private final Map<Character, Map<Character, Integer>>   charactersNearEdgeCharacter = new ConcurrentHashMap<>();

    private final Map<Character, Integer>                   edgeCharacters              = new ConcurrentHashMap<>();

    private final Map<Character, Integer>                   characters                  = new ConcurrentHashMap<>();

    public void addEdgeCharacter(final Character characterNearEdgeCharacter, final Character edgeCharacter) {
        final Character lowCaseCharacterBeforeEndCharacter = StatData.prepareCharacter(characterNearEdgeCharacter);
        final Character lowCaseEndCharacter = StatData.prepareCharacter(edgeCharacter);

        edgeCharacters.merge(lowCaseEndCharacter, 1, (v1, v2) -> v1 + v2);

        final Map<Character, Integer> characterBeforeEndCharacterMap = getMapForEdgeCharacter(edgeCharacter);
        characterBeforeEndCharacterMap.merge(lowCaseCharacterBeforeEndCharacter, 1, (v1, v2) -> v1 + v2);
    }

    public void addCharacter(final Character ch) {
        final Character lowCaseCharacter = StatData.prepareCharacter(ch);
        characters.merge(lowCaseCharacter, 1, (v1, v2) -> v1 + v2);
    }

    public Set<Character> getAllCharacters() {
        return characters.keySet();
    }

    public double getProbabilityThatCharacterIsSplitterCharacter(final Character ch) {
        return getProbabilityThatCharacterOnEdge(ch) *
                getProbabilityThatCharacterBeforeIsEdgeCharacter(ch) *
                getProbabilityThatCharacterBeforeIsEdgeCharacter(ch);
    }

    public int getCharacterCount(final Character ch) {
        return characters.get(ch);
    }

    private Map<Character, Integer> getMapForEdgeCharacter(final Character endCharacter) {
        if (!charactersNearEdgeCharacter.containsKey(endCharacter)) {
            synchronized (charactersNearEdgeCharacter) {
                final Map<Character, Integer> targetMap = charactersNearEdgeCharacter.getOrDefault(endCharacter, new ConcurrentHashMap<Character, Integer>());
                charactersNearEdgeCharacter.put(endCharacter, targetMap);
                return targetMap;
            }
        }
        return charactersNearEdgeCharacter.getOrDefault(endCharacter, new ConcurrentHashMap<>());
    }

    public double getProbabilityThatCharacterOnEdge(final Character ch) {
        final Character lowCaseCharacter = StatData.prepareCharacter(ch);
        return (double) edgeCharacters.getOrDefault(lowCaseCharacter, 0) /
                (double) characters.getOrDefault(lowCaseCharacter, 0);
    }

    public double getProbabilityThatCharacterBeforeIsEdgeCharacter(final Character ch) {
        final Character lowCaseCharacter = StatData.prepareCharacter(ch);
        final Map<Character, Integer> beforeCharacters = charactersNearEdgeCharacter.getOrDefault(lowCaseCharacter, new HashMap<>());

        return beforeCharacters.keySet()
                .stream()
                .mapToDouble(k -> getProbabilityThatCharacterOnEdge(k) * (double)beforeCharacters.get(k))
                .sum() / (double) edgeCharacters.getOrDefault(lowCaseCharacter, 1);
    }

    private static Character prepareCharacter(final Character ch) {
        return Character.toLowerCase(ch);
    }

}
