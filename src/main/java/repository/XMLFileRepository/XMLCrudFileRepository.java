package repository.XMLFileRepository;

import domain.Entity;

public interface XMLCrudFileRepository<ID,E extends Entity<ID>> {

    /**
     * Functie folosita pentru a scrie intr-un fisier tot continutul repository-ului
     */
    void writeAllToFile();

    /**
     * Functie folosita pentru a citi tot continutul fisierului
     * care apoi va fi incarcat in repository
     */
    void loadData();


}
