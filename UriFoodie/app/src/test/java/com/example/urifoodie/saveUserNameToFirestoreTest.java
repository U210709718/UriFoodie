package com.example.urifoodie;

import static org.mockito.Mockito.*;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

public class SaveUserNameToFirestoreTest {

    @Mock
    FirebaseFirestore mockFirestore;

    @Mock
    DocumentReference mockDocRef;

    private RegistrationActivity registrationActivity;

    @Before
    public void setup() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        registrationActivity = new RegistrationActivity();

        // Mock Firestore's document reference behavior
        when(mockFirestore.collection("Users")).thenReturn(mockCollectionRef -> {
            when(mockCollectionRef.document(anyString())).thenReturn(mockDocRef);
            return mockCollectionRef;
        });
    }

    @Test
    public void testSaveUserNameToFirestoreSuccess() {
        String userId = "testUserId";
        String username = "testUsername";

        // Mock Task for success
        Task<Void> mockSuccessTask = mock(Task.class);
        when(mockSuccessTask.isSuccessful()).thenReturn(true);
        when(mockDocRef.set(any(Map.class))).thenReturn(mockSuccessTask);

        // Call the method
        registrationActivity.firestore = mockFirestore;
        registrationActivity.saveUserNameToFirestore(userId, username);

        // Verify interactions
        verify(mockFirestore.collection("Users")).document(userId);
        verify(mockDocRef).set(argThat(userData ->
                userData instanceof Map && "testUsername".equals(((Map) userData).get("username"))
        ));
    }

    @Test
    public void testSaveUserNameToFirestoreFailure() {
        String userId = "testUserId";
        String username = "testUsername";

        // Mock Task for failure
        Task<Void> mockFailureTask = mock(Task.class);
        when(mockFailureTask.isSuccessful()).thenReturn(false);
        when(mockDocRef.set(any(Map.class))).thenReturn(mockFailureTask);

        // Call the method
        registrationActivity.firestore = mockFirestore;
        registrationActivity.saveUserNameToFirestore(userId, username);

        // Verify interactions
        verify(mockFirestore.collection("Users")).document(userId);
        verify(mockDocRef).set(argThat(userData ->
                userData instanceof Map && "testUsername".equals(((Map) userData).get("username"))
        ));
    }
}