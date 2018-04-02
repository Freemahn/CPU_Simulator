import java.util.Arrays;

/**
 * @author Pavel Gordon
 */
public class Instruction
{
    private Command command;
    private int[] args;


    public Instruction(int... args)
    {
        this.args = args;
    }


    public Instruction(Command command, int... args)
    {
        this.command = command;
        this.args = args;
    }


    public Instruction(Command command, int v1)
    {
        this.command = command;
        this.args = new int[] {v1};
    }


    public Instruction(Command command, int v1, int v2)
    {
        this.command = command;
        this.args = new int[] {v1, v2};
    }


    public Instruction(Command command, int v1, int v2, int v3)
    {
        this.command = command;
        this.args = new int[] {v1, v2, v3};
    }


    enum Command
    {
        ADD, MULT, STORE, FETCH, EQU, PUSH, POP, GOTO, LOAD, IF, DEBUG, END, OUT
    }


    @Override
    public String toString()
    {
        return command + " " + Arrays.toString(args);
    }


    public Command getCommand()
    {
        return command;
    }


    public void setCommand(Command command)
    {
        this.command = command;
    }


    public int[] getArgs()
    {
        return args;
    }


    public int arg(int index)
    {
        return args[index];
    }


    public void setArgs(int[] args)
    {
        this.args = args;
    }
}
