import java.io.IOException;

/**
 * @author Pavel Gordon
 */
public class Main
{
    public static void main(String[] args) throws IOException
    {
        MemoryManager memoryManager = new MemoryManager(4);
        CPU cpu = new CPU("C:\\Projects\\CPU_Simulator\\src\\main\\resources\\input1.txt", 1, memoryManager);
        new Thread(cpu).start();
    }


}
