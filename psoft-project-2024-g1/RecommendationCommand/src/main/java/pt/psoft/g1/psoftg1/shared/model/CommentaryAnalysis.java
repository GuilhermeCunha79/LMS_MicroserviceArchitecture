package pt.psoft.g1.psoftg1.shared.model;

import java.util.Properties;

import edu.stanford.nlp.pipeline.*;

public class CommentaryAnalysis {

    public static Commentary analyzeSentiment(String text) {
        // Configurações para o pipeline do Stanford CoreNLP
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        CoreDocument doc = new CoreDocument(text);

        pipeline.annotate(doc);

        String sentiment = null;

        for (CoreSentence sentence : doc.sentences()) {
            sentiment = sentence.sentiment();
        }

        if (sentiment != null) {
            return switch (sentiment) {
                case "Very Positive", "Positive" -> Commentary.POSITIVE;
                default -> Commentary.NEGATIVE;
            };
        }

        return Commentary.NEGATIVE;
    }
}
