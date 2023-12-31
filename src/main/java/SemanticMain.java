import org.apache.commons.lang3.time.StopWatch;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SemanticMain {
    public List<String> listVocabulary = new ArrayList<>();  //List that contains all the vocabularies loaded from the csv file.
    public List<double[]> listVectors = new ArrayList<>(); //Associated vectors from the csv file.
    public List<Glove> listGlove = new ArrayList<>();
    public final List<String> STOPWORDS;

    public SemanticMain() throws IOException {
        STOPWORDS = Toolkit.loadStopWords();
        Toolkit.loadGLOVE();
    }


    public static void main(String[] args) throws IOException {
        StopWatch mySW = new StopWatch();
        mySW.start();
        SemanticMain mySM = new SemanticMain();
        mySM.listVocabulary = Toolkit.getListVocabulary();
        mySM.listVectors = Toolkit.getlistVectors();
        mySM.listGlove = mySM.CreateGloveList();

        List<CosSimilarityPair> listWN = mySM.WordsNearest("computer");
        Toolkit.PrintSemantic(listWN, 5);

        listWN = mySM.WordsNearest("phd");
        Toolkit.PrintSemantic(listWN, 5);

        List<CosSimilarityPair> listLA = mySM.LogicalAnalogies("china", "uk", "london", 5);
        Toolkit.PrintSemantic("china", "uk", "london", listLA);

        listLA = mySM.LogicalAnalogies("woman", "man", "king", 5);
        Toolkit.PrintSemantic("woman", "man", "king", listLA);

        listLA = mySM.LogicalAnalogies("banana", "apple", "red", 3);
        Toolkit.PrintSemantic("banana", "apple", "red", listLA);
        mySW.stop();

        if (mySW.getTime() > 2000)
            System.out.println("It takes too long to execute your code!\nIt should take less than 2 second to run.");
        else
            System.out.println("Well done!\nElapsed time in milliseconds: " + mySW.getTime());
    }

    public List<Glove> CreateGloveList() {
        List<Glove> listResult = new ArrayList<>();
        //TODO Task 6.1
        for (int i = 0; i < listVocabulary.size(); i++)
        {
            boolean wordExistsInStopWords = false;
            for (int j = 0; j < STOPWORDS.size(); j++)
            {
                if (listVocabulary.get(i).equals(STOPWORDS.get(j)))
                {
                    wordExistsInStopWords = true;
                }
            }

            if (wordExistsInStopWords == false)
            {
                listResult.add(new Glove(listVocabulary.get(i),new Vector(listVectors.get(i))));
            }
        }
        return listResult;
    }

    public List<CosSimilarityPair> WordsNearest(String _word) {
        List<CosSimilarityPair> listCosineSimilarity = new ArrayList<>();
        //TODO Task 6.2
        int listGloveSize = listGlove.size();

        int positionOfWord = listGloveSize;
        for (int i = 0; i < listGloveSize; i++)
        {
            if (listGlove.get(i).getVocabulary().equals(_word))
            {
                positionOfWord = i;
            }
        }
        if (positionOfWord == listGloveSize)
        {
            return WordsNearest("error");
        }

        Vector specifiedWordVector = listGlove.get(positionOfWord).getVector();
        for (int i = 0; i < listGloveSize; i++)
        {
            if (!_word.equals(listGlove.get(i).getVocabulary()))
            {
                Vector comparedVector = listGlove.get(i).getVector();
                double doubCS = specifiedWordVector.cosineSimilarity(comparedVector);
                CosSimilarityPair cosSimilarityPair = new CosSimilarityPair(_word, listGlove.get(i).getVocabulary(), doubCS);
                listCosineSimilarity.add(cosSimilarityPair);
            }
        }

        listCosineSimilarity = HeapSort.doHeapSort(listCosineSimilarity);
        return listCosineSimilarity;
    }

    public List<CosSimilarityPair> WordsNearest(Vector _vector) {
        List<CosSimilarityPair> listCosineSimilarity = new ArrayList<>();
        //TODO Task 6.3
        int listGloveSize = listGlove.size();
        for (int i = 0; i < listGloveSize; i++)
        {
            Vector comparedVector = listGlove.get(i).getVector();
            String comparedWord = listGlove.get(i).getVocabulary();
            if (!_vector.equals(comparedVector))
            {
                double doubCS = _vector.cosineSimilarity(comparedVector);
                listCosineSimilarity.add(new CosSimilarityPair(_vector, comparedWord, doubCS));
            }
        }
        listCosineSimilarity = HeapSort.doHeapSort(listCosineSimilarity);
        return listCosineSimilarity;
    }

    /**
     * Method to calculate the logical analogies by using references.
     * <p>
     * Example: uk is to london as china is to XXXX.
     *       _firISRef  _firTORef _secISRef
     * In the above example, "uk" is the first IS reference; "london" is the first TO reference
     * and "china" is the second IS reference. Moreover, "XXXX" is the vocabulary(ies) we'd like
     * to get from this method.
     * <p>
     * If _top <= 0, then returns an empty listResult.
     * If the vocabulary list does not include _secISRef or _firISRef or _firTORef, then returns an empty listResult.
     *
     * @param _secISRef The second IS reference
     * @param _firISRef The first IS reference
     * @param _firTORef The first TO reference
     * @param _top      How many vocabularies to include.
     */
    public List<CosSimilarityPair> LogicalAnalogies(String _secISRef, String _firISRef, String _firTORef, int _top) {
        List<CosSimilarityPair> listResult = new ArrayList<>();
        //TODO Task 6.4
        Vector firISVector = null;
        Vector secISVector = null;
        Vector firTOVector = null;
        for (int i = 0; i < listGlove.size(); i++) {
            if (listGlove.get(i).getVocabulary().equals(_firISRef)) {
                firISVector = listGlove.get(i).getVector();

            } else if (listGlove.get(i).getVocabulary().equals(_secISRef)) {
                secISVector = listGlove.get(i).getVector();

            } else if (listGlove.get(i).getVocabulary().equals(_firTORef)) {
                firTOVector = listGlove.get(i).getVector();

            }
        }
        if (firISVector != null && secISVector != null && firTOVector != null)
        {
            Vector secTOVector = secISVector.subtraction(firISVector).add(firTOVector);
            listResult = WordsNearest(secTOVector).subList(0, _top + 3);
            for (int i = 0; i < _top; i++)
            {
                if (listResult.get(i).getWord2().equals(_secISRef) || listResult.get(i).getWord2().equals(_firISRef) || listResult.get(i).getWord2().equals(_firTORef))
                {
                    listResult.remove(i);
                }
            }
            if (_top > 0)
            {
                listResult = listResult.subList(0, _top);
            }
            else
            {
                listResult = new ArrayList<>();
            }
        }
        return listResult;



    }
}