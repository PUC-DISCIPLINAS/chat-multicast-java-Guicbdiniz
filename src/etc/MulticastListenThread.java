package etc;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MulticastClientThread extends Thread {
    private DataOutputStream out;

    public MulticastClientThread(DataOutputStream out,) {
        this.out = out;
        this.start();
    }

    public void run() {

    }
}
