package project.year.afinal.studentadvice;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;
import android.app.*;
import android.content.*;
import android.test.*;
import android.test.suitebuilder.annotation.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNull.*;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;
import static project.year.afinal.studentadvice.R.id.*;

import org.junit.Test;
import org.junit.runner.RunWith;


import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("project.year.afinal.studentadvice", appContext.getPackageName());
    }

    @Test
    public void testPost_AdviceCard() throws Exception {
        //test creating post
        Post post = new Post("myUID", "tester", "myTestTitle", "myTestMessage", 3, 3, 3, 3);
        assertEquals("tester", post.getAuthor());
        assertEquals("myTestTitle", post.getTitle());
        assertEquals("3 Disagree", post.getDisagreeMsg());
        assertEquals("3 Agree", post.getAgreeMsg());

        //test setting advice key
        post.setAdviceKey("myAdviceKey");
        assertEquals("myAdviceKey", post.getAdviceKey());

        //test preview
        assertEquals("myTest...", post.getMassagePreview(9));

        //test creating advice card
        Context context = returnContext();
        AdviceCard adviceCard = new AdviceCard(context,post);
        assertEquals(post, adviceCard.getPost());

    }

    @Test
    public void testUser()
    {
        User user = new User("TestUser", "test@email.com");
        assertEquals("TestUser",user.getName());
        assertEquals("test@email.com",user.getEmail());
    }

    /*@Test
    public void validInput()
    {
        SignupActivity signupActivity = new SignupActivity();
        signupActivity.editTextName = new EditText(returnContext());
        signupActivity.editTextEmail = new EditText(returnContext());
        signupActivity.editTextPassword = new EditText(returnContext());
        signupActivity.editRetypeTextPassword = new EditText(returnContext());
        signupActivity.editTextName.setText("name");
        signupActivity.editTextEmail.setText("name.email.com");
        signupActivity.editTextPassword.setText("Password1");
        signupActivity.editRetypeTextPassword.setText("Password1");
        assertEquals(true, signupActivity.isTextValidateForSignup());


        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editRetypeTextPassword = (EditText) findViewById(R.id.editRetypeTextPassword);
    }

    @Test
    public void testAValidUserCanLogIn() {

        Instrumentation instrumentation = getInstrumentation();

        // Register we are interested in the authentication activiry...
        Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(SignupActivity.class.getName(), null, false);

        // Start the authentication activity as the first activity...
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(instrumentation.getTargetContext(), SignupActivity.class.getName());
        instrumentation.startActivitySync(intent);

        // Wait for it to start...
        Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5);
        assertThat(currentActivity, is(notNullValue()));

        // Type into the username field...
        View currentView = currentActivity.findViewById(editTextName);
        assertThat(currentView, is(notNullValue()));
        assertThat(currentView, instanceOf(EditText.class));
        instrumentation.sendStringSync("TestName");

        // Type into the password field...
        currentView = currentActivity.findViewById(editTextEmail);
        assertThat(currentView, is(notNullValue()));
        assertThat(currentView, instanceOf(EditText.class));
        instrumentation.sendStringSync("test@email.com");

        // Type into the password field...
        currentView = currentActivity.findViewById(editTextPassword);
        assertThat(currentView, is(notNullValue()));
        assertThat(currentView, instanceOf(EditText.class));
        instrumentation.sendStringSync("TestPass1");

        // Type into the password field...
        currentView = currentActivity.findViewById(editRetypeTextPassword);
        assertThat(currentView, is(notNullValue()));
        assertThat(currentView, instanceOf(EditText.class));
        instrumentation.sendStringSync("TestPass1");


        // Register we are interested in the welcome activity...
        // this has to be done before we do something that will send us to that
        // activity...
        instrumentation.removeMonitor(monitor);
        monitor = instrumentation.addMonitor(SignupActivity.class.getName(), null, false);

        // Click the login button...
        currentView = currentActivity.findViewById(buttonSignup);
        assertThat(currentView, is(notNullValue()));
        assertThat(currentView, instanceOf(Button.class));

        // Wait for the welcome page to start...
        currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5);
        assertThat(currentActivity, is(notNullValue()));
    }*/

    private Context returnContext()
    {
        return InstrumentationRegistry.getTargetContext();
    }
}
