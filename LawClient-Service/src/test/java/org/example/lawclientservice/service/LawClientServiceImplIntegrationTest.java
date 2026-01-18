package org.example.lawclientservice.service;

import org.example.lawclientservice.domain.LawClient;
import org.example.lawclientservice.repository.LawClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataMongoTest
@Import(LawClientServiceImpl.class)
class LawClientServiceImplIntegrationTest {

    @Autowired
    private LawClientRepository repository;

    @Autowired
    private LawClientServiceImpl service;

    private LawClient client1;
    private LawClient client2;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        client1 = new LawClient(UUID.randomUUID(), "Adam Nowak", List.of());
        client2 = new LawClient(UUID.randomUUID(), "Ewa Kowalska", List.of());

        repository.save(client1);
        repository.save(client2);
    }

    @Test
    void testCreateClient() {
        LawClient newClient = new LawClient(null, "Janusz Biznes", List.of());
        Optional<LawClient> created = service.createClient(newClient);

        assertThat(created).isPresent();
        assertThat(created.get().getId()).isNotNull();
        assertThat(created.get().getName()).isEqualTo("Janusz Biznes");

        List<LawClient> all = repository.findAll();
        assertThat(all).hasSize(3);
    }

    @Test
    void testGetLawClientByID() {
        Optional<LawClient> found = service.getLawClientByID(client1.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Adam Nowak");
    }

    @Test
    void testGetLawClientsByName() {
        List<LawClient> result = service.getLawClientsByName("Ewa Kowalska");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(client2.getId());
    }

    @Test
    void testGetAllLawClients() {
        List<LawClient> all = service.getAllLawClients();

        assertThat(all).hasSize(2);
    }

    @Test
    void testUpdateLawClientById() {
        LawClient updatedInfo = new LawClient(null, "Adam Nowak Updated", List.of());

        Optional<LawClient> updated = service.updateLawClientById(client1.getId(), updatedInfo);

        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("Adam Nowak Updated");

        Optional<LawClient> fromDb = repository.findById(client1.getId());
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get().getName()).isEqualTo("Adam Nowak Updated");
    }

    @Test
    void testDeleteLawClientById() {
        Optional<LawClient> deleted = service.deleteLawClientById(client2.getId());

        assertThat(deleted).isPresent();
        assertThat(deleted.get().getName()).isEqualTo("Ewa Kowalska");

        List<LawClient> all = repository.findAll();
        assertThat(all).hasSize(1);
    }

    @Test
    void testDeleteAllLawClients() {
        String result = service.deleteAllLawClients();

        assertThat(result).contains("Deleted 2 LawClients");

        List<LawClient> all = repository.findAll();
        assertThat(all).isEmpty();
    }
}