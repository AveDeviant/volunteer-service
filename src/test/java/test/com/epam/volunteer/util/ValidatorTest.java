package test.com.epam.volunteer.util;

import com.epam.volunteer.util.Validator;
import org.junit.Test;

public class ValidatorTest {

    @Test
    public void checkEmailInvalid() {
      assert !Validator.checkEmail("test");
    }

    @Test
    public void checkEmailInvalid2() {
        assert  !Validator.checkEmail("test.mail.com");
    }

    @Test
    public void checkEmailValid() {
        assert  Validator.checkEmail("test@mail.com");
    }

    @Test
    public void checkEmailInvalid3() {
        assert  !Validator.checkEmail(null);
    }
}
