package gov.epa.ccte.api.rapidtox.chemical.service;

import gov.epa.ccte.api.rapidtox.chemical.model.ChemicalSearch;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Slf4j
public class ChemicalSearchUtils {
    public static String preprocessingSearchWord(String searchWord){

        // From https://confluence.epa.gov/display/CCTEA/Search+Requirements
        // Make all character upper case
        searchWord = searchWord.toUpperCase();
        // Search word should be trim
        searchWord = searchWord.trim();

        if(isCasrn(searchWord)){
            log.debug("{} is a casrn ", searchWord);
            return searchWord;
        }else{
            searchWord = searchWord.replaceAll("-"," ");
            log.debug("preprocessed search word = {}", searchWord);
            return searchWord;
        }
    }

    public static boolean isCasrn(String casrn) {
        return casrn.matches("^\\d{1,7}-\\d{2}-\\d$");
    }

    public static String[] preprocessingSearchWords(String[] words) {

        String[] searchWords = new String[words.length];
        int i =0;

        for(String word : words){
            searchWords[i] = preprocessingSearchWord(word);
            i++;
        }
        return searchWords;
    }

    public static List<ChemicalSearch> insertNotFound(List<ChemicalSearch> searchResults, String[] searchWords) {
        HashSet<String> searches = new HashSet<>(Arrays.asList(searchWords));

        for(ChemicalSearch searchResult : searchResults){
            if(searches.contains(searchResult.getModifiedValue())){
                searches.remove(searchResult.getModifiedValue());
            }
        }

        // add NOT FOUND records

        for(String search : searches){
            searchResults.add(addNotFoundRecord(search));
        }

        return searchResults;
    }

    private static ChemicalSearch addNotFoundRecord(String search) {
        ChemicalSearch chemicalSearch = new ChemicalSearch();

        chemicalSearch.setSearchGroup("NOT FOUND");
        chemicalSearch.setSearchWord(search);

        return chemicalSearch;
    }
}
