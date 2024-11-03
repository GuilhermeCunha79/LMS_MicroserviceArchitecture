package pt.psoft.g1.psoftg1.readermanagement.model;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.readermanagement.services.UpdateReaderRequest;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ReaderDetailsTest {

    private Reader reader;
    private List<Genre> interestList;

    @BeforeEach
    public void setUp() {
        reader = new Reader("userna22me@gmail.com", "paRss!*ord5!");
        interestList = new ArrayList<>();
        interestList.add(new Genre("Infantil"));
    }

    @Test
    public void testConstructorWithValidArguments() {
        reader = new Reader("userna22me@gmail.com", "paRss!*ord5!");
        ReaderDetails readerDetails = new ReaderDetails(123, reader, "1990-01-01", "223456789", true, true, true, "photoURI", interestList);

        assertThat(readerDetails).isNotNull();
        assertThat(readerDetails.getReaderNumber()).isEqualTo(LocalDate.now().getYear() + "/123");
        assertThat(readerDetails.getBirthDate().toString()).isEqualTo("1990-1-1");
        assertThat(readerDetails.getPhoneNumber()).isEqualTo("223456789");
        assertThat(readerDetails.isGdprConsent()).isTrue();
    }

    @Test
    public void testConstructorWithNullReader() {
        assertThatThrownBy(() -> new ReaderDetails(123, null, "1990-01-01", "123456789", true, true, true, "photoURI", interestList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Provided argument resolves to null object");
    }

    @Test
    public void testConstructorWithNullPhoneNumber() {
        assertThatThrownBy(() -> new ReaderDetails(123, reader, "1990-01-01", null, true, true, true, "photoURI", interestList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Provided argument resolves to null object");
    }

    @Test
    public void testConstructorWithFalseGdpr() {
        assertThatThrownBy(() -> new ReaderDetails(123, reader, "1990-01-01", "123456789", false, true, true, "photoURI", interestList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Provided argument resolves to null object");
    }

  /*  @Test
    public void testApplyPatchWithValidData() {
        reader = new Reader("userna22me@gmail.com", "paRss!*ord5!");
        ReaderDetails readerDetails = new ReaderDetails(123, reader, "1990-01-01", "223456789", true, true, true, "photoURI", interestList);
        long currentVersion = 1L;

        UpdateReaderRequest request = new UpdateReaderRequest();
        request.setUsername("newUsername@gmail.com");
        request.setPassword("newPasswor67d!");
        request.setFullName("newFullName");
        request.setBirthDate("1995-1-1");
        request.setPhoneNumber("987654321");
        request.setMarketing(false);
        request.setThirdParty(true);

        readerDetails.applyPatch(currentVersion, request, "newPhotoURI", interestList);

        assertThat(readerDetails.getReader().getUsername()).isEqualTo("newUsername");
        assertThat(readerDetails.getReader().getPassword()).isEqualTo("newPassword");
        assertThat(readerDetails.getReader().getName()).isEqualTo("newFullName");
        assertThat(readerDetails.getBirthDate().toString()).isEqualTo("1995-01-01");
        assertThat(readerDetails.getPhoneNumber()).isEqualTo("987654321");
        assertThat(readerDetails.isMarketingConsent()).isFalse();
        assertThat(readerDetails.isThirdPartySharingConsent()).isTrue();
    }

    @Test
    public void testRemovePhotoWithMismatchedVersion() {
        reader = new Reader("userna22me@gmail.com", "paRss!*ord5!");
        ReaderDetails readerDetails = new ReaderDetails(123, reader, "1990-01-01", "223456789", true, true, true, "photoURI", interestList);

        assertThatThrownBy(() -> readerDetails.removePhoto(323 + 1))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Cannot invoke \"java.lang.Long.longValue()\" because \"this.version\" is null");
    }

  @Test
    public void testRemovePhotoWithMatchingVersion() {
        reader = new Reader("userna22me@gmail.com", "paRss!*ord5!");
        ReaderDetails readerDetails = new ReaderDetails(123, reader, "1990-01-01", "223456789", true, true, true, "photoURI", interestList);


        readerDetails.removePhoto(1L);

        assertThat(readerDetails.getPhoto()).isNull();
    }*/
}
