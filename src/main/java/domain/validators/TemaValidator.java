package domain.validators;

import domain.Tema;

public class TemaValidator implements Validator<Tema> {

    @Override
    public void validate(Tema tema) throws ValidationException {
        String mesaje="";
        if(tema.getStartWeek()<1||tema.getDeadlineWeek()>14)
            mesaje+="startWeek este invalid!\n";

        if (tema.getDeadlineWeek() < 1 || tema.getDeadlineWeek() > 14)
            mesaje += "deadlineWeek invalid!\n";

        if(tema.getStartWeek()>tema.getDeadlineWeek())
            mesaje+="StartWeek trebuie sa fie mai mic decat DeadlineWeek!";

        if (!mesaje.equals(""))
            throw new ValidationException(mesaje);

    }
}
