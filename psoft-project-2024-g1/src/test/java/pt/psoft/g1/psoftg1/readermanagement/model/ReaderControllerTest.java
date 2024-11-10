package pt.psoft.g1.psoftg1.readermanagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.external.service.ApiNinjasService;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderController;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderQuoteView;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderViewMapper;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.usermanagement.model.Librarian;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;


class ReaderControllerTest {

    @Mock
    private ReaderService readerService;
    @Mock
    private UserService userService;
    @Mock
    private ReaderViewMapper readerViewMapper;

    @InjectMocks
    private ReaderController readerController;

    @Mock
    private Authentication authentication;
    @Mock
    private ApiNinjasService apiNinjasService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDataAsReader_returnsReaderDetails() {
        // Arrange
        User user = mock(User.class);
        when(userService.getAuthenticatedUser(authentication)).thenReturn(user);
        ReaderDetails readerDetails = mock(ReaderDetails.class);
        when(readerService.findByUsername(user.getUsername())).thenReturn(Optional.of(readerDetails));

        // Act
        ResponseEntity<?> response = readerController.getData(authentication);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        verify(readerService, times(1)).findByUsername(user.getUsername());
    }

    @Test
    void getDataAsLibrarian_returnsAllReaders() {
        // Arrange
        Librarian librarian = mock(Librarian.class);
        when(userService.getAuthenticatedUser(authentication)).thenReturn(librarian);
        when(readerService.findAll()).thenReturn(List.of(mock(ReaderDetails.class)));

        // Act
        ResponseEntity<?> response = readerController.getData(authentication);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        verify(readerService, times(1)).findAll();
    }

    @Test
    void findByReaderNumber_foundReader_returnsOk() {
        // Arrange
        int year = 2024;
        int seq = 1;
        String readerNumber = year + "/" + seq;

        // Mocking ReaderDetails and BirthDate
        ReaderDetails readerDetails = mock(ReaderDetails.class);
        BirthDate birthDate = mock(BirthDate.class);
        LocalDate mockBirthDate = LocalDate.of(1990, 5, 15);

        // Configuring mocks
        when(readerService.findByReaderNumber(readerNumber)).thenReturn(Optional.of(readerDetails));
        when(readerDetails.getBirthDate()).thenReturn(birthDate);
        when(birthDate.getBirthDate()).thenReturn(mockBirthDate);
        when(apiNinjasService.getRandomEventFromYearMonth(1990, 5)).thenReturn("Sample Quote");

        // Mocking ReaderQuoteView response
        ReaderQuoteView readerQuoteView = mock(ReaderQuoteView.class);
        when(readerViewMapper.toReaderQuoteView(readerDetails)).thenReturn(readerQuoteView);

        // Act
        ResponseEntity<ReaderQuoteView> response = readerController.findByReaderNumber(year, seq);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        verify(readerService, times(1)).findByReaderNumber(readerNumber);
        verify(apiNinjasService, times(1)).getRandomEventFromYearMonth(1990, 5);
    }

    @Test
    void findByReaderNumber_notFound_throwsNotFoundException() {
        // Arrange
        int year = 2024;
        int seq = 1;
        String readerNumber = year + "/" + seq;
        when(readerService.findByReaderNumber(readerNumber)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> readerController.findByReaderNumber(year, seq));
    }
}