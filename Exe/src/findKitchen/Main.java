package findKitchen;
import java.util.*;

public class Main {

    public static void main(String[] args)
    {
        if(args.length == 0)
        {
            System.out.println("Error: no path ");
            System.exit(0);
        }

        Program program = new Program();
        try {
            program.findLocationOfKitchen(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
