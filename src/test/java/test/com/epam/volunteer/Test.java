package test.com.epam.volunteer;

import com.epam.volunteer.service.MedicamentService;
import com.epam.volunteer.service.impl.MedicamentServiceImpl;
import org.junit.Assert;

/**
 * Created by Acer on 12/10/2017.
 */
public class Test {

    //проверка работы
    @org.junit.Test
    public void getMedicament(){
        MedicamentService service = new MedicamentServiceImpl();
        Assert.assertEquals(service.getAll().size(),1);
    }
}
