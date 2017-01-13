package fi.softala.tyokykypassi.tests;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.Button;

import fi.softala.tyokykypassi.R;
import fi.softala.tyokykypassi.activities.RegisterActivity;

/**
 * Created by jusju on 13.1.2017.
 */

public class RegisterActivityUnitTest extends ActivityUnitTestCase<RegisterActivity> {

    public RegisterActivityUnitTest() {
        super(RegisterActivity.class);
    }

    public void testRegister() {
        startActivity(new Intent(getInstrumentation().getTargetContext(), RegisterActivity.class), null, null);
        RegisterActivity rekisterointi = getActivity();

        Button registerButton = (Button) rekisterointi.findViewById(R.id.btnSubmitRegister);



    }
}
