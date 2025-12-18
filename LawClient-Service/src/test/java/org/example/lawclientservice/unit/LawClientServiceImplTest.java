package org.example.lawclientservice.unit;

import org.example.lawclientservice.domain.LawClient;
import org.example.lawclientservice.repository.LawClientRepository;
import org.example.lawclientservice.service.LawClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LawClientServiceImplTest {

    @Mock
    private LawClientRepository repository;

    @InjectMocks
    private LawClientServiceImpl service;

    @Test
    void shouldCreateLawClient() {
        // given
        LawClient client = new LawClient();
        client.setName("Jan Kowalski");

        LawClient saved = new LawClient();
        saved.setId("123");
        saved.setName("Jan Kowalski");

        when(repository.save(client)).thenReturn(saved);

        // when
        Optional<LawClient> result = service.createClient(client);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo("123");
        assertThat(result.get().getName()).isEqualTo("Jan Kowalski");

        verify(repository).save(client);
    }

    // ========= READ BY ID =========

    @Test
    void shouldReturnLawClientById_whenExists() {
        // given
        LawClient client = new LawClient();
        client.setId("123");

        when(repository.findById("123"))
                .thenReturn(Optional.of(client));

        // when
        Optional<LawClient> result = service.getLawClientByID("123");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo("123");

        verify(repository).findById("123");
    }

    @Test
    void shouldReturnEmpty_whenLawClientNotFoundById() {
        // given
        when(repository.findById("404"))
                .thenReturn(Optional.empty());

        // when
        Optional<LawClient> result = service.getLawClientByID("404");

        // then
        assertThat(result).isEmpty();

        verify(repository).findById("404");
    }

    // ========= READ BY NAME =========

    @Test
    void shouldReturnLawClientsByName_whenExists() {
        // given
        when(repository.findByName("Jan"))
                .thenReturn( List.of(new LawClient(), new LawClient()));

        // when
        List<LawClient> result = service.getLawClientsByName("Jan");

        // then
        assertThat(result.size()).isEqualTo(2);

        verify(repository).findByName("Jan");
    }

    @Test
    void shouldReturnEmptyList_whenLawClientNotFoundByName() {
        // given
        when(repository.findByName("Unknown"))
                .thenReturn(List.of());

        // when
        List<LawClient> result = service.getLawClientsByName("Unknown");

        // then
        assertThat(result.size()).isEqualTo(0);

        verify(repository).findByName("Unknown");
    }

    // ========= READ ALL =========

    @Test
    void shouldReturnAllLawClients() {
        // given
        when(repository.findAll())
                .thenReturn(List.of(new LawClient(), new LawClient(), new LawClient()));

        // when
        List<LawClient> result = service.getAllLawClients();

        // then
        assertThat(result.size()).isEqualTo(3);

        verify(repository).findAll();
    }

    // ========= UPDATE =========

    @Test
    void shouldUpdateLawClient_whenExists() {
        // given
        LawClient existing = new LawClient();
        existing.setId("123");
        existing.setName("Old Name");

        LawClient updatedData = new LawClient();
        updatedData.setName("New Name");

        when(repository.findById("123"))
                .thenReturn(Optional.of(existing));

        when(repository.save(existing))
                .thenReturn(existing);

        // when
        Optional<LawClient> result =
                service.updateLawClientById("123", updatedData);

        // then
        assertThat(result).isPresent();
        assertThat(existing.getName()).isEqualTo("New Name");

        verify(repository).findById("123");
        verify(repository).save(existing);
    }

    @Test
    void shouldNotUpdate_whenLawClientDoesNotExist() {
        // given
        when(repository.findById("404"))
                .thenReturn(Optional.empty());

        // when
        Optional<LawClient> result =
                service.updateLawClientById("404", new LawClient());

        // then
        assertThat(result).isEmpty();

        verify(repository).findById("404");
        verify(repository, never()).save(any());
    }

    // ========= DELETE BY ID =========

    @Test
    void shouldDeleteLawClient_whenExists() {
        // given
        LawClient client = new LawClient();
        client.setId("123");

        when(repository.findById("123"))
                .thenReturn(Optional.of(client));

        // when
        Optional<LawClient> result =
                service.deleteLawClientById("123");

        // then
        assertThat(result).isPresent();

        verify(repository).findById("123");
        verify(repository).delete(client);
    }

    @Test
    void shouldNotDelete_whenLawClientDoesNotExist() {
        // given
        when(repository.findById("404"))
                .thenReturn(Optional.empty());

        // when
        Optional<LawClient> result =
                service.deleteLawClientById("404");

        // then
        assertThat(result).isEmpty();

        verify(repository).findById("404");
        verify(repository, never()).delete(any());
    }

    // ========= DELETE ALL =========

    @Test
    void shouldDeleteAllLawClients() {
        // given
        when(repository.count()).thenReturn(5L);

        // when
        String result = service.deleteAllLawClients();

        // then
        assertThat(result)
                .isEqualTo("Deleted 5 LawClients. Database is empty!");

        verify(repository).count();
        verify(repository).deleteAll();
    }
}