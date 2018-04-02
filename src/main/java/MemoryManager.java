/**
 * @author Pavel Gordon
 */
public class MemoryManager
{
    public final int[] memory;
    public final int MODULE_SIZE = 30;


    public MemoryManager(int memoryLocationsAmount)
    {
        memory = new int[memoryLocationsAmount * MODULE_SIZE];
    }


    public synchronized void store(int value, int position)
    {
        memory[position] = value;
    }


    public synchronized int fetch(int position)
    {
        return memory[position];
    }

}
