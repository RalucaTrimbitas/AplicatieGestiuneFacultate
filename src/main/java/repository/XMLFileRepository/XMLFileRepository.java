package repository.XMLFileRepository;

import domain.Entity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import repository.InMemoryRepository.InMemoryRepository;
import domain.validators.ValidationException;
import domain.validators.Validator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public abstract class XMLFileRepository<E extends Entity<ID>, ID> extends InMemoryRepository<ID, E> implements XMLCrudFileRepository<ID, E> {
    private String fileName;

    public XMLFileRepository(Validator<E> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    @Override
    public E findOne(ID id) {
        return super.findOne(id);
    }

    @Override
    public Iterable<E> findAll() {
        return super.findAll();
    }

    @Override
    public E save(E entity) throws ValidationException {
        E rez = super.save(entity);
        if (rez == null)
            writeAllToFile();
        return rez;
    }

    @Override
    public E delete(ID id) {
        E rezultat = super.delete(id);
        writeAllToFile();
        return rezultat;
    }

    @Override
    public E update(E entity) {
        return null;
    }

    @Override
    public void loadData() {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(fileName);
            Element root = document.getDocumentElement();
            NodeList children = root.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node studentElement = children.item(i);
                if (studentElement instanceof Element) {
                    Entity entity = createEntityFromElement((Element) studentElement);
                    super.save((E) entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeAllToFile() {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .newDocument();
            Element root = document.createElement("entities");
            document.appendChild(root);
            super.findAll().forEach(s -> {
                Element e = createElementfromEntity(document, s);
                root.appendChild(e);
            });

            Transformer transformer = TransformerFactory.
                    newInstance().newTransformer();

            Source source=new DOMSource(document);

            transformer.transform(source,
                    new StreamResult(fileName));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract E createEntityFromElement (Element entityElement);

    public abstract Element createElementfromEntity (Document document, E entity);

}
