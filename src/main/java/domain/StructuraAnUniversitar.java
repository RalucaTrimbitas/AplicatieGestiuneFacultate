package domain;

import java.sql.Struct;
import java.time.LocalDate;
import java.util.Calendar;

public class StructuraAnUniversitar extends Entity<String> {

    private static StructuraAnUniversitar instance = null;
    private int anUniversitar;
    private StructuraSemestru sem1, sem2;

    public StructuraAnUniversitar(String fileName) {
        this.anUniversitar = Calendar.getInstance().get(Calendar.YEAR);
        this.sem1 = new StructuraSemestru(1,fileName);
        this.sem2 = new StructuraSemestru(2,fileName);
    }

    public static StructuraAnUniversitar getInstance(String fileName) {
        if(instance==null){
            instance=new StructuraAnUniversitar(fileName);
        }
        return instance;
    }

    public static StructuraAnUniversitar getInstance(){
        return instance;
    }

    public static void setInstance(StructuraAnUniversitar instance) {
        StructuraAnUniversitar.instance = instance;
    }

    public StructuraSemestru getSemestru(LocalDate data){
        if (data.isAfter(sem1.getStartSemester()) && data.isBefore(sem1.getEndSemester()))
            return sem1;
        if (data.isAfter(sem2.getStartSemester()) && data.isBefore(sem2.getEndSemester()))
            return sem2;
        return null;
    }
    public int getWeek(LocalDate date,StructuraSemestru semestru){
        Calendar calendar=Calendar.getInstance();
        //calendar.setFirstDayOfWeek(5);
        calendar.set(date.getYear(),date.getMonthValue()-1,date.getDayOfMonth());
        int currentWeek=calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.set(semestru.getStartSemester().getYear(),semestru.getStartSemester().getMonthValue()-1,semestru.getStartSemester().getDayOfMonth());
        int startSemesterWeek=calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.set(semestru.getBeginHolyday().getYear(),semestru.getBeginHolyday().getMonthValue()-1,semestru.getBeginHolyday().getDayOfMonth());
        int beginHolydayWeek=calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.set(semestru.getEndHolyday().getYear(),semestru.getEndHolyday().getMonthValue()-1,semestru.getEndHolyday().getDayOfMonth());
        int endHolydayWeek=calendar.get(Calendar.WEEK_OF_YEAR);
        if (semestru.getStartSemester().isBefore(date) && semestru.getBeginHolyday().isAfter(date))
            return currentWeek - startSemesterWeek + 1;
        if (semestru.getEndSemester().isAfter(date) && semestru.getEndHolyday().isBefore(date))
            return(currentWeek - endHolydayWeek + 1) + (beginHolydayWeek - startSemesterWeek + 1);
        return currentWeek;
    }

    public int getAnUniversitar() {
        return anUniversitar;
    }

    public void setAnUniversitar(int anUniversitar) {
        this.anUniversitar = anUniversitar;
    }

    public StructuraSemestru getSem1() {
        return sem1;
    }

    public void setSem1(StructuraSemestru sem1) {
        this.sem1 = sem1;
    }

    public StructuraSemestru getSem2() {
        return sem2;
    }

    public void setSem2(StructuraSemestru sem2) {
        this.sem2 = sem2;
    }
}
