package domain.validators;

import domain.Nota;

public class NotaValidator implements Validator<Nota> {
    @Override
    public void validate(Nota entity) throws ValidationException {
        String mesaje = "";
        if (entity.getId() == null || entity.getId().equals(""))
            mesaje += "Id-ul notei este invalid!\n";

        if (entity.getId().fst == null || entity.getId().equals(""))
            mesaje += "Id-ul studentului este invalid!\n";

        if (entity.getId().snd == null || entity.getId().snd.equals(""))
            mesaje += "Id-ul temei este invalid!\n";

        if (entity.getProfesor() == null || entity.getProfesor().equals("")) {
            mesaje += "Profesorul este invalid!\n";
        }
        if (entity.getData() == null || entity.getData().equals("")) {
            mesaje += "Data este invalida!\n";
        }
        if (entity.getProfesor() == null || entity.getProfesor().equals("")) {
            mesaje += "Profesorul este invalid!\n";
        }
        if (entity.getValoare() < 0 || entity.getValoare() > 10) {
            mesaje += "Valoarea notei este invalida!\n";
        }
        if (!mesaje.equals("")) {
            throw new ValidationException(mesaje);
        }
    }
}
