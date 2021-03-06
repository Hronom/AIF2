package io.aif.language.common.settings;

public interface ISettings {

    public ISettings SETTINGS = PropertyBasedSettings.createInstance();

    public String getVersion();

    public int recommendedMinimumTokensInputCount();

    public boolean useIsAlphabeticMethod();

    public double thresholdPForSeparatorCharacterInSecondFilter();

    public int minimalValuableTokenSizeForSentenceSplit();

    public int minimumCharacterObervationsCountForMakingCharatcerValuableDuringSentenceSplitting();

    public double thresholdPFirstFilterForSeparatorCharacter();

    public double splitterCharactersGrouperSearchStep();

    public double splitterCharactersGrouperInitSearchPValue();

    public double wordSetDictComparatorThreshold();
    
    public double recursiveSubstringComparatorWeight();
    
    public double simpleTokenComparatorWeight();

}
