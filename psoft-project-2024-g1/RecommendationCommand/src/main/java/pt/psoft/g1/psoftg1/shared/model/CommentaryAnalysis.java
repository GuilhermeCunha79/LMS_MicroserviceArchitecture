package pt.psoft.g1.psoftg1.shared.model;

import java.util.Properties;

import edu.stanford.nlp.pipeline.*;

public class CommentaryAnalysis {

    public static Commentary analyzeSentiment(String text) {
        // Validações do comentário
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("The commentary cannot be null or empty.");
        }

        if (text.matches("\\d+")) {
            throw new IllegalArgumentException("The commentary contains only numbers.");
        }

        if (text.matches("[^a-zA-Z0-9]+")) {
            throw new IllegalArgumentException("The commentary contains only special characters.");
        }

        // Configurações para o pipeline do Stanford CoreNLP
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // Analisar o texto
        CoreDocument doc = new CoreDocument(text);
        pipeline.annotate(doc);

        String sentiment = null;

        // Extrair o sentimento para cada sentença
        for (CoreSentence sentence : doc.sentences()) {
            sentiment = sentence.sentiment();
        }

        // Determinar o tipo de comentário com base no sentimento
        if (sentiment != null) {
            return switch (sentiment) {
                case "Very Positive", "Positive" -> Commentary.POSITIVE;
                default -> Commentary.NEGATIVE;
            };
        }

        // Retorno padrão caso o sentimento não seja identificado
        return Commentary.NEGATIVE;
    }
}
