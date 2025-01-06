package pt.psoft.g1.recommendation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import pt.psoft.g1.psoftg1.recommendationmanagement.model.Recommendation;
import pt.psoft.g1.psoftg1.shared.model.Commentary;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RecommendationTest {

    private Recommendation recommendation;

    @BeforeEach
    void setUp() {
        recommendation = new Recommendation("12345", "reader001", "9783161484100", Commentary.POSITIVE);
    }

    @Test
    void testConstructor() {
        Assertions.assertNotNull(recommendation);
        Assertions.assertNotNull(recommendation.getRecommendationNumber());
        Assertions.assertEquals("12345", recommendation.getLendingNumber());
        Assertions.assertEquals("reader001", recommendation.getReaderDetailsId());
        Assertions.assertEquals("9783161484100", recommendation.getIsbn());
        Assertions.assertEquals(Commentary.POSITIVE, recommendation.getCommentary());
    }

    @Test
    void testSetLendingNumber() {
        recommendation.setLendingNumber("54321");
        Assertions.assertEquals("54321", recommendation.getLendingNumber());
    }

    @Test
    void testSetReaderDetailsId() {
        recommendation.setReaderDetailsId("reader002");
        Assertions.assertEquals("reader002", recommendation.getReaderDetailsId());
    }

    @Test
    void testSetIsbn() {
        recommendation.setIsbn("978-1-4028-9462-6");
        Assertions.assertEquals("978-1-4028-9462-6", recommendation.getIsbn());
    }

    @Test
    void testSetCommentary() {
        recommendation.setCommentary(Commentary.NEGATIVE);
        Assertions.assertEquals(Commentary.NEGATIVE, recommendation.getCommentary());
    }

}
