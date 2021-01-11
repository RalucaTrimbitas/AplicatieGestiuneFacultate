package repository.FileRepository;

import domain.Entity;

/**
 * Clasa folosita pe post de interfata
 * Incapsuleaza metode specifice lucrului cu fisier
 * @param <E> tipului de obiect pe care il are
 * @param <ID> tipul id-ului obiectului respectiv
 */
public interface CrudFileRepository<ID,E extends Entity<ID>> {

    /**
     * Functie folosita pentru a scrie intr-un fisier tot continutul repository-ului
     */
    void writeAllToFile();

    /**
     * Functie folosita pentru a scrie un singur obiect in fisier
     */
    void writeOneToFile(E entity);

    /**
     * Functie folosita pentru a citi tot continutul fisierului
     * care apoi va fi incarcat in repository
     */
    void loadData();

}
