package step.learning.ioc;

import com.google.inject.Singleton;

import java.util.logging.Level;
import java.util.logging.Logger;


@Singleton
public class ByeService implements PartingService{
    private final Logger logger = Logger.getLogger(String.valueOf(ByeService.class));

    @Override
    public void sayGoodbye() {
        System.out.println("bye");
    }
}
